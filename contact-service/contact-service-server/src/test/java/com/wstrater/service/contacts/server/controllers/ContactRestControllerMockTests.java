package com.wstrater.service.contacts.server.controllers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.wstrater.commons.test.TestMatcher.containsAllItems;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.wstrater.service.contacts.common.ContactConstants;
import com.wstrater.service.contacts.server.data.ContactJPARepository;
import com.wstrater.service.contacts.server.data.ContactRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class ContactRestControllerMockTests {

  private ContactRestController controller;
  private MockMvc mvc;

  @Autowired
  private ApplicationContext applicationContext;
 
//  @Autowired
  private ContactRepository repository;

//  @Autowired
  private ContactJPARepository jpaRepository;

 	@Before
	public void setUp() throws Exception {
	  // applicationContext.getAutowireCapableBeanFactory().createBean(com.wstrater.service.contacts.server.data.ContactJPARepository.class);
	  controller = new ContactRestController();
	  repository = new ContactRepository();
	  repository.initialize();
	  controller.setContactRepository(repository);

		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void getContactsById() throws Exception {
	  System.out.println("ContactRestControllerMockTestsget: getContactsById");
	  List<String> names = Arrays.asList("Dave","Syer");
		MvcResult result = mvc.perform(MockMvcRequestBuilders
		    .get(ContactConstants.CONTACTS_ID_PATH, "1")
		    .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Dave")))
				.andExpect(content().string(containsAllItems(names)))
				.andReturn();
	}

	@Test
	public void getContactsByUserId() throws Exception {
	  System.out.println("ContactRestControllerMockTestsget: getContactsByUserId");
	  List<String> names = Arrays.asList("Dave","Syer","Phil","Webb","Juergen","Hoeller");
		MvcResult result = mvc.perform(MockMvcRequestBuilders
		    .get(ContactConstants.CONTACTS_USER_ID_PATH, "wstrater")
		    .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Dave")))
				.andExpect(content().string(containsAllItems(names)))
				.andReturn();
	}

	@Test
	public void getHome() throws Exception {
	  System.out.println("ContactRestControllerMockTests: getHome");
		mvc.perform(MockMvcRequestBuilders.get(ContactConstants.HOME_PATH).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo(ContactRestController.HOME_MESSAGE)));
	}

	@Test
	public void getNotFound() throws Exception {
	  System.out.println("ContactRestControllerMockTests: getNotFound");
		mvc.perform(MockMvcRequestBuilders
		    .get(ContactConstants.CONTACTS_USER_ID_PATH, "NoOne")
		    .accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
	
}