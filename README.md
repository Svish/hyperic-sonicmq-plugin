Hyperic SonicMQ plugin
===

Hyperic plugin for monitoring SonicMQ domains.


Notes
---

The plugin was created to monitor a clustered setup, which means it's thought to be run by a Hyperic Agent which is not running on any of the actual hosts where SonicMQ is running. This is so that the domain will be monitored as a single entity and that the monitoring will still work if any of the hosts in the clusters goes down.


Usage
---

# Install the plugin via the Plugin Manager
# Go to the platform which should monitor the domain
# Open Tools Menu -> New Server
# Give it a name and select server type `SonicMQ 7.6.x`
	* The install path requires a value, but isn't actually used for anything
# Edit the Configuration Properties of the server to define the domain, location, username and password

