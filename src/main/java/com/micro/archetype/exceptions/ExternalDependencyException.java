package com.micro.archetype.exceptions;

/**
 * Exception that indicate that something during communication with external system/dependency went wrong,
 * e.g when calling REST endpoint and when 500 is returned we can throw ExternalDependencyException to indicate
 * that maybe Hystrix circuit breaker should be opened.
 */
public class ExternalDependencyException extends RuntimeException
{
	public ExternalDependencyException()
	{
		super("Exception during external call");
	}

	public ExternalDependencyException(final String message)
	{
		super(message);
	}
}
