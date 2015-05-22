package com.wstrater.service.passport.common.data;

import java.util.Collections;
import java.util.Collection;

import com.wstrater.service.contacts.common.data.Contact;

public class Passport {

  private Collection<Contact> contacts;

  public Passport() {
  }

  public Passport(Collection<Contact> contacts) {
    this.contacts = contacts;
  }

  public Collection<Contact> getContacts() {
    return Collections.unmodifiableCollection(contacts);
  }

  @Override
  public String toString() {
    return "Passport{" +
        "contacts=" + contacts +
        '}';
  }

}