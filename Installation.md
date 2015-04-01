# Installation Instructions #

_Note: At present, JWatch can only interact with Quartz 2.0+. Future releases will support older versions of Quartz._

  1. Get the most recent WAR file from the [Downloads Section](http://code.google.com/p/jwatch/downloads/list). It should be labeled something like jwatch-(VERSION).zip
  1. Unpack the zip file and deploy the war within it to your application server.
  1. Point your browser to http://HOST:PORT/jwatch
  1. Note: You must have JMX enabled in **your** application for JWatch to communicate with your Quartz instances:

## Configuring JMX in your Application ##

JWatch communicates with Quartz Instances using JMX, so the JVM Quartz is running on, will have to have JMX enabled, **and** Quartz must also be exposing its internal MBeans.

Sample JVM parameters:
```
-Dcom.sun.management.jmxremote=true
-Dcom.sun.management.jmxremote.port=2911
-Dcom.sun.management.jmxremote.ssl=false
-Dcom.sun.management.jmxremote.authenticate=false
-Dorg.quartz.scheduler.jmx.export=true
```

## Creating a Quartz Monitor ##

  1. Click the "Add New Quartz Instance" link on the top-right of the user-interface.
  1. Configure your Quartz Instance using the dialogue popup:
<img src='http://jwatch.googlecode.com/svn/trunk/site/images/ss/addnewinstance.png' />

# Configuration Options #

To increase the amount of executed jobs that JWatch keeps in the queue, you will need to modify the jwatch.war web.xml file. By default, JWatch keeps 1000 executed jobs in memory.

```
    <context-param>
        <param-name>maxevents</param-name>
        <param-value>1000</param-value>
        <description>How many job execution events to keep in the queue.</description>
    </context-param>
```