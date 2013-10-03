package org.hyperic.hq.plugin.sonic;
import javax.management.ObjectName;

import com.sonicsw.mf.jmx.client.JMSConnectorClient;
import com.sonicsw.mf.mgmtapi.runtime.IAgentProxy;
import com.sonicsw.mf.mgmtapi.runtime.IDirectoryServiceProxy;
import com.sonicsw.mf.mgmtapi.runtime.MFProxyFactory;
import com.sonicsw.mq.mgmtapi.runtime.IBrokerProxy;
import com.sonicsw.mq.mgmtapi.runtime.MQProxyFactory;

public final class Common
{

    /**
     * Gets a management proxy for the Directory Service component; the DS component is used to
     * manage the domain configuration store.
     */
    public static IDirectoryServiceProxy getDirectoryServiceProxy(JMSConnectorClient connector, String domain)
    throws Exception
    {
        ObjectName jmxName = new ObjectName(domain + ".DIRECTORY SERVICE:ID=DIRECTORY SERVICE");
        return MFProxyFactory.createDirectoryServiceProxy(connector, jmxName);
    }
    
    
    /**
     * Gets a management proxy for an AGENT component; the AGENT component is used to
     * manage a container.
     */
    public static IAgentProxy getAgentProxy(JMSConnectorClient connector, String domain, String container)
    throws Exception
    {
        ObjectName jmxName = new ObjectName(domain + "." + container + ":ID=AGENT");
        return MFProxyFactory.createAgentProxy(connector, jmxName);
    }

    /**
     * Gets a management proxy for a SOnicMQ broker component.
     */
    public static IBrokerProxy getBrokerProxy(JMSConnectorClient connector, String domain, String container, String id)
    throws Exception
    {
        ObjectName jmxName = new ObjectName(domain + "." + container + ":ID=" + id);
        return MQProxyFactory.createBrokerProxy(connector, jmxName);
    }
}