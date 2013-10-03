package org.hyperic.hq.plugin.sonic;
import java.text.SimpleDateFormat;
import java.util.*;

import com.sonicsw.esb.mgmtapi.ESBAPI;
import com.sonicsw.esb.mgmtapi.ESBAPIFactory;
import com.sonicsw.esb.mgmtapi.config.IMonitoredContainerConfigAPI;
import com.sonicsw.esb.mgmtapi.runtime.IMonitoredContainerRuntimeAPI;
import com.sonicsw.ma.mgmtapi.config.IMgmtBeanBase;
import com.sonicsw.ma.mgmtapi.config.MgmtException;
import com.sonicsw.mf.common.metrics.IMetric;
import com.sonicsw.mf.common.metrics.IMetricIdentity;
import com.sonicsw.mf.jmx.client.JMSConnectorAddress;
import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mf.mgmtapi.config.IContainerBean;
import com.sonicsw.mf.mgmtapi.config.IContainerBean.IComponentsType;
import com.sonicsw.mf.mgmtapi.config.IContainerBean.IStartupParams;
import com.sonicsw.mf.mgmtapi.runtime.IAgentProxy;
import com.sonicsw.mf.mgmtapi.runtime.IDirectoryServiceProxy;
import com.sonicsw.mq.common.runtime.IQueueData;
import com.sonicsw.mq.mgmtapi.config.*;
import com.sonicsw.mq.mgmtapi.config.IClusterBean.IClusterMembers;
import com.sonicsw.mq.mgmtapi.config.IQueuesBean.IQueueAttributes;
import com.sonicsw.mq.mgmtapi.config.IQueuesBean.IQueuesSetType;
import com.sonicsw.mq.mgmtapi.runtime.IBrokerProxy;


public final class ManualPoking
{
	private static final class MetricComparator implements Comparator<IMetric>
	{
		public int compare(IMetric a, IMetric b)
		{
			String aName = a.getMetricIdentity().getAbsoluteName();
			String bName = b.getMetricIdentity().getAbsoluteName();
			return aName.compareTo(bName);
		}
	}
	
	//public static final String urls = "tcp://mwtest.nwn.no:3000";
	//public static final String domain = "dmNetworkNorway";

	//public static final String urls = "tcp://miwa1.nwn.no:3000,miwa2.nwn.no:3010";
	//public static final String domain = "dmNetworkNorway";

	//public static final String urls = "tcp://esb-dev.dax.net:2610";
	//public static final String domain = "tele2-dev";
	
	public static final String urls = "tcp://esb-test1.dax.net:2610,tcp://esb-test2.dax.net:2610";
	public static final String domain = "tele2-test";
	
	//public static final String urls = "tcp://nereus.dax.net:2610,tcp://glaucus.dax.net:2610";
	//public static final String domain = "tele2-prod";
	
	//public static final String urls = "tcp://localhost:2506";
	//public static final String domain = "Domain1";
	
