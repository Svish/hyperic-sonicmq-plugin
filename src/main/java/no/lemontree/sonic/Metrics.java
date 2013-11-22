package no.lemontree.sonic;

/**
 * Aliases used for metrics in the plugin descriptor.
 * 
 * Some of these also correspond to metric names in Sonic for simplicity.
 */
public final class Metrics
{
	private Metrics() {}
	
	public static final class Domain
	{
		private Domain() {}
		public static final String UpTime = "UpTime";
	}

	public static final class Container
	{
		private Container() {}
		public static final String UpTime = "UpTime";
		public static final String CurrentMemoryUsage = "system.memory.CurrentUsage";
		public static final String MaxMemoryUsage = "system.memory.MaxUsage";
		public static final String CurrentThreadUsage = "system.threads.CurrentTotal";
	}
	
	public static final class Broker
	{
		private Broker() {}
		public static final String UpTime = "UpTime";
		public static final String ReplicationState = "ReplicationState";
		public static final String IsPrimary = "IsPrimary";
		public static final String BytesDeliveredPerSecond = "broker.bytes.DeliveredPerSecond";
		public static final String BytesReceivedPerSecond = "broker.bytes.ReceivedPerSecond";
		public static final String ConnectionCount = "broker.connections.Count";
		public static final String MessagesDelivered = "broker.messages.Delivered";
		public static final String MessagesReceived = "broker.messages.Received";
	}
	
	public static final class Queue
	{
		private Queue() {}
		public static final String MessageCount = "queue.messages.Count";
	}
}
