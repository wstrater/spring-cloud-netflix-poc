package com.wstrater.service.passport.server;

import java.util.EnumSet;
import javax.servlet.DispatcherType;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.wstrater.commons.filter.LoggingFilter;
import com.wstrater.commons.filter.LoggingRequestInterceptor;
import com.wstrater.commons.log.LogUtil;

@Configuration
public class PassportServiceConfiguration {

  private final static Logger logger = LoggerFactory.getLogger(PassportServiceConfiguration.class);

  @Order(1)
  @Bean
  public FilterRegistrationBean loggingFilterRegistration() {
    FilterRegistrationBean ret = new FilterRegistrationBean();

    logger.info("loggingFilterRegistration");

    ret.setFilter(new LoggingFilter());
    ret.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
    ret.addUrlPatterns("/*");

    return ret;
  }

  @Bean
  public RequestInterceptor feignRequestInterceptor() {
    System.out.println("feignRequestInterceptor");

    return new RequestInterceptor() {
      
      private BasicAuthRequestInterceptor basicAuth = new BasicAuthRequestInterceptor("userName", "password");

      @Override
      public void apply(RequestTemplate template) {
        logger.info(String.format("RequestInterceptor.apply: %s", template.toString()));

        LogUtil.setMDC(LogUtil.SERVICE_URL, template.request().url());

        basicAuth.apply(template);

        template.header(LogUtil.REFERER, LogUtil.getMDCString(LogUtil.REQUEST_URL));
        // Set remote REQUEST_ID to TRANSACTION_ID
        template.header(LogUtil.REQUEST_ID, LogUtil.getMDCString(LogUtil.TRANSACTION_ID));
        template.header(LogUtil.REFERENCE_ID, LogUtil.getMDCString(LogUtil.REFERENCE_ID));
        template.header(LogUtil.TRACKING_ID, LogUtil.getMDCString(LogUtil.TRACKING_ID));
        template.header(LogUtil.X_FOWARDED_FOR_HEADER, LogUtil.getMDCString(LogUtil.CLIENT_IP_ADDRESS));
      }
    };
  }

}