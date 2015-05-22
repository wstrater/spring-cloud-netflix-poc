package com.wstrater.commons.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.wstrater.commons.log.LogUtil;
import com.wstrater.commons.log.ErrorMessage;
import com.wstrater.commons.log.StartMessage;
import com.wstrater.commons.log.TransactionMessage;
import com.wstrater.commons.util.Compare;

public class LoggingFilter extends BaseFilter  {

  private final static Logger transactionLogger = Logger.getLogger(TransactionMessage.LOGGER_NAME);

  @Override
  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    LogUtil.markMDCStart(true);

    HttpSession session = request.getSession(false);
    if (session != null) {
      LogUtil.setMDC(LogUtil.SESSION_ID, session.getId());
    }

    LogUtil.setMDC(LogUtil.CLIENT_IP_ADDRESS, getClientIPAddress(request));
    LogUtil.setMDC(LogUtil.REFERENCE_ID, request.getHeader(LogUtil.REFERENCE_ID));
    LogUtil.setMDC(LogUtil.REFERER, request.getHeader(LogUtil.REFERER_HEADER));
    LogUtil.setMDC(LogUtil.REQUEST_ID, request.getHeader(LogUtil.REQUEST_ID));
    LogUtil.setMDC(LogUtil.REQUEST_URI, request.getRequestURI());
    LogUtil.setMDC(LogUtil.REQUEST_URL, request.getRequestURL());
    LogUtil.setMDC(LogUtil.TRACKING_ID, request.getHeader(LogUtil.TRACKING_ID));
    LogUtil.setMDC(LogUtil.USER_AGENT, request.getHeader(LogUtil.USER_AGENT_HEADER));

    if (Compare.isBlank(LogUtil.getMDC(LogUtil.REFERENCE_ID))) {
      // Set REFERENCE_ID to the first TRANSACTION_ID
      LogUtil.setMDC(LogUtil.REFERENCE_ID, LogUtil.getMDC(LogUtil.TRANSACTION_ID), false);
    }

    transactionLogger.debug(LogUtil.toStringMDC());

    transactionLogger.info(StartMessage.builder().build());
    try {

      dump(request);

      chain.doFilter(request, response);
    } catch (IOException | ServletException ee) {
      transactionLogger.error(ErrorMessage.builder(ee).build(), ee);
      throw ee;
    } finally {
      transactionLogger.info(TransactionMessage.builder().build());
      LogUtil.clearMDC();
    }
  }

}