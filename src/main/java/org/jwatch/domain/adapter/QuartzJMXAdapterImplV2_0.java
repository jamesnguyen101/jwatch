/**
 * JWatch - Quartz Monitor: http://code.google.com/p/jwatch/
 * Copyright (C) 2011 Roy Russo and the original author or authors.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301 USA
 **/
package org.jwatch.domain.adapter;

import org.apache.log4j.Logger;
import org.jwatch.domain.connection.QuartzConnectUtil;
import org.jwatch.domain.instance.QuartzInstanceConnection;
import org.jwatch.domain.quartz.Scheduler;
import org.jwatch.util.GlobalConstants;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 12, 2011 4:31:31 PM
 */
public class QuartzJMXAdapterImplV2_0 implements QuartzJMXAdapter
{
   static Logger log = Logger.getLogger(QuartzJMXAdapterImplV2_0.class);

   @Override
   public String getVersion(QuartzInstanceConnection quartzInstanceConnection, ObjectName objectName) throws Exception
   {
      MBeanServerConnection connection = quartzInstanceConnection.getMBeanServerConnection();
      String quartzVersion = (String) connection.getAttribute(objectName, "Version");
      if (!QuartzConnectUtil.isSupported(quartzVersion))
      {
         log.error(GlobalConstants.MESSAGE_WARN_VERSION + " Version:" + quartzVersion);
      }
      return quartzVersion;
   }

   @Override
   public void printAttributes(QuartzInstanceConnection quartzInstanceConnection, ObjectName objectName) throws Exception
   {
      MBeanInfo info = quartzInstanceConnection.getMBeanServerConnection().getMBeanInfo(objectName);
      MBeanAttributeInfo[] attributeInfos = info.getAttributes();
      for (int i = 0; i < attributeInfos.length; i++)
      {
         MBeanAttributeInfo attributeInfo = attributeInfos[i];
         System.out.println(attributeInfo.toString());
      }
   }

   public void printConstructors(QuartzInstanceConnection quartzInstanceConnection, ObjectName objectName) throws Exception
   {
      MBeanInfo info = quartzInstanceConnection.getMBeanServerConnection().getMBeanInfo(objectName);
      MBeanConstructorInfo[] arr = info.getConstructors();
      for (int i = 0; i < arr.length; i++)
      {
         MBeanConstructorInfo s = arr[i];
         System.out.println(s.toString());
      }
   }

   public void printOperations(QuartzInstanceConnection quartzInstanceConnection, ObjectName objectName) throws Exception
   {
      MBeanInfo info = quartzInstanceConnection.getMBeanServerConnection().getMBeanInfo(objectName);
      MBeanOperationInfo[] arr = info.getOperations();
      for (int i = 0; i < arr.length; i++)
      {
         MBeanOperationInfo s = arr[i];
         System.out.println(s.toString());
      }
   }

   public void printNotifications(QuartzInstanceConnection quartzInstanceConnection, ObjectName objectName) throws Exception
   {
      MBeanInfo info = quartzInstanceConnection.getMBeanServerConnection().getMBeanInfo(objectName);
      MBeanNotificationInfo[] arr = info.getNotifications();
      for (int i = 0; i < arr.length; i++)
      {
         MBeanNotificationInfo s = arr[i];
         System.out.println(s.toString());
      }
   }

   @Override
   public void printClassName(QuartzInstanceConnection quartzInstanceConnection, ObjectName objectName) throws Exception
   {
      MBeanInfo info = quartzInstanceConnection.getMBeanServerConnection().getMBeanInfo(objectName);
      System.out.println(info.getClassName() + " Desc: " + info.getDescription());
   }

   @Override
   public Scheduler populateScheduler(QuartzInstanceConnection quartzInstanceConnection, ObjectName objectName) throws Exception
   {
      Scheduler scheduler = new Scheduler();
      MBeanServerConnection connection = quartzInstanceConnection.getMBeanServerConnection();
      scheduler.setObjectName(objectName);
      scheduler.setName((String) connection.getAttribute(objectName, "SchedulerName"));
      scheduler.setInstanceId((String) connection.getAttribute(objectName, "SchedulerInstanceId"));
      scheduler.setJobStoreClassName((String) connection.getAttribute(objectName, "JobStoreClassName"));
      scheduler.setThreadPoolClassName((String) connection.getAttribute(objectName, "ThreadPoolClassName"));
      scheduler.setThreadPoolSize((Integer) connection.getAttribute(objectName, "ThreadPoolSize"));
      scheduler.setShutdown((Boolean) connection.getAttribute(objectName, "Shutdown"));
      scheduler.setStarted((Boolean) connection.getAttribute(objectName, "Started"));
      scheduler.setStandByMode((Boolean) connection.getAttribute(objectName, "StandbyMode"));
      scheduler.setQuartzInstanceUUID(quartzInstanceConnection.getUuid());
      scheduler.setVersion(this.getVersion(quartzInstanceConnection, objectName));
      return scheduler;
   }
}
