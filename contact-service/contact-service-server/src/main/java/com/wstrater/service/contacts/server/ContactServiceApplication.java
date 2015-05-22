package com.wstrater.service.contacts.server;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.ApplicationContext;

@SpringCloudApplication
public class ContactServiceApplication {

  private final static Logger logger = LoggerFactory.getLogger(ContactServiceApplication.class);
  
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ContactServiceApplication.class, args);

		/*
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
				System.out.println(beanName);
		}
		*/
	}

  @Autowired
  void setMessage(@Value("${message}") String m) {
    logger.info("message = " + m);
    System.out.println("message = " + m);
  }

}