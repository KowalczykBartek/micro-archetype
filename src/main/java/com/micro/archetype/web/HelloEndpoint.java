package com.micro.archetype.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.micro.archetype.services.HelloService;

/**
 * Endpoint that stands here only for test purposes
 */
@Path("status")
public class HelloEndpoint
{
	@Autowired
	private HelloService helloService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIt() throws JsonProcessingException
	{
		return Response.ok(helloService.getHelloMessage()).build();
	}
}
