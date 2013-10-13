package no.lemontree.hyperic.sonic;

import static java.lang.System.*;

import java.util.Enumeration;
import java.util.Properties;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.product.Collector;

import progress.message.jclient.*;

/**
 * Collector for queue metrics.
 */
public class QueueCollector extends Collector
{
	private static Log log = LogFactory.getLog(QueueCollector.class.getName());

	
	@Override
	public final void collect()
	{
    	log.debug("collect invoked: "+getProperties());

    	setAvailability(true);
    	
		QueueConfiguration config = new QueueConfiguration(getProperties());

        Connection connection = null;
        try
        {
        	ConnectionFactory cf = new ConnectionFactory(config.url);
	        connection = (Connection) cf.createConnection(config.username, config.password);
	        Session session = (Session) connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
	        Queue dmq = (Queue) session.createQueue(config.queue);
	        QueueBrowser browser = (QueueBrowser) session.createBrowser(dmq);
	        
	        int messageCount = 0;
	        Enumeration e = browser.getEnumeration();
	        while(e.hasMoreElements())
	        {
	        	e.nextElement();
	        	messageCount++;
	        }
	        
	        setValue("queue.messages.Count", messageCount);
        }
		catch (JMSException e)
		{
			log.debug("Failed to get metrics for "+config.broker+":"+config.queue+" @ "+config.url+": "+e.getMessage());
			setAvailability(false);
		}
        finally
        {
        	if(connection != null)
        		try{connection.close();}
        		catch(Throwable t){}
        }
	}

	
	public static class QueueConfiguration
	{
		public final String url;
		public final String broker;
		public final String username;
		public final String password;
		public final String queue;
		public final Configuration ds;

		public QueueConfiguration(Properties properties)
		{
			ds = new Configuration(properties);
			url = properties.getProperty("broker.url");
			broker = properties.getProperty("broker.name");
			username = properties.getProperty("broker.username");
			password = properties.getProperty("broker.password");
			queue = properties.getProperty("queue");
		}
	}
	
	
}
