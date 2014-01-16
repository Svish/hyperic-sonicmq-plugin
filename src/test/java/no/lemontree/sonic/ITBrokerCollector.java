package no.lemontree.sonic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import no.lemontree.Properties;
import no.lemontree.sonic.config.Options;

import org.junit.Test;

public class ITBrokerCollector
{
	@Test
	public void PrimaryBroker_AvailableAndMetricsCollected()
	{
		Properties config = new Properties();
		config.setProperty(Options.Broker.Id, config.getProperty("primaryBroker.id"));
		BrokerProxyClient proxyClient = new BrokerProxyClient(config);
		
		BrokerCollector dc = new BrokerCollector(proxyClient);
		Map<String, Double> metrics = dc.getMetrics();
		
		assertTrue("Domain should be available", dc.getAvailability());
		assertThat(metrics, hasKey(Metrics.Broker.UpTime));
		assertThat(metrics, hasEntry(Metrics.Broker.IsPrimary, BrokerCollector.IsPrimary));
		assertThat(metrics, hasKey(Metrics.Broker.DmqCount));
		assertThat(metrics, hasKey(Metrics.Broker.DmqSize));
		assertThat(metrics, hasKey(Metrics.Broker.BytesDeliveredPerSecond));
		assertThat(metrics, hasKey(Metrics.Broker.BytesReceivedPerSecond));
		assertThat(metrics, hasKey(Metrics.Broker.MessagesReceived));
		assertThat(metrics, hasKey(Metrics.Broker.MessagesDelivered));
		assertThat(metrics, hasKey(Metrics.Broker.ConnectionCount));
	}
	
	@Test
	public void BackupBroker_AvailableAndMetricsCollected()
	{
		Properties config = new Properties();
		config.setProperty(Options.Broker.Id, config.getProperty("backupBroker.id"));
		BrokerProxyClient proxyClient = new BrokerProxyClient(config);
		
		BrokerCollector dc = new BrokerCollector(proxyClient);
		Map<String, Double> metrics = dc.getMetrics();
		
		assertTrue("Domain should be available", dc.getAvailability());
		assertThat(metrics, hasKey(Metrics.Broker.UpTime));
		assertThat(metrics, hasEntry(Metrics.Broker.IsPrimary, BrokerCollector.IsBackup));
		assertThat(metrics, hasKey(Metrics.Broker.ReplicationState));
		assertThat(metrics, hasKey(Metrics.Broker.BytesDeliveredPerSecond));
		assertThat(metrics, hasKey(Metrics.Broker.BytesReceivedPerSecond));
		assertThat(metrics, hasKey(Metrics.Broker.MessagesReceived));
		assertThat(metrics, hasKey(Metrics.Broker.MessagesDelivered));
		assertThat(metrics, hasKey(Metrics.Broker.ConnectionCount));
	}
}
