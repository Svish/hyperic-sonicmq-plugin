package no.lemontree.sonic;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for classes which collects metrics for the Hyperic plugin.
 * 
 * Created to disconnects actual collection from Hyperic dependencies and enable testing.
 */
public abstract class Collector
{
	private boolean availability = false;
	private final HashMap<String, Double> metrics = new HashMap<String, Double>();
	
	protected void setMetric(String key, double value)
	{
		// NOTE: Metrics MUST be doubles, otherwise we end up with runtime NumberFormatExceptions
		metrics.put(key, value);
	}
	protected void setAvailability(boolean available)
	{
		availability = available;
	}
	
	public final boolean getAvailability()
	{
		return availability;
	}
	
	public final Map<String, Double> getMetrics()
	{
		return metrics;
	}
}
