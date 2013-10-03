package no.lemontree.hyperic.sonic;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.product.MeasurementPlugin;
import org.hyperic.hq.product.Metric;

public class Measurement extends MeasurementPlugin
{
	private static Log log = LogFactory.getLog(Measurement.class.getName());
	
    public Properties getCollectorProperties(Metric metric)
    {
    	log.trace("getCollectorProperties (" + metric.getObjectPropString() + ")");
        return super.getCollectorProperties(metric);
    }
}
