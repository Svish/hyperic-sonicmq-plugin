package no.lemontree.hyperic.sonic;

import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sonicsw.mf.common.runtime.IComponentState;
import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mf.mgmtapi.runtime.IDirectoryServiceProxy;
import com.sonicsw.mf.mgmtapi.runtime.MFProxyFactory;

/**
 * Collector for domain/directory service metrics.
 */
public class DomainCollector extends CollectorBase<IDirectoryServiceProxy>
{
	private static Log log = LogFactory.getLog(DomainCollector.class.getName());

	
	@Override
	protected void collect(IDirectoryServiceProxy proxy, Configuration config)
	{	
    	log.debug("collect invoked: "+getProperties());
        
        setAvailability(proxy.getState() == IComponentState.STATE_ONLINE);
        setValue("UpTime", proxy.getUptime());
	}
	

	@Override
	protected IDirectoryServiceProxy createProxy(JMSConnectorClient client, ObjectName jmxName)
	{
		return MFProxyFactory.createDirectoryServiceProxy(client, jmxName);
	}
	

}
