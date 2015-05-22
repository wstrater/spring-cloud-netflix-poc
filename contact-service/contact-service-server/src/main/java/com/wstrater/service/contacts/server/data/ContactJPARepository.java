package com.wstrater.service.contacts.server.data;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactJPARepository extends JpaRepository<ContactDTO, Integer> {
  
  public ContactDTO findById(Integer id);
  
  public Collection<ContactDTO> findByUserId(String userId);

}