package no.lemontree.sonic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertTrue;
import no.lemontree.Properties;

import org.junit.Test;

public class ITDomainCollector
{
	@Test
	public void DomainIsUp_AvailableAndUpTime()
	{
		Properties config = new Properties();
		DomainProxyClient proxyClient = new DomainProxyClient(config);
		
		DomainCollector dc = new DomainCollector(proxyClient);
		
		assertTrue("Domain should be available", dc.getAvailability());
		assertThat(dc.getMetrics(), hasKey(Metrics.Domain.UpTime));
	}
}
