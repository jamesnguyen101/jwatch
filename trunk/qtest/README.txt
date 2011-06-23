QTest App:

When deployed in a web-app container, the QTest war will auto-create a random amount of Quartz Schedulers, Jobs, and Triggers.

To enable monitoring, you must ensure that the container has JMX available, and that the Quartz Scheduler is exposing it's innards:

VM Parameters:
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=2911
-Dcom.sun.management.jmxremote.ssl=false
-Dcom.sun.management.jmxremote.authenticate=false
-Dorg.quartz.scheduler.jmx.export=true