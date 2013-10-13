package no.lemontree.hyperic.sonic;

import java.util.ArrayList;

import no.lemontree.hyperic.sonic.detect.DomainScanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.product.PluginException;
import org.hyperic.hq.product.ServerDetector;
import org.hyperic.hq.product.ServiceResource;
import org.hyperic.util.config.ConfigResponse;

import com.sonicsw.ma.mgmtapi.config.MgmtException;

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
		
        try
		{
			DomainScanner ds = new DomainScanner(this, new Configuration(config));
			
			return ds.findServices();
		}
		catch (MgmtException e)
		{
			throw new PluginException(e.getMessage(), e);
		}
	}
}