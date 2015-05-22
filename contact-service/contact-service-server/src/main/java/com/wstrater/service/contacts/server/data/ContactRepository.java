package com.wstrater.service.contacts.server.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.context.annotation.Bean;

import com.wstrater.commons.util.Compare;

@Component
public class ContactRepository {

  private List<ContactDTO> repository = new ArrayList<>();

  @Autowired
  private ContactJPARepository jpaRepository;

  public ContactDTO findById(Integer id) {
    ContactDTO ret = null;
    
    if (jpaRepository == null) {
      if (id != null) {
        for (ContactDTO dto : repository) {
          if (id.equals(dto.getId())) {
            ret = dto;
            break;
          }
        }
      }
    } else {
      ret = jpaRepository.findById(id);
    }
      
    return ret;
  }

  public Collection<ContactDTO> findByUserId(String userId) {
    Collection<ContactDTO> ret = new ArrayList<>();
    
    if (jpaRepository == null) {
      if (Compare.isNotBlank(userId)) {
        for (ContactDTO dto : repository) {
          if (userId.equals(dto.getUserId())) {
            ret.add(dto);
          }
        }
      }
    } else {
      ret = jpaRepository.findByUserId(userId);
    }
      
    return ret;
  }

  public void initialize() {
    int id = 0;
    for (String userId : "wstrater,jlong,rwinch,dsyer,pwebb,sgibb".split(",")) {
      for (String fullName : "Dave,Syer;Phil,Webb;Juergen,Hoeller".split(";")) {
        String[] names  = fullName.split(",");
        if (jpaRepository == null) {
          repository.add(new ContactDTO(++id, userId, names[0], names[1], (names[0] + "." + names[1]).toLowerCase() + "@email.com"));
        } else {
          jpaRepository.save(new ContactDTO(userId, names[0], names[1], (names[0] + "." + names[1]).toLowerCase() + "@email.com"));
        }
      }
    }
  }

}