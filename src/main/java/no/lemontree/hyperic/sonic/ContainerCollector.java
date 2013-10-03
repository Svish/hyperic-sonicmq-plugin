package no.lemontree.hyperic.sonic;

import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sonicsw.mf.common.metrics.IMetric;
import com.sonicsw.mf.common.metrics.IMetricIdentity;
import com.sonicsw.mf.common.runtime.IComponentState;
import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mf.mgmtapi.runtime.IAgentProxy;
import com.sonicsw.mf.mgmtapi.runtime.MFProxyFactory;

/**
 * Collector for container metrics.
 */
public class ContainerCollector extends CollectorBase<IAgentProxy>
{
	private static Log log = LogFactory.getLog(ContainerCollector.class.getName());


	@Override
	protected IAgentProxy createProxy(JMSConnectorClient client, ObjectName jmxName)
	{
		return MFProxyFactory.createAgentProxy(client, jmxName);
	}

	
	@Override
	public void collect(IAgentProxy proxy, Configuration config)
	{
    	log.debug("collect invoked: "+getProperties());
    	
        setAvailability(proxy.getState() == IComponentState.STATE_ONLINE);
        setValue("UpTime", proxy.getUptime());
        
        IMetricIdentity[] metrics = new IMetricIdentity[] {
        		IAgentProxy.SYSTEM_MEMORY_MAXUSAGE_METRIC_ID,
        		IAgentProxy.SYSTEM_MEMORY_CURRENTUSAGE_METRIC_ID,
        		IAgentProxy.SYSTEM_THREADS_CURRENTTOTAL_METRIC_ID,
        };
        
        proxy.enableMetrics(metrics);
        
        IMetric[] data = proxy.getMetricsData(metrics, false).getMetrics();
        for(IMetric m : data)
        	setValue(m.getMetricIdentity().getAbsoluteName(), m.getValue());
	}	
}
