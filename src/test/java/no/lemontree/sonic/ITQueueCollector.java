package no.lemontree.sonic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertTrue;
import no.lemontree.Properties;

import org.junit.Test;

/**
 * Integration test for {@link QueueMessageCounter} towards Test broker.
 */
public class ITQueueCollector
{
	@Test
	public void primaryBroker_CountsMessages() throws Exception
	{
		Properties config = new Properties();
		config.setProperty("id", config.getProperty("primaryBroker.id"));
		config.setProperty("broker.url", config.getProperty("primaryBroker.url"));
		
		QueueProxyClient proxyClient = new QueueProxyClient(config);
		QueueCollector dc = new QueueCollector(proxyClient);
		
		assertTrue("Domain should be available", dc.getAvailability());
		assertThat(dc.getMetrics(), hasKey(Metrics.Queue.MessageCount));

		double count = dc.getMetrics().get(Metrics.Queue.MessageCount);
		assertTrue("Message count should be zero or more, but was "+count, count >= 0);
	}
	
	@Test
	public void backupBroker_ReturnsNegativeOne() throws Exception
	{
		Properties config = new Properties();
		config.setProperty("id", config.getProperty("backupBroker.id"));
		config.setProperty("broker.url", config.getProperty("backupBroker.url"));
		
		QueueProxyClient proxyClient = new QueueProxyClient(config);		
		QueueCollector dc = new QueueCollector(proxyClient);
		
		assertTrue("Domain should be available", dc.getAvailability());
		assertThat(dc.getMetrics(), hasKey(Metrics.Queue.MessageCount));

		double count = dc.getMetrics().get(Metrics.Queue.MessageCount);
		assertTrue("Message count should be less than zero, but was "+count, count < 0);
	}
	
	@Test(expected=RuntimeException.class)
	public void noBroker_ThrowsException() throws Exception
	{
		Properties config = new Properties();
		config.setProperty("id", config.getProperty("noBroker.id"));
		config.setProperty("broker.url", config.getProperty("noBroker.url"));
		
		QueueProxyClient proxyClient = new QueueProxyClient(config);		
		new QueueCollector(proxyClient);
	}
}
