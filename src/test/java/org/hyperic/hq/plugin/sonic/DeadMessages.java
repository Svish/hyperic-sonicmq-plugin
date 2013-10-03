package org.hyperic.hq.plugin.sonic;

import static java.lang.System.*;

import java.util.Enumeration;

import javax.jms.Message;
import javax.jms.MessageConsumer;

import progress.message.jclient.*;

public class DeadMessages
{
	public static void main(String[] args) throws Exception
	{
		out.print("Creating factory... ");
		ConnectionFactory cf = (new progress.message.jclient.ConnectionFactory("tcp://esb-test1.dax.net:2800"));
        out.println("OK");
        
        Connection connection = null;
        try
        {
	        out.print("Creating connection... ");
	        connection = (Connection) cf.createConnection("Administrator", "Administrator");
	        //connection.start();
	        out.println("OK");
	        
	        out.print("Creating session... ");
	        Session session = (Session) connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
	        Queue dmq = (Queue) session.createQueue("SonicMQ.deadMessage");
	        out.println("OK");
	        	        
	        out.print("Creating browser... ");
	        QueueBrowser browser = (QueueBrowser) session.createBrowser(dmq);
	        out.println("OK");


	        out.print("Counting messages");
	        int n = 0;
	        Enumeration e = browser.getEnumeration();
	        while(e.hasMoreElements())
	        {
	        	e.nextElement();
	        	n++;
	        	out.print(".");
	        }
	        
	        out.println(" "+n);
        }
        finally
        {
        	if(connection != null)
        		try{connection.close();}
        		catch(Throwable t) {out.println("Failed to close connection: "+t.getMessage());}
        }
	}

}
