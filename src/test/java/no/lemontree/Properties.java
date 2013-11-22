package no.lemontree;

import java.io.IOException;

/**
 * Properties for integration tests. Loads {@link #FILE} in constructor.
 */
public class Properties extends java.util.Properties
{
	private static final long serialVersionUID = 1L;
	
	private static final String FILE = "/it.properties";
	
	public Properties()
	{
		try
		{
			load(getClass().getResourceAsStream(FILE));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
