package com.wstrater.service.contacts.server;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.wstrater.service.contacts.common.ContactConstants;
import com.wstrater.service.contacts.server.controllers.ContactRestController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ContactServiceApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class ContactServiceIntegrationTest {

  @Value("${local.server.port}")
  private int port;

	private URL baseURL;
	private RestTemplate template;

	@Before
	public void setUp() throws Exception {
		baseURL = new URL(String.format("http://localhost:%d%s", port, ContactConstants.HOME_PATH));
		System.out.println(baseURL.toString());
		template = new TestRestTemplate();
	}

	@Test
	public void getHome() throws Exception {
		ResponseEntity<String> response = template.getForEntity(baseURL.toString(), String.class);
		assertThat(response.getBody(), equalTo(ContactRestController.HOME_MESSAGE));
	}

}