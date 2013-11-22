package no.lemontree.sonic;

import com.sonicsw.mf.common.metrics.IMetric;
import com.sonicsw.mf.common.metrics.IMetricIdentity;
import com.sonicsw.mf.common.runtime.IComponentState;
import com.sonicsw.mf.mgmtapi.runtime.IAgentProxy;

public class ContainerCollector extends Collector
{
    private static final IMetricIdentity[] metrics = new IMetricIdentity[]
        {
    		IAgentProxy.SYSTEM_MEMORY_MAXUSAGE_METRIC_ID,
    		IAgentProxy.SYSTEM_MEMORY_CURRENTUSAGE_METRIC_ID,
    		IAgentProxy.SYSTEM_THREADS_CURRENTTOTAL_METRIC_ID,
    	};
    
	public ContainerCollector(ContainerProxyClient proxyClient)
	{
		IAgentProxy proxy = proxyClient.getProxy();
        
        proxy.enableMetrics(metrics);
        
        IMetric[] data = proxy.getMetricsData(metrics, false).getMetrics();
        for(IMetric m : data)
        	setMetric(m.getMetricIdentity().getAbsoluteName(), m.getValue());

		setAvailability(proxy.getState() == IComponentState.STATE_ONLINE);
		setMetric(Metrics.Container.UpTime, proxy.getUptime());
	}
}
