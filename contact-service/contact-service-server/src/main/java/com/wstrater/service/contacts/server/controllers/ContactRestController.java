package com.wstrater.service.contacts.server.controllers;

import java.util.ArrayList;
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

import com.wstrater.service.contacts.common.ContactConstants;
import com.wstrater.service.contacts.common.data.Contact;
import com.wstrater.service.contacts.server.data.ContactDTO;
import com.wstrater.service.contacts.server.data.ContactRepository;

@RestController
public class ContactRestController {

  public final static String HOME_MESSAGE = "Hello From " + ContactRestController.class.getName();

  private final static org.slf4j.Logger logger1 = org.slf4j.LoggerFactory.getLogger(ContactRestController.class);
  private final static org.apache.log4j.Logger logger2 = org.apache.log4j.Logger.getLogger(ContactRestController.class);

  @Autowired
  private ContactRepository contactRepository;

  @RequestMapping(ContactConstants.HOME_PATH)
  public String defaultHome() {
    logger1.info("1: ContactRestController: defaultHome");
    logger2.info("2: ContactRestController: defaultHome");

    return HOME_MESSAGE;
  }

  @RequestMapping(ContactConstants.CONTACTS_ID_PATH)
  public ResponseEntity<Contact> contactsById(@PathVariable Integer id) {
    ResponseEntity<Contact> ret = null;

    logger1.info("1: ContactRestController: contactsById - " + id);
    logger2.info("2: ContactRestController: contactsById - " + id);

    ContactDTO dto = this.contactRepository.findById(id);
    if (dto == null) {
      ret = new ResponseEntity(HttpStatus.NOT_FOUND);
    } else {
      ret = ResponseEntity.ok(new Contact(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getEmail()));
    }

    return ret;
  }

  @RequestMapping(ContactConstants.CONTACTS_USER_ID_PATH)
  public Collection<Contact> contactsByUserId(@PathVariable String userId) {
    Collection<Contact> ret = new ArrayList<>();

    logger1.info("1: ContactRestController: contactsByUserId - " + userId);
    logger2.info("2: ContactRestController: contactsByUserId - " + userId);

    Collection<ContactDTO> dtos = this.contactRepository.findByUserId(userId);
    if (dtos != null) {
      for (ContactDTO dto : dtos) {
        ret.add(new Contact(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getEmail()));
      }
    }
    
    if (ret.isEmpty()) {
      throw new ResourceNotFoundException();
    }

    return ret;
  }

  void setContactRepository(ContactRepository contactRepository) {
    this.contactRepository = contactRepository;
  }
  
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public class ResourceNotFoundException extends RuntimeException {
  }

}