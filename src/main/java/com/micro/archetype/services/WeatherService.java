package com.micro.archetype.services;

import com.micro.archetype.dto.WeatherDTO;

public interface WeatherService
{
	WeatherDTO getWeatherForCity(final String cityName);
}
