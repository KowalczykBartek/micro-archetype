package com.micro.archetype.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface HelloService
{
	String getHelloMessage() throws JsonProcessingException;
}
