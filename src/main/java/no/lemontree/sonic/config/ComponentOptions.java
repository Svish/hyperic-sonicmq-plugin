package no.lemontree.sonic.config;

import static no.lemontree.sonic.config.Options.validate;

import java.util.Properties;

public class ComponentOptions extends DomainOptions
{
	public ComponentOptions(Properties properties)
	{
		super(properties);
		
		validate(properties, 
				Options.Component.Id);
	}
}