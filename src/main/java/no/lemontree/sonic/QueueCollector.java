package no.lemontree.sonic;

import java.util.Enumeration;

import no.lemontree.sonic.config.QueueOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import progress.message.jclient.Connection;
import progress.message.jclient.ConnectionFactory;
import progress.message.jclient.Queue;
import progress.message.jclient.QueueBrowser;
import progress.message.jclient.Session;

import com.sonicsw.mq.mgmtapi.runtime.IBrokerProxy;

/**
 * Base class for classes which collects metrics for the Hyperic plugin.
 * 
 * Created to disconnects actual collection from Hyperic dependencies and enable testing.
 */
public class QueueCollector extends Collector
{
	private static Log log = LogFactory.getLog(QueueCollector.class.getName());
	
	public QueueCollector(QueueProxyClient client)
	{
		QueueOptions config = (QueueOptions) client.getConfig();
		
		
		IBrokerProxy broker = client.getProxy();

		// NOTE: Unable to connect to backup brokers, so we just consider it up
		if("BACKUP".equals(broker.getReplicationType()))
		{
			setMetric(Metrics.Queue.MessageCount, -1);
			setAvailability(true);
			return;
		}
		
		// NOTE: Unable to get count of dead messages through metrics, 
		// so have to connect to broker and count manually
		
		Connection connection = null;
        try
        {
        	// Connection
        	log.debug("Connecting to queue '"+config.queue+"' at '"+config.url+"'...");
        	ConnectionFactory cf = new ConnectionFactory(config.url);
	        connection = (Connection) cf.createConnection(config.username, config.password);
	        
	        // Session
	        Session session = (Session) connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
	        
	        // QueueBrowser
	        Queue queue = (Queue) session.createQueue(config.queue);
	        QueueBrowser browser = (QueueBrowser) session.createBrowser(queue);
	        browser.setPrefetchCount(50);
		    browser.setPrefetchThreshold(25);
		    
		    // Count...
        	log.debug("Counting messages...");
        	int count = 0;
	        Enumeration e = browser.getEnumeration();
	        while(e.hasMoreElements())
	        {
	        	e.nextElement();
	        	count++;
	        }
        	log.debug("Counted "+count+" messages...");

	        setMetric(Metrics.Queue.MessageCount, count);
	        setAvailability(true);
        }
		catch (Exception e)
		{
			throw new RuntimeException("Unable to count messages in queue '"+config.queue+"' at '"+config.url+"'", e);
		}
        finally
        {
        	if(connection != null)
        		try{connection.close();}
        		catch(Throwable t){}
        }
	}
}
