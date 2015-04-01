# Introduction #

This document describes how to interact with JWatch RESTful API. As a standard convention, we will assume JWatch is accessible via:
http://localhost:8081/jwatch/

## Get all Quartz Instances ##

This will return all currently configured Quartz Instances.

```
http://localhost:8081/jwatch/ui?action=get_all_instances
```

Sample output:
```
{
  data: [
    {
      connected: false,
      host: "localhost",
      password: "",
      port: 2911,
      uname: "localhost2911",
      userName: "",
      uuid: "f5c1edd6-0101-4c93-9162-58ca104b8fdb"
    }
  ],
  totalCount: 1,
  success: true
}
```

## Get all Schedulers for a Quartz Instance ##

To obtain all Shcedulers for a Quartz Instance, you will need to know the Quartz Instance UUID, by first getting all Quartz instances.

```
http://localhost:8081/jwatch/ui?action=get_schedulers&uuid=<UUID>
```

Sample output:
```
{
  success: true,
  data: [
    {
      instanceId: "AUTO2",
      jobStoreClassName: "org.quartz.simpl.RAMJobStore",
      name: "ASmallScheduler",
      objectName: {
        canonicalKeyPropertyListString: "instance=AUTO2,name=ASmallScheduler,type=QuartzScheduler",
        canonicalName: "quartz:instance=AUTO2,name=ASmallScheduler,type=QuartzScheduler",
        domain: "quartz",
        domainPattern: false,
        keyPropertyList: {
          instance: "AUTO2",
          name: "ASmallScheduler",
          type: "QuartzScheduler"
        },
        keyPropertyListString: "type=QuartzScheduler,name=ASmallScheduler,instance=AUTO2",
        pattern: false,
        propertyListPattern: false,
        propertyPattern: false,
        propertyValuePattern: false
      },
      quartzInstanceUUID: "f5c1edd6-0101-4c93-9162-58ca104b8fdb",
      shutdown: false,
      standByMode: false,
      started: true,
      threadPoolClassName: "org.quartz.simpl.SimpleThreadPool",
      threadPoolSize: 3,
      uuidInstance: "f5c1edd6-0101-4c93-9162-58ca104b8fdb@@AUTO2",
      version: "2.0.0"
    },...
```

### Get Scheduler Information ###

To obtain Scheduler Information, you will need to know the uuidInstance value, using the call above. It's important to note that the uuidInstance value is simply a concatenation of the Quartz Instance ID and the Scheduler Instance Id with @@ in between:

```
[uuidInstance]@@[instanceId]
```

```
http://localhost:8081/jwatch/ui?action=get_scheduler_info&uuidInstance=[uuidInstance]
```

```
{
  instanceId: "NON_CLUSTERED",
  jobStoreClassName: "org.quartz.simpl.RAMJobStore",
  name: "MyScheduler",
  objectName: {
    canonicalKeyPropertyListString: "instance=NON_CLUSTERED,name=MyScheduler,type=QuartzScheduler",
    canonicalName: "quartz:instance=NON_CLUSTERED,name=MyScheduler,type=QuartzScheduler",
    domain: "quartz",
    domainPattern: false,
    keyPropertyList: {
      instance: "NON_CLUSTERED",
      name: "MyScheduler",
      type: "QuartzScheduler"
    },
    keyPropertyListString: "type=QuartzScheduler,name=MyScheduler,instance=NON_CLUSTERED",
    pattern: false,
    propertyListPattern: false,
    propertyPattern: false,
    propertyValuePattern: false
  },
  quartzInstanceUUID: "f5c1edd6-0101-4c93-9162-58ca104b8fdb",
  shutdown: false,
  standByMode: false,
  started: true,
  threadPoolClassName: "org.quartz.simpl.SimpleThreadPool",
  threadPoolSize: 3,
  uuidInstance: "f5c1edd6-0101-4c93-9162-58ca104b8fdb@@NON_CLUSTERED",
  version: "2.0.0",
  success: true
}
```


### Get all Jobs for a Scheduler ###

To obtain all jobs from a Scheduler, you will need to know the uuidInstance value, using the call above. It's important to note that the uuidInstance value is simply a concatenation of the Quartz Instance ID and the Scheduler Instance Id with @@ in between:

