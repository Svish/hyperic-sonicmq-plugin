package no.lemontree.hyperic.sonic;

import no.lemontree.sonic.DomainProxyClient;

/**
 * Hyperic collector for domain/directory service.
 */
public class DomainCollector extends CollectorBase<no.lemontree.sonic.DomainCollector>
{
	@Override
	protected no.lemontree.sonic.DomainCollector createCollector()
	{
		DomainProxyClient client = new DomainProxyClient(getProperties());
		client.connect(getTimeoutMillis());
		try
		{
			return new no.lemontree.sonic.DomainCollector(client);	
		}
		finally
		{
			client.disconnect();
		}
	}
}
