package no.lemontree.hyperic.sonic;

import no.lemontree.sonic.QueueProxyClient;

/**
 * Collector for queue metrics.
 */
public class QueueCollector extends CollectorBase<no.lemontree.sonic.QueueCollector>
{
	@Override
	protected no.lemontree.sonic.QueueCollector createCollector()
	{
		QueueProxyClient client = new QueueProxyClient(getProperties());
		client.connect(getTimeoutMillis());
		try
		{
			return new no.lemontree.sonic.QueueCollector(client);	
		}
		finally
		{
			client.disconnect();
		}
	}
}
