package no.lemontree.hyperic.sonic;

import no.lemontree.sonic.BrokerProxyClient;

/**
 * Hyperic collector for broker metrics.
 */
public class BrokerCollector extends CollectorBase<no.lemontree.sonic.BrokerCollector>
{
	@Override
	protected no.lemontree.sonic.BrokerCollector createCollector()
	{
		BrokerProxyClient client = new BrokerProxyClient(getProperties());
		client.connect(getTimeoutMillis());
		
		try
		{
			return new no.lemontree.sonic.BrokerCollector(client);	
		}
		finally
		{
			client.disconnect();
		}
	}
}
