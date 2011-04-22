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

import org.apache.log4j.Logger;
import org.jwatch.domain.connection.QuartzConnectService;
import org.jwatch.domain.connection.QuartzConnectServiceImpl;
import org.jwatch.listener.settings.QuartzConfig;
import org.jwatch.util.SettingsUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Maps contain QuartzInstance objects loaded from the settings file and on-demand by the ui.
 *
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 7, 2011 5:04:10 PM
 */
public class QuartzInstanceConnectionService
{
   static Logger log = Logger.getLogger(QuartzInstanceConnectionService.class);

   private static HashMap<String, QuartzInstanceConnection> quartzInstanceMap;

   public static HashMap<String, QuartzInstanceConnection> getQuartzInstanceMap()
   {
      return quartzInstanceMap;
   }

   public static void setQuartzInstanceMap(HashMap<String, QuartzInstanceConnection> quartzInstanceMap)
   {
      QuartzInstanceConnectionService.quartzInstanceMap = quartzInstanceMap;
   }

   public static void putQuartzInstance(QuartzInstanceConnection quartzInstanceConnection)
   {
      if (quartzInstanceConnection != null)
      {
         if (quartzInstanceMap == null)
         {
            quartzInstanceMap = new HashMap();
         }
         quartzInstanceMap.put(quartzInstanceConnection.getUuid(), quartzInstanceConnection);
      }
   }

   /**
    * loads configurations from the settings file and attempts connections on each config. It then populates in-memory
    * map with {@link QuartzInstanceConnection} objects.
    */
   public static void initQuartzInstanceMap()
   {
      if (quartzInstanceMap == null)
      {
         quartzInstanceMap = new HashMap();
      }

      List<QuartzConfig> configs = SettingsUtil.loadConfig();
      if (configs != null && configs.size() > 0)
      {
         log.info("Found " + configs.size() + " Quartz instances in settings file.");
         for (int i = 0; i < configs.size(); i++)
         {
            QuartzConfig config = configs.get(i);
            QuartzConnectService quartzConnectService = new QuartzConnectServiceImpl();
            List<QuartzInstanceConnection> instanceConnections = null;
            try
            {
               instanceConnections = quartzConnectService.initInstance(config);
               config.setConnected(true);
               SettingsUtil.saveConfig(config);
            }
            catch (Throwable t)
            {
               log.error("Failed to connect! " + config.toString(), t);
            }

            if (instanceConnections != null && instanceConnections.size() > 0)
            {
               for (int j = 0; j < instanceConnections.size(); j++)
               {
                  QuartzInstanceConnection instanceConnection = instanceConnections.get(j);
                  QuartzInstanceConnectionService.putQuartzInstance(instanceConnection);
                  log.debug(instanceConnection.toString());
               }
            }
         }
      }
   }

   /**
    * fetch instance object from map.
    *
    * @param qiid
    * @return
    */
   public static QuartzInstanceConnection getQuartzInstanceByID(String qiid)
   {
      if (quartzInstanceMap != null)
      {
         return quartzInstanceMap.get(qiid);
      }
      return null;
   }

}
