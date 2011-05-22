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
import org.jwatch.domain.instance.QuartzInstanceConnection;
import org.jwatch.domain.instance.QuartzInstanceConnectionService;
import org.jwatch.domain.quartz.Job;
import org.jwatch.domain.quartz.Scheduler;
import org.jwatch.domain.quartz.Trigger;
import org.jwatch.listener.settings.QuartzConfig;
import org.jwatch.util.GlobalConstants;
import org.jwatch.util.JSONUtil;
import org.jwatch.util.SettingsUtil;
import org.jwatch.util.Tools;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
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
    * Returns instances found. For now, it pulls from the config file every time.
    *
    * @return
    * @see org.jwatch.domain.instance.QuartzInstanceConnectionService
    */
   public static JSONObject loadInstances()
   {
      JSONObject jsonObject = new JSONObject();
      try
      {
         QuartzInstanceConnectionService.initQuartzInstanceMap();
         Map qMap = QuartzInstanceConnectionService.getQuartzInstanceMap();
         if (qMap != null)
         {
            JSONArray jsonArray = new JSONArray();
            for (Iterator it = qMap.entrySet().iterator(); it.hasNext();)
            {
               Map.Entry entry = (Map.Entry) it.next();
               String k = (String) entry.getKey();
               QuartzInstanceConnection quartzInstanceConnection = (QuartzInstanceConnection) qMap.get(k);
               QuartzConfig quartzConfig = new QuartzConfig(quartzInstanceConnection);
               JSONObject jo = JSONObject.fromObject(quartzConfig);
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

         if (StringUtils.trimToNull(host) != null)
         {
            QuartzConfig quartzConfig = new QuartzConfig(Tools.generateUUID(), host, port, username, password);
            QuartzConnectService quartzConnectService = new QuartzConnectServiceImpl();
            QuartzInstanceConnection quartzInstanceConnection = quartzConnectService.initInstance(quartzConfig);
            if (quartzInstanceConnection == null)
            {
               log.error(GlobalConstants.MESSAGE_FAILED_CONNECT + " " + quartzConfig);
               jsonObject = JSONUtil.buildError(GlobalConstants.MESSAGE_FAILED_CONNECT + " " + quartzConfig);
               return jsonObject;
            }

            // persist
            QuartzInstanceConnectionService.putQuartzInstance(quartzInstanceConnection);
            SettingsUtil.saveConfig(quartzConfig);
            jsonObject.put(GlobalConstants.JSON_DATA_ROOT_KEY, quartzConfig);
            jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, true);
         }
         else
         {
            jsonObject.put(GlobalConstants.JSON_MESSAGE, GlobalConstants.MESSAGE_CONFIG_EMPTY);
            jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, false);
         }
      }
      catch (UnknownHostException e)
      {
         log.error(e);
         jsonObject = JSONUtil.buildError("Unknown Host. " + GlobalConstants.MESSAGE_ERR_CHECK_LOG);
      }
      catch (Throwable t)
      {
         log.error(t);
         jsonObject = JSONUtil.buildError(GlobalConstants.MESSAGE_ERR_CHECK_LOG);
      }
      return jsonObject;
   }

   /**
    * Given a Quartz Instance id, it will load the schedulers associated with it from the in-memory map.
    *
    * @param map
    * @return
    */
   public static JSONObject getSchedulersForForQuartzInstance(Map map)
   {
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      String qiid = StringUtils.trimToNull((String) map.get("uuid"));
      try
      {
         QuartzInstanceConnection quartzInstanceConnection = QuartzInstanceConnectionService.getQuartzInstanceByID(qiid);
         if (quartzInstanceConnection != null)
         {
            int totalCount = 0;
            List<Scheduler> schedulers = quartzInstanceConnection.getSchedulerList();
            if (schedulers != null && schedulers.size() > 0)
            {
               totalCount = schedulers.size();
               for (int i = 0; i < schedulers.size(); i++)
               {
                  Scheduler scheduler = schedulers.get(i);
                  JSONObject o = JSONObject.fromObject(scheduler);
                  jsonArray.add(o);
               }
            }
            jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, true);
            jsonObject.put(GlobalConstants.JSON_DATA_ROOT_KEY, jsonArray);
            jsonObject.put(GlobalConstants.JSON_TOTAL_COUNT, totalCount);
         }
      }
      catch (Throwable t)
      {
         log.error(t);
         jsonObject = JSONUtil.buildError(GlobalConstants.MESSAGE_ERR_CHECK_LOG);
      }
      return jsonObject;
   }

   public static JSONObject getJobsForScheduler(Map map)
   {
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();
      int totalCount = 0;

      String uuidInstance = StringUtils.trimToNull((String) map.get("uuidInstance"));
      try
      {
         if (uuidInstance != null)
         {
            String[] arr = uuidInstance.split("@@");
            String uuid = arr[0];
            String scheduleID = arr[1];
            QuartzInstanceConnection quartzInstanceConnection = QuartzInstanceConnectionService.getQuartzInstanceByID(uuid);
            if (quartzInstanceConnection != null)
            {
               List<Job> jobs = quartzInstanceConnection.getJmxAdapter().getJobDetails(quartzInstanceConnection, scheduleID);
               if (jobs != null && jobs.size() > 0)
               {
                  totalCount = jobs.size();
                  for (int i = 0; i < jobs.size(); i++)
                  {
                     Job job = jobs.get(i);
                     JSONObject o = JSONObject.fromObject(job);
                     jsonArray.add(o);
                  }
               }
            }
         }
         jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, true);
         jsonObject.put(GlobalConstants.JSON_DATA_ROOT_KEY, jsonArray);
         jsonObject.put(GlobalConstants.JSON_TOTAL_COUNT, totalCount);
      }
      catch (Throwable t)
      {
         log.error(t);
         jsonObject = JSONUtil.buildError(GlobalConstants.MESSAGE_ERR_LOAD_JOBS);
      }
      return jsonObject;
   }

   public static JSONObject getSchedulerInfo(Map map)
   {
      JSONObject jsonObject = new JSONObject();
      String uuidInstance = StringUtils.trimToNull((String) map.get("uuidInstance"));
      try
      {
         if (uuidInstance != null)
         {
            String[] arr = uuidInstance.split("@@");
            String uuid = arr[0];
            String scheduleID = arr[1];
            QuartzInstanceConnection quartzInstanceConnection = QuartzInstanceConnectionService.getQuartzInstanceByID(uuid);
            if (quartzInstanceConnection != null)
            {
               Scheduler scheduler = quartzInstanceConnection.getJmxAdapter().getScheduler(quartzInstanceConnection, scheduleID);
               if (scheduler != null)
               {
                  jsonObject = JSONObject.fromObject(scheduler);
               }
            }
         }
         jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, true);
      }
      catch (Throwable t)
      {
         log.error(t);
         jsonObject = JSONUtil.buildError(GlobalConstants.MESSAGE_ERR_LOAD_SCHEDULER);
      }
      return jsonObject;
   }

   public static JSONObject getTriggersForJob(Map map)
   {
      JSONObject jsonObject = new JSONObject();
      JSONArray jsonArray = new JSONArray();

      String qiid = StringUtils.trimToNull((String) map.get("uuid"));
      String jobName = StringUtils.trimToNull((String) map.get("jobName"));
      String groupName = StringUtils.trimToNull((String) map.get("groupName"));
      String scheduleID = StringUtils.trimToNull((String) map.get("sid"));
      int totalCount = 0;
      try
      {
         QuartzInstanceConnection quartzInstanceConnection = QuartzInstanceConnectionService.getQuartzInstanceByID(qiid);
         List<Trigger> triggers = quartzInstanceConnection.getJmxAdapter().getTriggersForJob(quartzInstanceConnection, scheduleID, jobName, groupName);
         if (triggers != null && triggers.size() > 0)
         {
            totalCount = triggers.size();
            for (int i = 0; i < triggers.size(); i++)
            {
               Trigger trigger = triggers.get(i);
               JSONObject object = JSONObject.fromObject(trigger);
               jsonArray.add(object);
            }
         }
         jsonObject.put(GlobalConstants.JSON_DATA_ROOT_KEY, jsonArray);
         jsonObject.put(GlobalConstants.JSON_SUCCESS_KEY, true);
         jsonObject.put(GlobalConstants.JSON_TOTAL_COUNT, totalCount);
      }
      catch (Throwable t)
      {
         log.error(t);
         jsonObject = JSONUtil.buildError(GlobalConstants.MESSAGE_ERR_LOAD_TRIGGERS);
      }
      return jsonObject;
   }
}
