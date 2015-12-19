package testutils;


import javax.ws.rs.client.WebTarget;

import com.micro.archetype.webclient.WebClient;

public class Utils
{
	public static WebTarget getClientForPath(final String path, final int port)
	{
		return WebClient.getClient().target(String.format("http://localhost:%s", port)).path(path);
	}
}
