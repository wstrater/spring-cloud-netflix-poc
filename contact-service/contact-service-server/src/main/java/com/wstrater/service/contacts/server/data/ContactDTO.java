package com.wstrater.service.contacts.server.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ContactDTO {

  private String email;
  private String firstName;
  @Id
  @GeneratedValue
  private Integer id;
  private String lastName;
  private String userId;

  public ContactDTO() {
  }

  public ContactDTO(String userId, String firstName, String lastName, String email) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public ContactDTO(Integer id, String userId, String firstName, String lastName, String email) {
    this(userId, firstName, lastName, email);
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return "Contact{" +
        "id=" + id +
        ", userId='" + userId + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        '}';
  }

}
