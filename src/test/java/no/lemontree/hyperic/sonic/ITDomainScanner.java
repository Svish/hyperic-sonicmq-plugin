package no.lemontree.hyperic.sonic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;

import no.lemontree.Properties;
import no.lemontree.hyperic.sonic.DomainScanner;
import no.lemontree.sonic.config.DomainOptions;

import org.hyperic.hq.product.ServiceResource;
import org.junit.Test;

/**
 * Simple {@link DomainScanner} test towards domain specified in /it.properties
 */
public class ITDomainScanner
{
	@Test
	public void findsServices() throws Exception
	{
		DomainOptions config = new DomainOptions(new Properties());
		
		ArrayList<ServiceResource> services = new DomainScanner(null, config)
			.findServices();
		
		HashSet<Class> types = new HashSet<Class>();
		for(ServiceResource s : services)
			types.add(s.getClass());
		
		// NOTE: No idea what's actually in the domain, but we should at least discover one of each service type
		assertEquals(3, types.size());
	}
}
