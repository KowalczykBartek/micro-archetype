package com.micro.archetype.services.impl;

import java.util.function.Supplier;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.micro.archetype.services.WeatherService;
import com.micro.archetype.webclient.HystrixWebWrapper;
import com.micro.archetype.webclient.WebClient;

@Component
public class OpenWeatherMapImpl implements WeatherService
{
	@Value("${openweathermap.uri.api}")
	private String openWeatherApi;

	@Value("${openweathermap.uri.key}")
	private String apiKey;

	@Override
	public String getWeatherForCity(final String cityName)
	{
		final Client client = WebClient.getClient();

		final Supplier<Response> call = () -> {
			final Response response = client.target(constructSearchForCityWeatherUri(cityName))//
					.request()//
					.get();
			return response;
		};

		HystrixWebWrapper<Response> openweathermapCommand = new HystrixWebWrapper<>("openweathermap-command", call);

		final Response response = openweathermapCommand.execute();

		return response.readEntity(String.class);
	}

	//helpers
	private String constructSearchForCityWeatherUri(final String city)
	{

		return String.format("%s/weather?q=%s&appid=%s", openWeatherApi, city, apiKey);
	}
}
