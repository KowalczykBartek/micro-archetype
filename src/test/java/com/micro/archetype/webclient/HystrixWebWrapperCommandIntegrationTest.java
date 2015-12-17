package com.micro.archetype.webclient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.util.Map;
import java.util.function.Supplier;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import static org.junit.Assert.*;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;

public class HystrixWebWrapperCommandIntegrationTest
{
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void shouldMakeSuccessfulCallThroughHystrixWrapper() throws Exception
	{
		//given
		final Map<String,String> responseBody = ImmutableMap.of("key","value");
		stubFor(get(urlEqualTo("/assureHystrixCommand"))//
				.willReturn(aResponse()//
						.withHeader("Content-Type", "application/json")//
						.withStatus(200)//
						.withBody(objectMapper.writeValueAsString(responseBody))));

		WebTarget target = getClientForPath("assureHystrixCommand");
		Supplier<Response> call = () -> target.request(MediaType.APPLICATION_JSON_TYPE).get();

		//when
		HystrixWebWrapper<Response> callWrapper = new HystrixWebWrapper<>("sample-call",call);

		//then
		assertTrue(callWrapper.run().getStatus() == 200);
	}

	//TODO extract it better
	private WebTarget getClientForPath(final String path){
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8089").path(path);
		return target;
	}
}
