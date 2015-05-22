package com.wstrater.service.contacts.client;

import java.util.Collection;
import java.util.Collections;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

//import com.wstrater.commons.log.ErrorMessage;
//import com.wstrater.commons.log.LogUtil;
//import com.wstrater.commons.log.ServiceMessage;
//import com.wstrater.commons.log.TransactionMessage;
import com.wstrater.service.contacts.common.data.Contact;

@Component
public class ContactServiceCommand {

  private final static Logger logger = Logger.getLogger(ContactServiceCommand.class);
//  private final static Logger transactionLogger = Logger.getLogger(TransactionMessage.LOGGER_NAME);

  @Autowired
  private ContactService contactService;

  public Collection<Contact> contactsByUserId(String userId) {
    Collection<Contact> ret = null;
    
//    LogUtil.removeMDC(LogUtil.SERVICE_FAILED);
//    LogUtil.setMDC(LogUtil.SERVICE_NAME, ContactService.CLIENT_NAME);

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      ret = contactsByUserIdPrimary(userId);
    } finally {
      stopWatch.stop();
//      transactionLogger.info(ServiceMessage.builder(String.format("contactsByUserId: %s", userId))
//          .duration(stopWatch.getTotalTimeMillis()).build());
    }
    
    return ret;
  }

  private Collection<Contact> contactsByUserIdFallback(String userId) {
//    LogUtil.setMDC(LogUtil.SERVICE_FAILED, Boolean.TRUE);
//    transactionLogger.error(new ErrorMessage(String.format("contactsByUserIdFallback: %s", userId)));
    return Collections.emptyList();
  }

  @HystrixCommand(fallbackMethod = "contactsByUserIdFallback")
  private Collection<Contact> contactsByUserIdPrimary(String userId) {
//    logger.info(String.format("contactsByUserIdPrimary: %s", userId));
    return contactService.contactsByUserId(userId);
  }

}