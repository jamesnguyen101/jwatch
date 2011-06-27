For information about JWatch, please visit the JWatch Home: http://code.google.com/p/jwatch/

Quick and Dirty setup:

1. Get the WAR from the JWatch Download page: http://code.google.com/p/jwatch/
2. Deploy the WAR to your Appserver.
3. Point your browser to http://<HOST>:<PORT>/jwatch

Note that JWatch communicates with Quartz Instances on any server anywhere, using JMX, so your Quartz instances must have
JMX exposed and Quartz must also be exposing its internal MBeans.

Example VM Parameters:
-Dcom.sun.management.jmxremote=true
-Dcom.sun.management.jmxremote.port=2911
-Dcom.sun.management.jmxremote.ssl=false
-Dcom.sun.management.jmxremote.authenticate=false
-Dorg.quartz.scheduler.jmx.export=true