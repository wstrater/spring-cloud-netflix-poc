package com.wstrater.service.passport.server;

import java.util.Properties;

import com.netflix.blitz4j.LoggingConfiguration;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.stereotype.Component;

@Component
public class PassportServiceInitializer implements ApplicationListener<ContextRefreshedEvent> {

  public void onApplicationEvent(ContextRefreshedEvent event) {
    System.out.println("\nSystem.getProperties: ");
    for (Object key : new java.util.TreeSet(System.getProperties().keySet())) {
      // System.out.println(String.format("%s=%s", String.valueOf(key), System.getProperty(String.valueOf(key))));
    }

    Properties props = new Properties();
    // props.setProperty("log4j.rootCategory", "OFF");
    props.setProperty("log4j.logger.asyncAppenders", "INFO,stdout,file");
    props.setProperty("batcher.com.netflix.logging.AsyncAppender.stdout.waitTimeinMillis", "120000");
    LoggingConfiguration.getInstance().configure(props);
  }

}