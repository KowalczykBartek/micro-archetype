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
import java.util.stream.IntStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;
import com.micro.archetype.exceptions.ExternalDependencyException;

public class HystrixWebWrapperCommandIntegrationTest
{
	private final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	private ObjectMapper objectMapper = new ObjectMapper();

	public static int CIRCUIT_BREAKER_THRESHOLD = 20; // see {@link HystrixCommandProperties
	// .default_circuitBreakerRequestVolumeThreshold}

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

		final WebTarget target = getClientForPath("assureHystrixCommand",PORT);
		final Supplier<Response> call = () -> target.request(MediaType.APPLICATION_JSON_TYPE).get();

		//when
		Response response = new HystrixWebWrapper<>("sample-call", call).run();

		//then
		assertTrue(response.getStatus() == (HttpStatus.SC_OK));

		Map<String, Object> mappedResponse = response.readEntity(Map.class);

		assertThat(mappedResponse.get("key"), is("value"));
	}

	@Test
	public void shouldOpenCircuitBreaker() throws Exception
	{
		//given
		stubFor(get(urlEqualTo("/alwaysreturn500"))//
				.willReturn(aResponse()//
						.withHeader("Content-Type", "application/json")//
						.withStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR)));

		final WebTarget target = getClientForPath("alwaysreturn500",PORT);

		final Supplier<Response> call = () -> {
			final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

			//sample logic, base on hystrix can deduce that something went wrong, and it should
			//open circuit breaker
			if (response.getStatus() == 500 || response.getStatus() == 404)
			{
				//for now, threshold is set to 20 - it is default value
				throw new ExternalDependencyException();
			}
			return response;
		};

		//when
		callEndpointAtLeast21Times(call);

		//Hystrix circuit breaker logic consists of 10 "1 second buckets", so we want to be
		//sure that at least one second passed, to give ocassion for hystrix to open circuit breaker
		//see more https://github.com/Netflix/Hystrix/wiki/How-it-Works#CircuitBreaker
		Thread.sleep(1000);

		//then
		assertTrue(new HystrixWebWrapper<>("sample-call", call).isCircuitBreakerOpen());
	}

	private void callEndpointAtLeast21Times(final Supplier<Response> call)
	{
		IntStream.range(0,CIRCUIT_BREAKER_THRESHOLD)//
				.forEach(i -> new HystrixWebWrapper<>("sample-call", call).execute());
	}
}