```
[uuidInstance]@@[instanceId]
```

```
http://localhost:8081/jwatch/ui?action=get_jobs&uuidInstance=[uuidInstance]
```

```
{
  success: true,
  data: [
    {
      description: "Some rand jobx",
      durability: true,
      group: "group1",
      jobClass: "org.qtest.job.HelloJob",
      jobDataMap: null,
      jobName: "j_0",
      nextFireTime: "06/30/11 16:34:00 EDT",
      numTriggers: 1,
      quartzInstanceId: "f5c1edd6-0101-4c93-9162-58ca104b8fdb",
      schedulerInstanceId: "MEGA",
      shouldRecover: false
    },
    {
      description: "Some rand jobx",
      durability: true,
      group: "group0",
      jobClass: "org.qtest.job.HelloJob",
      jobDataMap: null,
      jobName: "j_0",
      nextFireTime: "06/30/11 16:34:00 EDT",
      numTriggers: 1,
      quartzInstanceId: "f5c1edd6-0101-4c93-9162-58ca104b8fdb",
      schedulerInstanceId: "MEGA",
      shouldRecover: false
    },...
```

### Get Triggers For a Job ###

```
http://localhost:8081/jwatch/ui?action=get_job_triggers&groupName=[group]&jobName=[jobName]&sid=[instanceId]&uuid=[quartzInstanceUUID]
```

Sample Output:
```
{
  data: [
    {
      STriggerState: "NORMAL",
      calendarName: "",
      description: "",
      finalFireTime: null,
      fireInstanceId: "1309458445830",
      group: "groupx",
      jobGroup: "groupx",
      jobName: "myJobx",
      misfireInstruction: 0,
      name: "myTriggerx",
      nextFireTime: "06/30/11 16:42:25 EDT",
      previousFireTime: "06/30/11 16:34:55 EDT",
      priority: 5,
      startTime: "06/30/11 14:27:25 EDT"
    },
    {
      STriggerState: "NORMAL",
      calendarName: "",
      description: "",
      finalFireTime: null,
      fireInstanceId: "1309458445790",
      group: "groupx2",
      jobGroup: "groupx",
      jobName: "myJobx",
      misfireInstruction: 0,
      name: "myTriggerx2",
      nextFireTime: "06/30/11 17:27:25 EDT",
      previousFireTime: "06/30/11 16:27:25 EDT",
      priority: 5,
      startTime: "06/30/11 14:27:25 EDT"
    }
  ],
  success: true,
  totalCount: 2
}
```

## Job Monitoring API ##

The job monitoring API aggregates all jobs executing across all of your configured Quartz Instances.

_Currently there is no capability available to filter or sort results by Quartz Instance or Scheduler. This will be added in a future release._

### Getting all Executed Jobs in Queue ###

To obtain all the jobs executed and sitting in the queue:
```
http://localhost:8081/jwatch/ui?action=monitor_jobs
```

Sample output:
```
{
  data: [
    {
      calendarName: "",
      fireTime: "06/30/11 15:59:01 EDT",
      jobGroup: "group0",
      jobName: "j_1",
      jobRunTime: 0,
      nextFireTime: "06/30/11 16:00:01 EDT",
      previousFireTime: "06/30/11 15:58:01 EDT",
      quartzInstanceId: "f5c1edd6-0101-4c93-9162-58ca104b8fdb",
      recovering: false,
      refireCount: 0,
      scheduledFireTime: "06/30/11 15:59:01 EDT",
      schedulerId: "MEGA",
      schedulerName: "MegaScheduler",
      triggerGroup: "group0",
      triggerName: "t_1"
    },
    {
      calendarName: "",
      fireTime: "06/30/11 15:59:01 EDT",
      jobGroup: "group1",
      jobName: "j_1",
      jobRunTime: 0,
      nextFireTime: "06/30/11 16:00:01 EDT",
      previousFireTime: "06/30/11 15:58:01 EDT",
      quartzInstanceId: "f5c1edd6-0101-4c93-9162-58ca104b8fdb",
      recovering: false,
      refireCount: 0,
      scheduledFireTime: "06/30/11 15:59:01 EDT",
      schedulerId: "MEGA",
      schedulerName: "MegaScheduler",
      triggerGroup: "group1",
      triggerName: "t_1"
    },...
```