package no.lemontree.sonic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import no.lemontree.Properties;
import no.lemontree.sonic.config.Options;

import org.junit.Test;


public class ITContainerCollector
{
	@Test
	public void ContainerIsUp_AvailableAndMetricsCollected()
	{
		Properties config = new Properties();
		config.setProperty(Options.Container.Id, config.getProperty("container.id"));
		ContainerProxyClient proxyClient = new ContainerProxyClient(config);
		
		ContainerCollector dc = new ContainerCollector(proxyClient);
		Map<String, Double> metrics = dc.getMetrics();
		
		assertTrue("Container should be available", dc.getAvailability());
		assertThat(metrics, hasKey(Metrics.Container.UpTime));
		assertThat(metrics, hasKey(Metrics.Container.MaxMemoryUsage));
		assertThat(metrics, hasKey(Metrics.Container.CurrentMemoryUsage));
		assertThat(metrics, hasKey(Metrics.Container.CurrentThreadUsage));
	}
}
