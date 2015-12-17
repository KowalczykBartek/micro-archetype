package com.micro.archetype.webclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static testutils.Utils.getClientForPath;

import java.util.Map;
import java.util.function.Supplier;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
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
		final Map<String, String> responseBody = ImmutableMap.of("key", "value");
		stubFor(get(urlEqualTo("/assureHystrixCommand"))//
				.willReturn(aResponse()//
						.withHeader("Content-Type", "application/json")//
						.withStatus(HttpStatus.SC_OK)//
						.withBody(objectMapper.writeValueAsString(responseBody))));

		final WebTarget target = getClientForPath("assureHystrixCommand");
		final Supplier<Response> call = () -> target.request(MediaType.APPLICATION_JSON_TYPE).get();

		//when
		Response response = new HystrixWebWrapper<>("sample-call", call).run();

		//then
		assertTrue(response.getStatus() == (HttpStatus.SC_OK));

		Map<String, Object> mappedResponse = response.readEntity(Map.class);

		assertThat(mappedResponse.get("key"), is("value"));
	}
}
