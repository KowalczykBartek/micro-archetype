package com.micro.archetype.configuration;

import org.glassfish.jersey.server.ResourceConfig;

public class ArchetypeConfiguration extends ResourceConfig
{
	public ArchetypeConfiguration()
	{
		packages("com.micro.archetype");
	}
}