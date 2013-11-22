package no.lemontree.sonic.config;

import java.util.Properties;

import org.junit.Test;

public class OptionsTest {

	@Test
	public void validate_Good_NoException()
	{
		Properties p = new Properties();
		p.setProperty("a", "b");
		Options.validate(p, "a");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void validate_Bad_ThrowsException()
	{
		Properties p = new Properties();
		Options.validate(p, "a");
	}
}
