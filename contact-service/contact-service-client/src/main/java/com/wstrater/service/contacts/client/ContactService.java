package com.wstrater.service.contacts.client;

import java.util.Collection;

import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wstrater.service.contacts.common.ContactConstants;
import com.wstrater.service.contacts.common.data.Contact;

//@Configuration
@FeignClient(ContactService.CLIENT_NAME)
public interface ContactService {

  public final static String CLIENT_NAME = "contact-service";

  @RequestMapping(method = RequestMethod.GET, value = ContactConstants.CONTACTS_USER_ID_PATH)
  public Collection<Contact> contactsByUserId(@PathVariable("userId") String userId);

}