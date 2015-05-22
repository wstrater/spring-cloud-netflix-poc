package com.wstrater.service.passport.server.controllers;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

//import com.wstrater.service.contacts.client.ContactService;
import com.wstrater.service.contacts.client.ContactServiceCommand;
//import com.wstrater.service.passport.server.ContactService;
import com.wstrater.service.contacts.common.data.Contact;
import com.wstrater.service.passport.common.PassportContstants;
import com.wstrater.service.passport.common.data.Passport;

@RestController
public class PassportRestController {

  public final static String HOME_MESSAGE = "Hello From " + PassportRestController.class.getName();

  private final static org.slf4j.Logger logger1 = org.slf4j.LoggerFactory.getLogger(PassportRestController.class);
  private final static org.apache.log4j.Logger logger2 = org.apache.log4j.Logger.getLogger(PassportRestController.class);

  @Autowired
  private ContactServiceCommand contactService;

  @RequestMapping(PassportContstants.HOME_PATH)
  public String defaultHome() {
    logger1.info("1: PassportRestController: defaultHome");
    logger2.info("2: PassportRestController: defaultHome");

    return HOME_MESSAGE;
  }

  @RequestMapping(PassportContstants.PASSPORT_USER_ID_PATH)
  public ResponseEntity<Passport> passportByUserId(@PathVariable String userId) {
    ResponseEntity<Passport> ret = null;

    logger1.info("1: PassportRestController: passportByUserId - " + userId);
    logger2.info("2: PassportRestController: passportByUserId - " + userId);

    Collection<Contact> contacts = contactService.contactsByUserId(userId);
    if (contacts == null || contacts.isEmpty()) {
      ret = new ResponseEntity(HttpStatus.NOT_FOUND);
    } else {
      ret = ResponseEntity.ok(new Passport(contacts));
    }

    return ret;
  }

}