package no.lemontree.sonic;

import java.util.ArrayList;

import com.sonicsw.mf.common.metrics.IMetric;
import com.sonicsw.mf.common.metrics.IMetricIdentity;
import com.sonicsw.mf.common.runtime.IComponentState;
import com.sonicsw.mq.common.runtime.IQueueData;
import com.sonicsw.mq.mgmtapi.runtime.IBrokerProxy;

public class BrokerCollector extends Collector
{
	public static final double IsPrimary = 1;
	public static final double IsBackup = 0;
	
    private static final IMetricIdentity[] metrics = new IMetricIdentity[]
        {
			IBrokerProxy.BROKER_BYTES_DELIVEREDPERSECOND_METRIC_ID,
			IBrokerProxy.BROKER_BYTES_RECEIVEDPERSECOND_METRIC_ID,
			IBrokerProxy.BROKER_BYTES_TOPICDBSIZE_METRIC_ID,
			IBrokerProxy.BROKER_CONNECTIONS_COUNT_METRIC_ID,
			IBrokerProxy.BROKER_CONNECTIONS_REJECTEDPERMINUTE_METRIC_ID,
			IBrokerProxy.BROKER_MESSAGES_DELIVERED_METRIC_ID,
			IBrokerProxy.BROKER_MESSAGES_RECEIVED_METRIC_ID,
    	};
    
	public BrokerCollector(BrokerProxyClient proxyClient)
	{
		IBrokerProxy proxy = proxyClient.getProxy();
		
        proxy.enableMetrics(metrics);
        
        IMetric[] data = proxy.getMetricsData(metrics, false).getMetrics();
        for(IMetric m : data)
        	setMetric(m.getMetricIdentity().getAbsoluteName(), m.getValue());
        
        if("PRIMARY".equals(proxy.getReplicationType()))
        {
            ArrayList deadMessageQueue = proxy.getQueues("SonicMQ.deadMessage");
            if(deadMessageQueue.size() == 1)
            {
            	IQueueData q = (IQueueData) deadMessageQueue.get(0);
            	setMetric(Metrics.Broker.DmqCount, q.getMessageCount());
            	setMetric(Metrics.Broker.DmqSize, q.getTotalMessageSize());
            }	
        }
	    
	    setMetric(Metrics.Broker.IsPrimary, "PRIMARY".equals(proxy.getReplicationType()) ? IsPrimary : IsBackup);
	    setMetric(Metrics.Broker.ReplicationState, proxy.getReplicationState());
	    setMetric(Metrics.Broker.UpTime, proxy.getUptime());
        setAvailability(proxy.getState() == IComponentState.STATE_ONLINE);
	}
}
