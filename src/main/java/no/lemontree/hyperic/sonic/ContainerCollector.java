package no.lemontree.hyperic.sonic;

import no.lemontree.sonic.ContainerProxyClient;

/**
 * Hyperic collector for container metrics.
 */
public class ContainerCollector extends CollectorBase<no.lemontree.sonic.ContainerCollector>
{
	@Override
	protected no.lemontree.sonic.ContainerCollector createCollector()
	{
		ContainerProxyClient client = new ContainerProxyClient(getProperties());
		client.connect(getTimeoutMillis());
		
		try
		{
			return new no.lemontree.sonic.ContainerCollector(client);	
		}
		finally
		{
			client.disconnect();
		}
	}
}
