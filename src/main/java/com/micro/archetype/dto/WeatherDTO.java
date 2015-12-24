package com.micro.archetype.dto;

/*
 Abstraction over json returned from external weather service
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherDTO
{
	private final String id;
	private final String name;
	private final String cod;

	@JsonCreator
	public WeatherDTO(@JsonProperty("id") final String id, @JsonProperty("name") final String name,
			@JsonProperty("cod") final String cod)
	{
		this.id = id;
		this.name = name;
		this.cod = cod;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getCod()
	{
		return cod;
	}
}