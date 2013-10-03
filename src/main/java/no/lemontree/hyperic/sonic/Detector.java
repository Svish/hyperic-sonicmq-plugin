package no.lemontree.hyperic.sonic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.product.PluginException;
import org.hyperic.hq.product.ServerDetector;
import org.hyperic.hq.product.ServiceResource;
import org.hyperic.util.config.ConfigResponse;

import com.sonicsw.ma.mgmtapi.config.IMgmtBeanBase;
import com.sonicsw.ma.mgmtapi.config.MgmtException;
import com.sonicsw.mf.mgmtapi.config.IContainerBean;
import com.sonicsw.mf.mgmtapi.config.IContainerBean.IComponentsType;
import com.sonicsw.mf.mgmtapi.config.IContainerBean.IStartupParams;
import com.sonicsw.mq.mgmtapi.config.IBackupBrokerBean;
import com.sonicsw.mq.mgmtapi.config.IBrokerBean;
import com.sonicsw.mq.mgmtapi.config.MQMgmtBeanFactory;

/**
 * Finds objects in a Sonic domain.
 * 
 * @see http://pubs.vmware.com/vfabric52/index.jsp#com.vmware.vfabric.hyperic.4.6/Dynamic_Service_Type_Detection.html
 */
public class Detector extends ServerDetector
{
	private static Log log = LogFactory.getLog(Detector.class.getName());
	
	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<ServiceResource> discoverServices(ConfigResponse config) throws PluginException
	{
		log.info("discoverServices invoked: "+config);
		
		ArrayList<ServiceResource> services = new ArrayList<ServiceResource>();
		Configuration sonic = new Configuration(config);
        MQMgmtBeanFactory factory = new MQMgmtBeanFactory();
        
        try
		{
			factory.connect(sonic.domain, sonic.location, sonic.username, sonic.password);
            
			// Get containers
            for(String containerName : (Collection<String>)factory.getContainerBeanNames())
            {
            	IContainerBean container = factory.getContainerBean(containerName);
            	containerName = container.getContainerName();
            	
            	// Create resource
            	{
                	log.info("Discovered container: "+containerName);
                	
                	ConfigResponse c = new ConfigResponse();
                	c.setValue("id", sonic.domain+"."+containerName+":ID=AGENT");
                	
					services.add(createContainer(sonic.domain + " " + containerName, c));	
            	}
            	
				// Get components in container
            	IComponentsType components = container.getComponents();
				for(String componentName : (List<String>)components.getKeyNames())
				{
					IStartupParams entry = components.getEntry(componentName);
					try
					{
						// If broker
						IMgmtBeanBase ref = entry.getConfigRef();
						if(ref instanceof IBrokerBean
						|| ref instanceof IBackupBrokerBean)
						{
							// Create resource
			            	log.info("Discovered broker: "+componentName);
			            	
			            	ConfigResponse c = new ConfigResponse();
		                	c.setValue("id", sonic.domain+"."+containerName+":ID="+componentName);
			            	
							services.add(createBroker(sonic.domain + " " + containerName + " " + componentName, c));
						}
					}
					catch(MgmtException e)
					{
						continue;
					}
				}
            }
		}
		catch (MgmtException e)
		{
			throw new PluginException(e.getMessage(), e);
		}
		
		return services;
	}
	
	

	private ServiceResource createContainer(String name, ConfigResponse config)
	{
		return createService("Container", name, config);
	}

	private ServiceResource createBroker(String name, ConfigResponse config)
	{
		return createService("Broker", name, config);
	}

	private ServiceResource createService(String type, String name, ConfigResponse config)
	{
		ServiceResource s = new ServiceResource();
		s.setType(this, type);
		s.setName(name);
		s.setProductConfig(config);
		s.setMeasurementConfig();
		s.setControlConfig();
		return s;
	}
}