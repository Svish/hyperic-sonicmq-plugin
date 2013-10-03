package no.lemontree.hyperic.sonic;

import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sonicsw.mf.common.metrics.IMetric;
import com.sonicsw.mf.common.metrics.IMetricIdentity;
import com.sonicsw.mf.common.runtime.IComponentState;
import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mq.mgmtapi.runtime.IBrokerProxy;
import com.sonicsw.mq.mgmtapi.runtime.MQProxyFactory;

/**
 * Collector for broker metrics.
 */
public class BrokerCollector extends CollectorBase<IBrokerProxy>
{
	private static Log log = LogFactory.getLog(BrokerCollector.class.getName());
	
	
	@Override
	public void collect(IBrokerProxy proxy, Configuration config)
	{
    	log.debug("collect invoked: "+getProperties());

        setAvailability(proxy.getState() == IComponentState.STATE_ONLINE);
        setValue("UpTime", proxy.getUptime());
        
        IMetricIdentity[] metrics = new IMetricIdentity[] {
        		IBrokerProxy.BROKER_BYTES_DELIVEREDPERSECOND_METRIC_ID,
        		IBrokerProxy.BROKER_BYTES_RECEIVEDPERSECOND_METRIC_ID,
        		IBrokerProxy.BROKER_BYTES_TOPICDBSIZE_METRIC_ID,
        		IBrokerProxy.BROKER_CONNECTIONS_COUNT_METRIC_ID,
        		IBrokerProxy.BROKER_CONNECTIONS_REJECTEDPERMINUTE_METRIC_ID,
        		IBrokerProxy.BROKER_MESSAGES_DELIVERED_METRIC_ID,
        		IBrokerProxy.BROKER_MESSAGES_RECEIVED_METRIC_ID,
        };
        
        proxy.enableMetrics(metrics);
        
        IMetric[] data = proxy.getMetricsData(metrics, false).getMetrics();
        for(IMetric m : data)
        	setValue(m.getMetricIdentity().getAbsoluteName(), m.getValue());
	}


	@Override
	protected IBrokerProxy createProxy(JMSConnectorClient client, ObjectName jmxName)
	{
		return MQProxyFactory.createBrokerProxy(client, jmxName);
	}


}
