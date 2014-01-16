package no.lemontree.hyperic.sonic;

import java.util.ArrayList;
import java.util.Collection;


import no.lemontree.sonic.config.DomainOptions;
import no.lemontree.sonic.config.Options;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.product.GenericPlugin;
import org.hyperic.hq.product.ServiceResource;
import org.hyperic.util.config.ConfigResponse;

import com.sonicsw.ma.mgmtapi.config.IMgmtBeanBase;
import com.sonicsw.ma.mgmtapi.config.MgmtException;
import com.sonicsw.mf.mgmtapi.config.IContainerBean;
import com.sonicsw.mf.mgmtapi.config.IContainerBean.IComponentsType;
import com.sonicsw.mf.mgmtapi.config.IContainerBean.IStartupParams;
import com.sonicsw.mq.mgmtapi.config.*;

public class DomainScanner
{
	private static Log log = LogFactory.getLog(DomainScanner.class.getName());
	
	private final DomainOptions config;
	private final GenericPlugin plugin;
	

	public DomainScanner(GenericPlugin plugin, DomainOptions config)
	{
		this.plugin = plugin;
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ServiceResource> findServices() throws MgmtException
	{
		ArrayList<ServiceResource> services = new ArrayList<ServiceResource>();
		
		MQMgmtBeanFactory factory = new MQMgmtBeanFactory();
		try
		{
			factory.connect(config.domain, config.location, config.username, config.password);
			
			// Find containers
			ArrayList<IContainerBean> containers = new ArrayList<IContainerBean>();
			for(String containerName : (Collection<String>) factory.getContainerBeanNames())
			{
				IContainerBean container = factory.getContainerBean(containerName);
				containers.add(container);
				
				ContainerResource r = new ContainerResource(container);
				services.add(r);
				logDiscovery(r);
			}
			
			// Find brokers
			for(IContainerBean container : containers)
			{
				IComponentsType components = container.getComponents();
				for(String componentName : (Collection<String>) components.getKeyNames())
				{
					IStartupParams entry = components.getEntry(componentName);
					try
					{
						// If broker
						IMgmtBeanBase ref = entry.getConfigRef();
						if(ref instanceof IBrokerBean
						|| ref instanceof IBackupBrokerBean)
						{
							BrokerResource b = new BrokerResource(componentName, container);
							services.add(b);
							logDiscovery(b);
						}
					}
					catch(MgmtException e)
					{
						continue;
					}
				}
			}

		}
		finally
		{
			try {factory.disconnect();}
			catch(Exception e) {}
		}

		
		return services;
	}
	
	private class ContainerResource extends MyResource
	{
		public ContainerResource(IContainerBean container) throws MgmtException
		{
			String name = container.getContainerName();
			
			ConfigResponse c = new ConfigResponse();
        	c.setValue(Options.Container.Id, config.domain+"."+name+":ID=AGENT");

    		setType(plugin, "Container");
			setName(config.domain + " " + name);
			setProductConfig(c);
			setMeasurementConfig();
			setControlConfig();
		}
	}
	
	private class BrokerResource extends MyResource
	{
		public BrokerResource(String name, IContainerBean container) throws MgmtException
		{
        	ConfigResponse c = new ConfigResponse();
        	c.setValue(Options.Broker.Id, config.domain+"."+container.getContainerName()+":ID="+name);

    		setType(plugin, "Broker");
			setName(config.domain + " " + name);
			setProductConfig(c);
			setMeasurementConfig();
			setControlConfig();
		}
	}

	private void logDiscovery(ServiceResource resource)
	{
		log.info(String.format("Discovered %s: %s\n\t%s", 
			resource.getType(),
			resource.getName(),
			resource.getProductConfig()));
	}
	
	private static class MyResource extends ServiceResource
	{
		@Override
		public void setType(GenericPlugin plugin, String type)
		{
			if(plugin != null)
				super.setType(plugin, type);
			// For unit tests
			else
				setType(type);
		}
	}
}
