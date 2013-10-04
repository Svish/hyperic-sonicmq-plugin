Hyperic SonicMQ plugin
===

Hyperic plugin for monitoring SonicMQ domains.


Notes
---

The plugin was created to monitor a clustered setup, which means it's thought to be run by 
a Hyperic Agent which is not running on any of the actual hosts where SonicMQ is running. 
This is so that the domain will be monitored as a single entity and that the monitoring will 
still work if any of the hosts in the clusters goes down.

This is also why the plugin only monitors metrics collected from the Sonic domain as I'm not 
sure how to monitor Java VM metrics and such remotely or how to do so nicely in a Hyperic
plugin.


Usage
---

1. Download jar from [releases](https://github.com/Svish/hyperic-sonicmq-plugin/releases) or build from source with `mvn package`
1. Install the plugin via the Plugin Manager
1. Go to the platform which should monitor the domain
1. Open Tools Menu -> New Server
1. Give it a name and select server type `SonicMQ 7.6.x`
	* The install path requires a value, but isn't actually used for anything
1. Edit the Configuration Properties of the server to define the domain, location, username and password


Tested with
---

* Hyperic 4.6
* SonicMQ 7.6.2
