package no.lemontree.hyperic.sonic;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.hq.product.Collector;

/**
 * Base class for collectors.
 * 
 * @param <TProxy> Type of proxy used by collector
 */
public abstract class CollectorBase<TCollector extends no.lemontree.sonic.Collector> extends Collector
{
	private static Log log = LogFactory.getLog(CollectorBase.class.getName());
	
	protected abstract TCollector createCollector();
	
	@Override
	public final void collect()
	{
		// Ignore collect invokations without properties
		if(getProperties().keySet().size() == 0)
		{
			// Not sure why this happens sometimes...
			log.info("collect invoked without properties. Ignoring...");
			return;
		}
        try
		{
    		TCollector collector = createCollector();
    		
    		for(Map.Entry<String, Double> metric : collector.getMetrics().entrySet())
    			setValue(metric.getKey(), metric.getValue());
    		setAvailability(collector.getAvailability());
		}
		catch(Exception e)
		{
			setAvailability(false);
			
			log.debug(getClass().getSimpleName() + " failed to get metrics. " + getProperties());
			log.warn(e,e);
		}
	}
}
