package com.wstrater.service.contacts.server;

import java.util.EnumSet;
import javax.servlet.DispatcherType;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.wstrater.commons.filter.LoggingFilter;

@Configuration
public class ContactServiceConfiguration {

  @Order(1)
  @Bean
  public FilterRegistrationBean loggingFilterRegistration() {
    FilterRegistrationBean ret = new FilterRegistrationBean();
    
    System.out.println("loggingFilterRegistration");

    ret.setFilter(new LoggingFilter());
    ret.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
    ret.addUrlPatterns("/*");

    return ret;
  }

}