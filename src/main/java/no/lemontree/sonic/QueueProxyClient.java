package no.lemontree.sonic;

import java.util.Properties;

import javax.management.ObjectName;

import no.lemontree.sonic.config.QueueOptions;

import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mq.mgmtapi.runtime.IBrokerProxy;
import com.sonicsw.mq.mgmtapi.runtime.MQProxyFactory;

public class QueueProxyClient extends ProxyClient<IBrokerProxy, QueueOptions>
{
	public QueueProxyClient(Properties config)
	{
		this(new QueueOptions(config));
	}

	public QueueProxyClient(QueueOptions config)
	{
		super(config);
	}

	@Override
	protected final IBrokerProxy getProxy(JMSConnectorClient client, ObjectName jmxName)
	{
		return MQProxyFactory.createBrokerProxy(client, jmxName);
	}
}
