package com.micro.archetype.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.archetype.services.HelloService;

import jersey.repackaged.com.google.common.collect.ImmutableMap;

public class HelloServiceImpl implements HelloService
{
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String getHelloMessage() throws JsonProcessingException
	{
		return objectMapper.writeValueAsString(ImmutableMap.of("k", "v"));
	}
}
