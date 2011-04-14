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

import org.jwatch.domain.instance.QuartzInstance;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 12, 2011 4:31:31 PM
 */
public class QuartzJMXAdapterImplV2_0 implements QuartzJMXAdapter
{
   @Override
   public String getVersion(QuartzInstance quartzInstance) throws Exception
   {
      String quartzVersion = (String) quartzInstance.getMBeanServerConnection().getAttribute(quartzInstance.getObjectName(), "Version");
      return quartzVersion;
   }

   @Override
   public void printAttributes(QuartzInstance quartzInstance) throws Exception
   {
      MBeanInfo info = quartzInstance.getMBeanServerConnection().getMBeanInfo(quartzInstance.getObjectName());
      MBeanAttributeInfo[] attributeInfos = info.getAttributes();
      for (int i = 0; i < attributeInfos.length; i++)
      {
         MBeanAttributeInfo attributeInfo = attributeInfos[i];
         System.out.println(attributeInfo.toString());
      }
   }

   public void printConstructors(QuartzInstance quartzInstance) throws Exception
   {
      MBeanInfo info = quartzInstance.getMBeanServerConnection().getMBeanInfo(quartzInstance.getObjectName());
      MBeanConstructorInfo[] arr = info.getConstructors();
      for (int i = 0; i < arr.length; i++)
      {
         MBeanConstructorInfo s = arr[i];
         System.out.println(s.toString());
      }
   }

   public void printOperations(QuartzInstance quartzInstance) throws Exception
   {
      MBeanInfo info = quartzInstance.getMBeanServerConnection().getMBeanInfo(quartzInstance.getObjectName());
      MBeanOperationInfo[] arr = info.getOperations();
      for (int i = 0; i < arr.length; i++)
      {
         MBeanOperationInfo s = arr[i];
         System.out.println(s.toString());
      }
   }

   public void printNotifications(QuartzInstance quartzInstance) throws Exception
   {
      MBeanInfo info = quartzInstance.getMBeanServerConnection().getMBeanInfo(quartzInstance.getObjectName());
      MBeanNotificationInfo[] arr = info.getNotifications();
      for (int i = 0; i < arr.length; i++)
      {
         MBeanNotificationInfo s = arr[i];
         System.out.println(s.toString());
      }
   }

   @Override
   public void printClassName(QuartzInstance quartzInstance) throws Exception
   {
      MBeanInfo info = quartzInstance.getMBeanServerConnection().getMBeanInfo(quartzInstance.getObjectName());
      System.out.println(info.getClassName() + " Desc: " + info.getDescription());
   }
}
