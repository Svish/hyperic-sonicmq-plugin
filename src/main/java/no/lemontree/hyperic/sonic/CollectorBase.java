package no.lemontree.hyperic.sonic;

import java.util.Hashtable;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.product.Collector;

import com.sonicsw.mf.jmx.client.JMSConnectorAddress;
import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mf.mgmtapi.runtime.ProxyRuntimeException;

/**
 * Base class for collectors.
 * 
 * @param <TProxy> Type of proxy used by collector
 */
public abstract class CollectorBase<TProxy> extends Collector
{
	private static Log log = LogFactory.getLog(CollectorBase.class.getName());

	
	protected abstract void collect(TProxy proxy, Configuration config);
	protected abstract TProxy createProxy(JMSConnectorClient client, ObjectName jmxName);
	
	
	@Override
	public final void collect()
	{
		// Ignore collect invokations without properties
		if(getProperties().keySet().size() == 0)
		{
			// Not sure why this happens sometimes...
			log.info("collect invoked without properties. Ignoring...");
			return;
		}
		
		log.debug("collect invoked: "+getProperties());

		Configuration config = new Configuration(getProperties());  
        JMSConnectorClient client = new JMSConnectorClient();
        try
		{
        	Hashtable<String, String> env = new Hashtable<String, String>();
            env.put("ConnectionURLs", config.location);
            env.put("DefaultUser", config.username);
            env.put("DefaultPassword", config.password);
	        client.connect(new JMSConnectorAddress(env), getTimeoutMillis());
	        
	        TProxy proxy = getProxy(client, config.id);
	        
			collect(proxy, config);
		}
		catch(ProxyRuntimeException e)
		{
			// Happens when whatever we're poking at is down
			log.debug("Failed to get metrics for "+config.id+" @ "+config.location+": "+e.getMessage());
			setAvailability(false);
		}
        finally
        {
        	client.disconnect();
        }
	}
	
	protected final TProxy getProxy(JMSConnectorClient client, String id)
	{
		try
		{
			ObjectName jmxName = new ObjectName(id);
			return createProxy(client, jmxName);
		}
		catch (MalformedObjectNameException e)
		{
			// Shouldn't happen...
			log.error("Failed to create proxy: "+e.getMessage());
			throw new RuntimeException(e);
		}
	}

	

}
