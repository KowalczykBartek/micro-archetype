package testutils;


import javax.ws.rs.client.WebTarget;

import com.micro.archetype.webclient.WebClient;

public class Utils
{
	public static WebTarget getClientForPath(final String path)
	{
		return WebClient.getClient().target("http://localhost:8089").path(path);
	}
}
