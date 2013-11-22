package no.lemontree.sonic;

import java.util.Properties;

import javax.management.ObjectName;

import no.lemontree.sonic.config.ComponentOptions;

import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mq.mgmtapi.runtime.IBrokerProxy;
import com.sonicsw.mq.mgmtapi.runtime.MQProxyFactory;

/**
 * @see <a href="http://documentation.progress.com/output/Sonic/8.5.0/Docs8.5/api/mgmt_api/com/sonicsw/mq/mgmtapi/runtime/IBrokerProxy.html">IBrokerProxy API</a>
 */
public class BrokerProxyClient extends ProxyClient<IBrokerProxy, ComponentOptions>
{
	public BrokerProxyClient(Properties config)
	{
		this(new ComponentOptions(config));
	}
	
	public BrokerProxyClient(ComponentOptions config)
	{
		super(config);
	}

	@Override
	protected final IBrokerProxy getProxy(JMSConnectorClient client, ObjectName jmxName)
	{
		return MQProxyFactory.createBrokerProxy(client, jmxName);
	}
}