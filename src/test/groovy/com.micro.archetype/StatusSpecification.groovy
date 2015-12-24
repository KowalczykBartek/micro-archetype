package com.micro.archetype

import groovyx.net.http.RESTClient
import spock.lang.Specification

import static groovyx.net.http.ContentType.JSON

class StatusSpecification extends Specification {

	def RESTClient localClient = new RESTClient(Configuration.localAddress, JSON)

	def "Should return response OK for status endpoint"() {
		given:

		when:
		def response = localClient.get(path: '/status')

		then:
		response.status == 200
	}
}