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
package org.jwatch.domain.connection;

import org.apache.log4j.Logger;
import org.jwatch.domain.adapter.QuartzJMXAdapter;
import org.jwatch.domain.adapter.QuartzJMXAdapterImplV2_0;
import org.jwatch.domain.instance.QuartzInstance;
import org.jwatch.listener.settings.QuartzConfig;
import org.jwatch.util.GlobalConstants;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 9, 2011 9:56:21 AM
 */
public class QuartzConnectServiceImpl implements QuartzConnectService
{
   static Logger log = Logger.getLogger(QuartzConnectServiceImpl.class);

   /**
    * {@inheritDoc}
    */
   public QuartzInstance initInstance(QuartzConfig config) throws Exception
   {
      QuartzInstance quartzInstance = new QuartzInstance(config);

      // create url / add credentials map
      Map<String, String[]> env = new HashMap<String, String[]>();
      env.put(JMXConnector.CREDENTIALS, new String[]{config.getUserName(), config.getPassword()});
      JMXServiceURL jmxServiceURL = QuartzConnectUtil.createQuartzInstanceConnection(config);
      JMXConnector connector = JMXConnectorFactory.connect(jmxServiceURL, env);
      MBeanServerConnection connection = connector.getMBeanServerConnection();

      // test connection
      ObjectName mBName = new ObjectName("quartz:type=QuartzScheduler,*");
      Set names = connection.queryNames(mBName, null);
      Iterator iterator = names.iterator();
      if (iterator.hasNext())
      {
         ObjectName objectName = (ObjectName) iterator.next();
         quartzInstance.setMBeanServerConnection(connection);
         quartzInstance.setObjectName(objectName);

         QuartzJMXAdapter jmxAdapter = initQuartzJMXAdapter(objectName, connection);
         quartzInstance.setJmxAdapter(jmxAdapter);
         String v = jmxAdapter.getVersion(quartzInstance);
         if (!QuartzConnectUtil.isSupported(v))
         {
            log.error(GlobalConstants.MESSAGE_WARN_VERSION + " Version:" + v);
         }
      }
      return quartzInstance;
   }

   /**
    * Currently creates the v2.0.0 adapter. In the future, we will need to have an adapter map that returns the correct
    * adapter object to use depending on version.
    *
    * @param objectName
    * @param connection
    * @return
    * @throws Exception
    */
   private QuartzJMXAdapter initQuartzJMXAdapter(ObjectName objectName, MBeanServerConnection connection) throws Exception
   {
      QuartzJMXAdapter jmxAdapter = new QuartzJMXAdapterImplV2_0();
      return jmxAdapter;
   }
}
