

import static java.lang.System.*;

import java.util.Enumeration;

import progress.message.jclient.Message;
import javax.jms.MessageConsumer;

import progress.message.jclient.*;

/**
 * Experiment for counting dead messages
 */
public class DeadMessages
{
	public static void main(String[] args) throws Exception
	{
		ConnectionFactory cf = (new progress.message.jclient.ConnectionFactory("tcp://nereus.dax.net:2800"));
        Connection connection = null;
        try
        {
	        connection = (Connection) cf.createConnection("Administrator", "Administrator");
	        Session session = (Session) connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
	        Queue dmq = (Queue) session.createQueue("SonicMQ.deadMessage");
	        QueueBrowser browser = (QueueBrowser) session.createBrowser(dmq);
	        
	        int n = 0;
	        int size = 0;
	        Enumeration e = browser.getEnumeration();
	        while(e.hasMoreElements())
	        {
	        	Message m = (Message) e.nextElement();
	        	n++;
	        	size += m.getBodySize();
	        }
	        
	        out.println("n        ="+n);
	        out.println("body size="+size);
        }
        finally
        {
        	if(connection != null)
        		try{connection.close();}
        		catch(Throwable t) {out.println("Failed to close connection: "+t.getMessage());}
        }
	}

}
