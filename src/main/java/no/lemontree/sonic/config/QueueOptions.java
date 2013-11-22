package no.lemontree.sonic.config;

import static no.lemontree.sonic.config.Options.validate;

import java.util.Properties;

public class QueueOptions extends ComponentOptions
{
	public final String url;
	public final String queue;
	public final String username;
	public final String password;
	
	public QueueOptions(Properties properties)
	{
		super(properties);
		
		validate(properties,
				Options.Queue.BrokerUrl,
				Options.Queue.BrokerUsername,
				Options.Queue.BrokerPassword,
				Options.Queue.Name);
		
		url = properties.getProperty(Options.Queue.BrokerUrl);
		queue = properties.getProperty(Options.Queue.Name);
		username = properties.getProperty(Options.Queue.BrokerUsername);
		password = properties.getProperty(Options.Queue.BrokerPassword);
	}
}