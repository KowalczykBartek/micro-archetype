package com.micro.archetype

import groovyx.net.http.RESTClient
import spock.lang.Specification

import static groovyx.net.http.ContentType.JSON

class WeatherEndpoint extends Specification {

	def RESTClient localClient = new RESTClient(Configuration.localAddress, JSON)

	def "Should return weather information for city passed as path param"() {
		given:
		def desiredCity = "Sosnowiec"

		when:
		def response = localClient.get(path: "/weather/${desiredCity}");

		then:
		response.responseData.name == desiredCity
	}
}
