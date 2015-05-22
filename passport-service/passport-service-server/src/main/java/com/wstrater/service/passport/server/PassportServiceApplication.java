package com.wstrater.service.passport.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@EnableEurekaClient
//@EnableFeignClients(basePackageClasses = {com.wstrater.service.contacts.client.ContactService.class})
@EnableFeignClients(basePackages = {"com.wstrater.service.contacts.client"})
@SpringCloudApplication
@ComponentScan({"com.wstrater.service.passport.server", "com.wstrater.service.contacts.client"})
public class PassportServiceApplication {

  private final static Logger logger = LoggerFactory.getLogger(PassportServiceApplication.class);

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(PassportServiceApplication.class, args);
  }

  @Autowired
  void setMessage(@Value("${message}") String m) {
    logger.info("message = " + m);
  }

}