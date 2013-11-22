package no.lemontree.sonic;

import com.sonicsw.mf.common.runtime.IComponentState;
import com.sonicsw.mf.mgmtapi.runtime.IDirectoryServiceProxy;

public class DomainCollector extends Collector
{
	public DomainCollector(DomainProxyClient proxyClient)
	{
		IDirectoryServiceProxy proxy = proxyClient.getProxy();

        setMetric(Metrics.Domain.UpTime, proxy.getUptime());
        setAvailability(proxy.getState() == IComponentState.STATE_ONLINE);
	}
}
