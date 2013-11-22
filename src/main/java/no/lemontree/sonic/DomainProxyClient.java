package no.lemontree.sonic;

import java.util.Properties;

import javax.management.ObjectName;

import no.lemontree.sonic.config.DomainOptions;

import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mf.mgmtapi.runtime.IDirectoryServiceProxy;
import com.sonicsw.mf.mgmtapi.runtime.MFProxyFactory;

/**
 * @see <a href="http://documentation.progress.com/output/Sonic/8.5.0/Docs8.5/api/mgmt_api/com/sonicsw/mf/mgmtapi/runtime/IDirectoryServiceProxy.html">IDirectoryServiceProxy API</a>
 */
public class DomainProxyClient extends ProxyClient<IDirectoryServiceProxy, DomainOptions>
{
	public DomainProxyClient(Properties config)
	{
		this(new DomainOptions(config));
	}
	public DomainProxyClient(DomainOptions config)
	{
		super(config);
	}

	@Override
	protected final IDirectoryServiceProxy getProxy(JMSConnectorClient client, ObjectName jmxName)
	{
		return MFProxyFactory.createDirectoryServiceProxy(client, jmxName);
	}
}