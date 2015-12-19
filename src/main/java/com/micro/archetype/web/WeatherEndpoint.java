package com.micro.archetype.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;

import com.micro.archetype.services.WeatherService;

@Path("weather")
public class WeatherEndpoint
{
	@Autowired
	private WeatherService weatherService;

	@Path("/{city}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeatherForCity(@NotBlank @PathParam("city") final String city){

		return Response.ok(weatherService.getWeatherForCity(city)).build();
	}
}