	public static final String user = "Administrator";
	public static final String pass = "Administrator";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception
	{
		// Connection
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("ConnectionURLs", urls);
        env.put("DefaultUser", user);
        env.put("DefaultPassword", pass);

        JMSConnectorAddress address = new JMSConnectorAddress(env);
        JMSConnectorClient client = new JMSConnectorClient();
        MQMgmtBeanFactory factory = new MQMgmtBeanFactory();
        
		try
		{	
			// Connect
	        client.connect(address, 2000);                        
            factory.connect(domain, urls, user, pass);
	        
	        // Directory Service
            if(1==1)
            {
	            IDirectoryServiceProxy ds = Common.getDirectoryServiceProxy(client, domain); 
	            
	            p("-- Domain (metrics) --");
	            p("Domain=", ds.getDomain());
	            p("State=", ds.getStateString());
	            p("UpTime=", msToTime(ds.getUptime()));
            }
            
            

            // Container?
            if(1==1)
            {
	            p("-- Container (metrics) --");
                IAgentProxy agent = Common.getAgentProxy(client, domain, "ctbrData1");
                p("State=", agent.getStateString());

                IMetricIdentity[] metrics = new IMetricIdentity[] {
                		IAgentProxy.SYSTEM_MEMORY_MAXUSAGE_METRIC_ID,
                		IAgentProxy.SYSTEM_MEMORY_CURRENTUSAGE_METRIC_ID,
                		IAgentProxy.SYSTEM_THREADS_CURRENTTOTAL_METRIC_ID,
                };
                
                agent.enableMetrics(metrics);

                IMetric[] data = agent.getMetricsData(metrics, false).getMetrics();
				Arrays.sort(data, new MetricComparator());
                for(IMetric m : data)
                	p(m.getMetricIdentity().getAbsoluteName(), "=", m.getValue());
            }
            
            
            // Broker
            if(1==1)
            {
	            p("-- Broker (metrics) --");
	            IBrokerProxy broker = Common.getBrokerProxy(client, domain, "ctbrData1", "brData1");
	            p("State=", broker.getStateString());
	            

                IMetricIdentity[] metrics = new IMetricIdentity[] {
                		IBrokerProxy.BROKER_BYTES_DELIVEREDPERSECOND_METRIC_ID,
                		IBrokerProxy.BROKER_BYTES_RECEIVEDPERSECOND_METRIC_ID,
                		IBrokerProxy.BROKER_BYTES_TOPICDBSIZE_METRIC_ID,
                		IBrokerProxy.BROKER_CONNECTIONS_COUNT_METRIC_ID,
                		IBrokerProxy.BROKER_CONNECTIONS_REJECTEDPERMINUTE_METRIC_ID,
                		IBrokerProxy.BROKER_MESSAGES_DELIVERED_METRIC_ID,
                		IBrokerProxy.BROKER_MESSAGES_RECEIVED_METRIC_ID,
                };
                
                broker.enableMetrics(metrics);
                
                IMetric[] data = broker.getMetricsData(metrics, false).getMetrics();
				Arrays.sort(data, new MetricComparator());
                for(IMetric m : data)
                	p(m.getMetricIdentity().getAbsoluteName(), "=", m.getValue());
	            
                ArrayList<IQueueData> queues = broker.getQueues("Q.");
                for(IQueueData q : queues)
                	p("\t", q.isClusteredQueue(), "\t", q.getMessageCount(), "\t", q.getTotalMessageSize(), "\t", q.getQueueName());
                
            }
            
            
            
            if(1==1)
            {
	            p("-- Clusters (mgmt) --");
	            for(String name : (Collection<String>)factory.getClusterBeanNames())
	            {
	            	IClusterBean cluster = factory.getClusterBean(name);
	            	p(cluster.getClusterName());
	            	
	            	// Cluster queues
	            	{
		            	IQueuesBean queuesBean = cluster.getQueuesBean();
		            	IQueuesSetType queues = queuesBean.getQueues();
		            	for(String n : (Collection<String>)queues.getKeyNames())
		            	{
		            		IQueueAttributes queue = queues.getQueue(n);
		            		p("\t", queue.getQueueMaxSize(),
		            		  "\t", queue.getQueueSaveThreshold(),
		            		  "\t", queue.getQueueName());
		            	}
	            	}
	            	
	            	// Cluster members
	            	IClusterMembers clusterMembers = cluster.getClusterMembers();
					for(String cm : (Collection<String>)clusterMembers.getKeyNames())
	            	{
	            		IMgmtBeanBase item = clusterMembers.getItem(cm);
	            		if(item instanceof IBrokerBean)
	            		{
	            			IBrokerBean broker = (IBrokerBean) item;
	            			p("\t", broker.getBrokerName());
	                    	
	                    	IQueuesBean queuesBean = broker.getQueuesBean();
	                    	IQueuesSetType queues = queuesBean.getQueues();
	                    	for(String qn : (Collection<String>)queues.getKeyNames())
	                    	{
	                    		IQueueAttributes queue = queues.getQueue(qn);
	                    		p("\t\t", queue.getQueueMaxSize(),
	                    		  "\t", queue.getQueueSaveThreshold(),
	                    		  "\t", queue.getQueueName());
	                    	}
	            		}
	            	}
	            	
	            }
            }
            
            if(1==1)
			{
                p("-- Containers (mgmt) --");
				for(String name : (Collection<String>)factory.getContainerBeanNames())
				{
					IContainerBean container = factory.getContainerBean(name);
					p(container.getContainerName());
					
					
					// Components
					IComponentsType components = container.getComponents();
					for(String cn : (List<String>)components.getKeyNames())
					{
						IStartupParams entry = components.getEntry(cn);
						try
						{
							IMgmtBeanBase configRef = entry.getConfigRef();
							if(configRef instanceof IBrokerBean
							|| configRef instanceof IBackupBrokerBean)
							{
								p("\tBroker\t", cn);
							}
							else
							{
								p("\tOther\t", cn);
							}
						}
						catch(MgmtException e)
						{
							p("\t?\t", cn);
						}
					}
					
				}
			}
            

            if(1==1)
			{
                p("-- Brokers (mgmt) --");
				for(String name : (Collection<String>)factory.getBrokerBeanNames())
				{
					IBrokerBean broker = factory.getBrokerBean(name);
					p(broker.getBrokerName(), " (cluster=", broker.getClusterBean().getClusterName(), ")");
					
					IQueuesBean queuesBean = broker.getQueuesBean();
					IQueuesSetType queues = queuesBean.getQueues();
					for(String n : (Collection<String>)queues.getKeyNames())
					{
						IQueueAttributes queue = queues.getQueue(n);
						p("\t", queue.getQueueMaxSize(),
						  "\t", queue.getQueueSaveThreshold(),
						  "\t", queue.getQueueName());
					}
				}
			}

            if(1==1)
            {
                p("-- Container Collections (mgmt) --");
	            for(String name : (Collection<String>)factory.getContainerCollectionBeanNames())
	            	p(name);
            }
            
		}
		finally
		{
			if(client != null)
				client.disconnect();
			if(factory != null)
				factory.disconnect();
		}
	}

	
	private static String msToTime(long ms)
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ms));
	}
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static void printContainerState()
	{
		ESBAPIFactory factory = ESBAPIFactory.createESBAPIFactory();
		
		ESBAPI api = factory.createAPI(domain, urls, user, pass);

		IMonitoredContainerRuntimeAPI containersRuntimeAPI = api.getMonitoredContainersRuntimeAPI();
		IMonitoredContainerConfigAPI containersConfigAPI = api.getMonitoredContainersConfigAPI();
		
		String[] containers = ((Set<String>)containersConfigAPI.getAllMFContainers()).toArray(new String[0]);
		Arrays.sort(containers);
		
		for(String c : containers)
		{
			String online = containersRuntimeAPI.isMFComponentOnline(c, c) ? "+" : " ";
			
			pf("%s\t%s", online, c);
		}
		
		api.dispose();
	}

	private static void p(Object... args)
	{
		for(Object o : args)
			System.out.print(o);
		System.out.println();
	}
	
	private static void pf(String format, Object... args)
	{
		System.out.println(String.format(format, args));
	}
}
