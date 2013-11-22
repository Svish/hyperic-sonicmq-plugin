package no.lemontree.sonic.config;


import no.lemontree.Properties;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComponentOptionsTest
{

	@Test
	public void IdIsOverriddenProperly()
	{
		Properties p = new Properties();
		p.setProperty(Options.Component.Id, "test");
		
		ComponentOptions c = new ComponentOptions(p);
		
		assertEquals("test", c.id);
	}
	
}
