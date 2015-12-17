package com.micro.archetype.webclient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.micro.archetype.webclient.mappers.JSONMapperProvider;

public class WebClient
{
	public static Client getClient(){

		final Client client = ClientBuilder.newClient();
		client.register(JSONMapperProvider.class);

		return client;
	}
}
