package no.lemontree.sonic.config;

import java.util.Properties;

/**
 * Names used for options in the plugin descriptor. 
 */
public final class Options
{
	private Options() {}
	
	
	
	public static final class Domain
	{
		private Domain() {}
		public static final String Name = "ds.domain";
		public static final String Location = "ds.location";
		public static final String Username = "ds.username";
		public static final String Password = "ds.password";
	}
	
	
	
	public static class Component
	{
		private Component() {}
		public static final String Id = "id";
	}
	
	
	
	public static final class Container extends Component
	{
		private Container() {}
	}
	
	
	
	public static final class Broker extends Component
	{
		private Broker() {}
	}
	
	
	
	public static final class Queue extends Component
	{
		private Queue() {}
		
		public static final String BrokerUrl = "broker.url";
		public static final String BrokerUsername = "broker.username";
		public static final String BrokerPassword = "broker.password";
		public static final String Name = "queue";
	}
	
	/**
	 * Makes sure properties include what we expect.
	 * 
	 * @param properties Properties to check
	 * @param expectedOptions Name of options which is expected to exist
	 * @throws IllegalArgumentException When an expected option is missing
	 */
	public static void validate(Properties properties, String... expectedOptions)
	{
		for(String o : expectedOptions)
			if( ! properties.containsKey(o))
				throw new IllegalArgumentException("Missing expected option '"+o+"'");
	}
}
