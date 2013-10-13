package no.lemontree.hyperic.sonic.detect;

import java.util.ArrayList;
import java.util.Collection;

import no.lemontree.hyperic.sonic.Configuration;

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
	
	private final Configuration config;
	private final GenericPlugin plugin;
	

	public DomainScanner(GenericPlugin plugin, Configuration config)
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
							
							// Look for dead message queue
							try
							{
								QueueResource q = new QueueResource(ref, componentName);
								services.add(q);
								logDiscovery(q);
							}
							catch(UnsupportedOperationException e)
							{
								log.warn(e.getMessage());
							}
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
        	c.setValue("id", config.domain+"."+name+":ID=AGENT");

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
        	c.setValue("id", config.domain+"."+container.getContainerName()+":ID="+name);

    		setType(plugin, "Broker");
			setName(config.domain + " " + name);
			setProductConfig(c);
			setMeasurementConfig();
			setControlConfig();
		}
	}
	
	private class QueueResource extends MyResource
	{
		public QueueResource(IMgmtBeanBase bean, String brokerName) throws MgmtException
		{
			//NOTE: Yes, this is annoying, but broker and backup broker doesn't share an interface... 
			IAcceptorTcpsBean tcpAcceptor = bean instanceof IBrokerBean
				? getDefaultAcceptor((IBrokerBean)bean)
				: getDefaultAcceptor((IBackupBrokerBean)bean);

			String name = "SonicMQ.deadMessage";
				
			if( ! getQueues(bean).getQueues().getKeyNames().contains(name))
				throw new UnsupportedOperationException("Could not find expected queue named '"+name+"' in broker '"+brokerName+"'.");
			
        	ConfigResponse c = new ConfigResponse();
        	c.setValue("broker.url", tcpAcceptor.getAcceptorUrl());
        	c.setValue("broker.username", config.username);
        	c.setValue("broker.password", config.password);
        	c.setValue("broker.name", brokerName);
        	c.setValue("queue", name);

    		setType(plugin, "Queue");    			
			setName(config.domain + " " + brokerName + " " + name);
			setProductConfig(c);
			setMeasurementConfig();
			setControlConfig();
		}

		
		private IQueuesBean getQueues(IMgmtBeanBase broker) throws MgmtException
		{
			return broker instanceof IBrokerBean
				? ((IBrokerBean) broker).getQueuesBean()
				: ((IBackupBrokerBean) broker).getPrimaryBrokerBean().getQueuesBean();
		}

		private IAcceptorTcpsBean getDefaultAcceptor(IBackupBrokerBean broker) throws MgmtException
		{
			return getDefaultAcceptor(broker.getAcceptorsBean());
		}

		private IAcceptorTcpsBean getDefaultAcceptor(IBrokerBean broker) throws MgmtException
		{
			return getDefaultAcceptor(broker.getAcceptorsBean());
		}
		
		private IAcceptorTcpsBean getDefaultAcceptor(IAcceptorsBean acceptors) throws MgmtException
		{
			IMgmtBeanBase acceptor = acceptors
				.getDefaultAcceptors()
				.getPrimaryAcceptorRef();
			
			if(acceptor instanceof IAcceptorTcpsBean)
				return (IAcceptorTcpsBean) acceptor;
			
			throw new UnsupportedOperationException("Default broker acceptor is expected be of type TCP.");
		}
	}

	private void logDiscovery(ServiceResource resource)
	{	
		log.info(String.format("Discovered %s: %s", 
			resource.getType(),
			resource.getName()));
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
