package no.lemontree.sonic;

import java.util.Hashtable;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import no.lemontree.sonic.config.DomainOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sonicsw.mf.jmx.client.JMSConnectorAddress;
import com.sonicsw.mf.jmx.client.JMSConnectorClient;

/**
 * Takes care of necessary JMS stuff required to create a sonic management proxy.
 *
 * @see <a href="http://documentation.progress.com/output/Sonic/8.5.0/Docs8.5/api/mgmt_api/">Sonic Management Application API</a>
 */
public abstract class ProxyClient<TProxy, TConfig extends DomainOptions>
{
	public static final int DEFAULT_TIMEOUT = 5000;
	private static final Log log = LogFactory.getLog(ProxyClient.class.getName());
		
	private final TConfig config;
	private JMSConnectorClient client;
	private TProxy proxy;
	
	/**
	 * Creates a new {@link ProxyClient}.
	 */
	public ProxyClient(TConfig config)
	{
		this.config = config;
	}
	
	/**
	 * Creates and connects the {@link JMSConnectorClient}.
	 * 
	 * @param timeout Timeout in milliseconds.
	 */
	public void connect(int timeout)
	{  
		if(client != null)
			return;
		
		log.debug("Connecting to '"+config.location+"'...");
		
        client = new JMSConnectorClient();
    	Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("ConnectionURLs", config.location);
        env.put("DefaultUser", config.username);
        env.put("DefaultPassword", config.password);
        client.connect(new JMSConnectorAddress(env), timeout);
        
        log.debug("Connected to '"+config.location+"'");
	}
	
	/**
	 * Disconnects the {@link JMSConnectorClient}.
	 */
	public void disconnect()
	{
		if(client == null)
			return;
		
		log.debug("Disconnecting from '"+config.location+"'...");
		
		client.disconnect();
		client = null;
		proxy = null;
		
		log.debug("Disconnected from '"+config.location+"'");
	}
	
	@Override
	protected void finalize() throws Throwable 
	{
		if(client != null)
		{
			log.warn("Someone forgot to disconnect...");
			disconnect();
		}
	}
	
	public TConfig getConfig()
	{
		return config;
	}
	
	/**
	 * Returns the {@link TProxy}, or a new one if it hasn't been created for this client already.
	 * Also {@link #connect(int)}s with {@link #DEFAULT_TIMEOUT} if that hasn't been done yet.
	 * 
	 * @return The {@link TProxy} for this client.
	 */
	public TProxy getProxy()
	{
		if(client == null)
			connect(DEFAULT_TIMEOUT);
		
		if(proxy == null)
			try
			{
				log.debug("Creating proxy for id '"+ config.id+"'...");
				ObjectName jmxName = new ObjectName(config.id);
				proxy = getProxy(client, jmxName);
				log.debug("Created proxy for id '"+config.id+"'");
			}
			catch (MalformedObjectNameException e)
			{
				log.error("Failed to create proxy for id '"+config.id+"': "+e.getMessage());
				throw new RuntimeException(e);
			}
		
		return proxy;
	}
	
	protected abstract TProxy getProxy(JMSConnectorClient client, ObjectName jmxName);
	
}
