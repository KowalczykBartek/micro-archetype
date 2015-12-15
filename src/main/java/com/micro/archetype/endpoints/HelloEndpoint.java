package com.micro.archetype.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Endpoint that stands here only for test purposes
 */
@Path("status")
public class HelloEndpoint
{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getIt()
	{
		return Response.ok().build();
	}
}
