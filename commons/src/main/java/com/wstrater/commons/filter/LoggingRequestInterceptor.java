package com.wstrater.commons.filter;

/*
import feign.RequestInterceptor;
import feign.RequestTemplate;

import com.wstrater.commons.log.LogUtil;
*/

public class LoggingRequestInterceptor /* implements RequestInterceptor */ {

  //
  //  Can't have Feign in base commons. Maybe commons-feign.
  //

/*
  @Override
  public void apply(RequestTemplate template) {
    System.out.println("RequestInterceptor.apply");

    template.header(LogUtil.X_FOWARDED_FOR, LogUtil.getMDCString(LogUtil.CLIENT_IP_ADDRESS));
    // Set remote REQUEST_ID to TRANSACTION_ID
    template.header(LogUtil.REQUEST_ID, LogUtil.getMDCString(LogUtil.TRANSACTION_ID));
    template.header(LogUtil.REFERENCE_ID, LogUtil.getMDCString(LogUtil.REFERENCE_ID));
    template.header(LogUtil.TRACKING_ID, LogUtil.getMDCString(LogUtil.TRACKING_ID));
  }
*/

}