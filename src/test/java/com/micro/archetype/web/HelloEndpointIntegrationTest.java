package com.micro.archetype.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.micro.archetype.configuration.ArchetypeConfiguration;
import com.micro.archetype.services.HelloService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class HelloEndpointIntegrationTest extends JerseyTest
{
	@Autowired
	HelloService helloService;

	@Override
	protected javax.ws.rs.core.Application configure()
	{
		return new ArchetypeConfiguration()//
				.property("contextConfigLocation", "classpath:applicationContext.xml");
	}

	@Test
	public void shouldReturnGreetingFromHelloService() throws JsonProcessingException
	{
		//when
		final Response response = target() //
				.path("status") //
				.request().get();

		//then
		final String responseAsString = response.readEntity(String.class);

		assertThat(response.getStatus(), is(HttpStatus.SC_OK));
		assertThat(responseAsString,equalTo(helloService.getHelloMessage()));
	}
}
