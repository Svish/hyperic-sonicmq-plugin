package no.lemontree.hyperic.sonic.detect;

import java.util.ArrayList;
import java.util.HashSet;

import no.lemontree.hyperic.sonic.Configuration;

import org.hyperic.hq.product.ServiceResource;
import org.junit.Test;
import static org.junit.Assert.*;

public class ITDomainScanner
{
	private static final String DOMAIN = "Domain1";
	private static final String LOCATION = "tcp://localhost:2506";
	private static final String USERNAME = "Administrator";
	private static final String PASSWORD = "Administrator";

	@Test
	public void findsServices() throws Exception
	{
		Configuration config = new Configuration(DOMAIN, LOCATION, USERNAME, PASSWORD);
		
		ArrayList<ServiceResource> services = new DomainScanner(null, config)
			.findServices();
		
		HashSet<Class> types = new HashSet<Class>();
		for(ServiceResource s : services)
			types.add(s.getClass());
		
		// NOTE: No idea what's actually in the domain, but we should at least discover one of each service type
		assertEquals(3, types.size());
	}
}
