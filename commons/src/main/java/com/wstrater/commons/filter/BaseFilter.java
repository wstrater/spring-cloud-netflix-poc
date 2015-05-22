package com.wstrater.commons.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.TreeSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wstrater.commons.log.LogUtil;
import com.wstrater.commons.util.Compare;

public abstract class BaseFilter implements Filter {

  private static final String[] INTERNAL_IP_PREFIXES = new String[] { "10.", "127.", "169.254.", "172.16.",
      "172.17.", "172.18.", "172.19.", "172.20.", "172.21.", "172.22.", "172.23.", "172.24.",
      "172.25.", "172.26.", "172.27.", "172.28.", "172.29.", "172.30.", "172.31.", "192.168." };

  private FilterConfig config;
  private Logger dumpLogger = Logger.getLogger(getClass().getName() + ".dump");

  public void destroy() {
  }

  public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

  public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
      doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    } else {
      chain.doFilter(request, response);
    }
  }

  protected void dump(HttpServletRequest request) {
    if (dumpLogger.isDebugEnabled()) {
      dumpLogger.debug("==================== Request ====================");

      dumpLogger.debug("getAuthType=" +  request.getAuthType());
      dumpLogger.debug("getContextPath=" + request.getContextPath());
      dumpLogger.debug("getMethod=" + request.getMethod());
      dumpLogger.debug("getPathInfo=" + request.getPathInfo());
      dumpLogger.debug("getProtocol=" + request.getProtocol());
      dumpLogger.debug("getQueryString=" + request.getQueryString());
      dumpLogger.debug("getRemoteAddr=" + request.getRemoteAddr());
      dumpLogger.debug("getRemoteHost=" + request.getRemoteHost());
      dumpLogger.debug("getRemotePort=" + request.getRemotePort());
      dumpLogger.debug("getRemoteUser=" + request.getRemoteUser());
      dumpLogger.debug("getRequestURI=" + request.getRequestURI());
      dumpLogger.debug("getRequestURL=" + request.getRequestURL());
      dumpLogger.debug("getScheme=" + request.getScheme());
      dumpLogger.debug("getServerName=" + request.getServerName());
      dumpLogger.debug("getServerPort=" + request.getServerPort());
      dumpLogger.debug("getUserPrincipal=" + request.getUserPrincipal());

      dumpLogger.debug("Headers:");
      for (String name : new TreeSet<String>(Collections.list(request.getHeaderNames()))) {
        Object value = request.getHeader(name);
        if (value != null) {
          dumpLogger.debug(name + "=" + String.valueOf(value));
        }
      }

      dumpLogger.debug("Attributes:");
      for (String name : new TreeSet<String>(Collections.list(request.getAttributeNames()))) {
        Object value = request.getAttribute(name);
        if (value != null) {
          dumpLogger.debug(name + "=" + String.valueOf(value));
        }
      }

      dumpLogger.debug("Parameters:");
      for (String name : new TreeSet<String>(Collections.list(request.getParameterNames()))) {
        String[] values = request.getParameterValues(name);
        if (values != null) {
          for (String value : values) {
            if (name.toLowerCase().indexOf("passw") >= 0) {
              dumpLogger.debug(name + "=" + ((Compare.isBlank(value) ? "" : "********")));
            } else {
              dumpLogger.debug(name + "=" + value);
            }
          }
        }
      }

      dumpLogger.debug("Cookies:");
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          dumpLogger.debug(
              cookie.getName() + "=" + cookie.getValue() + " " + cookie.getPath() + " "
                  + cookie.getMaxAge());
        }
      }

      dumpLogger.debug("-------------------- Request --------------------");
    }
  }

  protected String getClientIPAddress(HttpServletRequest request) {
    String ret = null;

    String forwardedFor = request.getHeader(LogUtil.X_FOWARDED_FOR_HEADER);
    if (Compare.isNotBlank(forwardedFor)) {
      String[] ipAddresses = forwardedFor.split("[,]");
      for (String ipAddress : ipAddresses) {
        String testAddress = ipAddress == null ? null : ipAddress.trim();
        if (Compare.isNotBlank(testAddress)) {
          if (isInternalIPAddress(testAddress)) {
            if (Compare.isBlank(ret)) {
              // Default to the first IP address
              ret = testAddress;
            }
          } else {
            ret = testAddress;
            break;
          }
        }
      }
    }

    if (Compare.isBlank(ret)) {
      ret = request.getRemoteAddr();
    }

    return ret;
  }

  protected FilterConfig getFilterConfig() {
    return config;
  }

  public void init(FilterConfig config) {
    this.config = config;
  }

  protected boolean isInternalIPAddress(String ipAddress) {
    boolean ret = false;

    if (Compare.isNotBlank(ipAddress)) {
      try {
        InetAddress addr = InetAddress.getByName(ipAddress);
        ret = addr.isSiteLocalAddress() || addr.isLoopbackAddress();
      } catch (UnknownHostException ee) {
        for (String prefix : INTERNAL_IP_PREFIXES) {
          if (ipAddress.startsWith(prefix)) {
            ret = true;
            break;
          }
        }
      }
    }

    return ret;
  }

}