/**
 * 
 */
package no.lemontree.sonic.config;

import static no.lemontree.sonic.config.Options.validate;

import java.util.Properties;

/**
 * Configuration for domains.
 */
public class DomainOptions
{
	public final String domain;
	public final String location;
	public final String username;
	public final String password;
	public final String id;

	public DomainOptions(Properties properties)
	{
		validate(properties, 
				Options.Domain.Name,
				Options.Domain.Location,
				Options.Domain.Username,
				Options.Domain.Password);
		
		domain = properties.getProperty(Options.Domain.Name);
		location = properties.getProperty(Options.Domain.Location);
		username = properties.getProperty(Options.Domain.Username);
		password = properties.getProperty(Options.Domain.Password);
		id = properties.getProperty(Options.Component.Id, domain + ".DIRECTORY SERVICE:ID=DIRECTORY SERVICE");
	}
}