package no.lemontree.sonic;

import java.util.Properties;

import javax.management.ObjectName;

import no.lemontree.sonic.config.ComponentOptions;

import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mf.mgmtapi.runtime.IAgentProxy;
import com.sonicsw.mf.mgmtapi.runtime.MFProxyFactory;

/**
 * @see <a href="http://documentation.progress.com/output/Sonic/8.5.0/Docs8.5/api/mgmt_api/com/sonicsw/mf/mgmtapi/runtime/IAgentProxy.html">IAgentProxy API</a>
 */
public class ContainerProxyClient extends ProxyClient<IAgentProxy, ComponentOptions>
{	
	public ContainerProxyClient(Properties config)
	{
		this(new ComponentOptions(config));
	}
	
	public ContainerProxyClient(ComponentOptions config)
	{
		super(config);
	}

	@Override
	protected final IAgentProxy getProxy(JMSConnectorClient client, ObjectName jmxName)
	{
		return MFProxyFactory.createAgentProxy(client, jmxName);
	}
}