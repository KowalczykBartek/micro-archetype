package com.micro.archetype.web;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.micro.archetype.configuration.ArchetypeConfiguration;
import com.micro.archetype.dto.WeatherDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class WeatherEndpointIntegrationTest extends JerseyTest
{
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	@Value("${openweathermap.uri.key}")
	private String apiKey; //notice that this is value from test.properties

	@Override
	protected javax.ws.rs.core.Application configure()
	{
		return new ArchetypeConfiguration()//
				.property("contextConfigLocation", "classpath:applicationContext.xml");
	}

	@Test
	public void ShouldReturnWeatherInformationForCity() throws JsonProcessingException
	{
		//given
		final String responseFromWeatherService = "{\n" +
				"\t\"name\":\"Katowice\",\n" +
				"\t\"id\" : \"f23423\",\n" +
				"\t\"cod\":\"aaw2d\"\n" +
				"}";

		final String desiredCity = "Katowice";

		wireMockRule.stubFor(get(urlEqualTo("/weather?q=" + desiredCity + "&appid=" + apiKey))//
				.willReturn(aResponse()//
						.withHeader("Content-Type", "application/json")//
						.withBody(responseFromWeatherService)));

		//when
		final Response response = target() //
				.path("weather") //
				.path(desiredCity)//
				.request()//
				.get();

		//then
		assertThat(response.getStatus(), equalTo(HttpStatus.SC_OK));
		final WeatherDTO weather = response.readEntity(WeatherDTO.class);
		assertThat(weather.getName(), equalTo(desiredCity));
	}
}
