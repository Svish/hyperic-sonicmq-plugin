/**
 * 
 */
package no.lemontree.hyperic.sonic;

import java.util.Properties;

import org.hyperic.util.config.ConfigResponse;

/**
 * Single place of getting runtime configuration from objects.
 */
public class Configuration
{
	public final String domain;
	public final String location;
	public final String username;
	public final String password;
	public final String id;
	

	/**
	 * Constructor used by {@link Detector}.
	 */
	public Configuration(ConfigResponse config)
	{
		domain = config.getValue("ds.domain");
		location = config.getValue("ds.location");
		username = config.getValue("ds.username");
		password = config.getValue("ds.password");
		id = null; // Unused by detector
	}

	/**
	 * Constructor used by {@link DomainCollector}.
	 */
	public Configuration(Properties properties)
	{
		domain = properties.getProperty("ds.domain");
		location = properties.getProperty("ds.location");
		username = properties.getProperty("ds.username");
		password = properties.getProperty("ds.password");
		// If there's no explicit id, we assume we want the domain directory service id  
		id = properties.getProperty("id", domain + ".DIRECTORY SERVICE:ID=DIRECTORY SERVICE");
	}

	/**
	 * Constructor used by tests.
	 */
	public Configuration(String domain, String location, String username, String password)
	{
		this.domain = domain;
		this.location = location;
		this.username = username;
		this.password = password;
		this.id = null;
	}	
}