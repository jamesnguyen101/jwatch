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
package org.jwatch.domain.instance;

import org.jwatch.domain.adapter.QuartzJMXAdapter;
import org.jwatch.listener.settings.QuartzConfig;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 6, 2011 4:57:14 PM
 */
public class QuartzInstance extends QuartzConfig
{
   private MBeanServerConnection mBeanServerConnection;
   private ObjectName objectName;
   private QuartzJMXAdapter jmxAdapter;

   public QuartzInstance(String uuid, String host, int port, String userName, String password)
   {
      super(uuid, host, port, userName, password);
   }

   public QuartzInstance(QuartzConfig config)
   {
      super(config.getUuid(), config.getHost(), config.getPort(), config.getUserName(), config.getPassword());
   }

   public MBeanServerConnection getMBeanServerConnection()
   {
      return mBeanServerConnection;
   }

   public void setMBeanServerConnection(MBeanServerConnection mBeanServerConnection)
   {
      this.mBeanServerConnection = mBeanServerConnection;
   }

   public ObjectName getObjectName()
   {
      return objectName;
   }

   public void setObjectName(ObjectName objectName)
   {
      this.objectName = objectName;
   }

   public QuartzJMXAdapter getJmxAdapter()
   {
      return jmxAdapter;
   }

   public void setJmxAdapter(QuartzJMXAdapter jmxAdapter)
   {
      this.jmxAdapter = jmxAdapter;
   }

   @Override
   public String toString()
   {
      return "QuartzInstance{" +
             "mBeanServerConnection=" + mBeanServerConnection +
             ", objectName=" + objectName +
             "} " + super.toString();
   }
}
