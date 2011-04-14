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
package org.jwatch.handler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jwatch.domain.connection.QuartzConnectService;
import org.jwatch.domain.connection.QuartzConnectServiceImpl;
import org.jwatch.domain.instance.QuartzInstance;
import org.jwatch.domain.instance.QuartzInstanceService;
import org.jwatch.listener.settings.QuartzConfig;
import org.jwatch.util.GlobalConstants;
import org.jwatch.util.JSONUtil;
import org.jwatch.util.SettingsUtil;
import org.jwatch.util.Tools;

import java.util.Iterator;
import java.util.Map;

/**
 * All interactions with QuartzInstance objects are handled through this class.
 *
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 8, 2011 4:31:24 PM
 */
public class QuartzInstanceHandler
{
   static Logger log = Logger.getLogger(QuartzInstanceHandler.class);

   /**
    * Returns instances found in memory map
    *
    * @return
    * @see org.jwatch.domain.instance.QuartzInstanceService
    */
   public static JSONObject getInstances()
   {
      JSONObject jsonObject = new JSONObject();
      try
      {
         Map qMap = QuartzInstanceService.getQuartzInstanceMap();
         if (qMap != null)
         {
            JSONArray jsonArray = new JSONArray();
            for (Iterator it = qMap.entrySet().iterator(); it.hasNext();)
            {
               Map.Entry entry = (Map.Entry) it.next();
               String k = (String) entry.getKey();
               QuartzInstance quartzInstance = (QuartzInstance) qMap.get(k);
               JSONObject jo = JSONObject.fromObject(quartzInstance);
               jsonArray.add(jo);
            }
            jsonObject.put(GlobalConstants.JSON_DATA_ROOT_KEY, jsonArray);
            jsonObject.put(GlobalConstants.JSON_TOTAL_COUNT, qMap.size());
         }
         jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, true);
      }
      catch (Throwable t)
      {
         jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, false);
      }
      return jsonObject;
   }

   /**
    * Given JMX connection settings: this will connect to the instance, and if successful
    * persist the new instance in the settings file and memory map.
    *
    * @param map
    * @return success/failure
    */
   public static JSONObject createInstance(Map map)
   {
      JSONObject jsonObject = new JSONObject();

      try
      {
         String host = StringUtils.trimToNull((String) map.get("host"));
         int port = Integer.valueOf(StringUtils.trimToNull((String) map.get("port")));
         String username = StringUtils.trimToNull((String) map.get("userName"));
         String password = StringUtils.trimToNull((String) map.get("password"));

         QuartzConfig quartzConfig = new QuartzConfig(Tools.generateUUID(), host, port, username, password);
         QuartzConnectService quartzConnectService = new QuartzConnectServiceImpl();
         QuartzInstance quartzInstance = quartzConnectService.initInstance(quartzConfig);
         if (quartzInstance == null)
         {
            log.error(GlobalConstants.MESSAGE_FAILED_CONNECT + " " + quartzConfig);
            jsonObject = JSONUtil.buildError(GlobalConstants.MESSAGE_FAILED_CONNECT + " " + quartzConfig);
            return jsonObject;
         }

         // persist
         QuartzInstanceService.putQuartzInstance(quartzInstance);
         SettingsUtil.saveConfig(quartzConfig);

         jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, true);
      }
      catch (Throwable t)
      {
         log.error(t);
         jsonObject = JSONUtil.buildError(GlobalConstants.MESSAGE_ERR_CHECK_LOG);
      }
      return jsonObject;
   }

   public static JSONObject getInstanceDetails(Map map)
   {
      JSONObject jsonObject = new JSONObject();
      String qiid = StringUtils.trimToNull((String) map.get("uuid"));
      QuartzInstance quartzInstance = QuartzInstanceService.getQuartzInstanceByID(qiid);
      if (quartzInstance != null)
      {

      }
      return jsonObject;
   }
}
