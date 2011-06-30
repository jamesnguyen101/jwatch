For information about JWatch, please visit the JWatch Home: http://code.google.com/p/jwatch/

Installation and Configuration Options can be found here:

http://code.google.com/p/jwatch/wiki/Installation

Note that JWatch communicates with Quartz Instances on any server anywhere, using JMX, so your Quartz instances must have
JMX exposed and Quartz must also be exposing its internal MBeans.

Example VM Parameters:
-Dcom.sun.management.jmxremote=true
-Dcom.sun.management.jmxremote.port=2911
-Dcom.sun.management.jmxremote.ssl=false
-Dcom.sun.management.jmxremote.authenticate=false
-Dorg.quartz.scheduler.jmx.export=true