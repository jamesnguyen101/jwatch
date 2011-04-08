/*
 
*/
package org.jwatch.domain.instance;

import org.apache.log4j.Logger;
import org.jwatch.util.SettingsUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Map contains @QuartzInstance objects.
 *
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 7, 2011 5:04:10 PM
 */
public class QuartzInstanceMap
{
   static Logger log = Logger.getLogger(QuartzInstanceMap.class);

   private static HashMap<String, QuartzInstance> quartzInstanceMap;

   public static HashMap<String, QuartzInstance> getQuartzInstanceMap()
   {
      return quartzInstanceMap;
   }

   public static void setQuartzInstanceMap(HashMap<String, QuartzInstance> quartzInstanceMap)
   {
      QuartzInstanceMap.quartzInstanceMap = quartzInstanceMap;
   }

   public static void putQuartzInstance(QuartzInstance quartzInstance)
   {


      if (quartzInstance != null)
      {
         quartzInstanceMap.put(quartzInstance.getUuid(), quartzInstance);
      }
   }

   public static void initQuartzInstanceMap()
   {
      if (quartzInstanceMap == null)
      {
         quartzInstanceMap = new HashMap();
      }

      List<QuartzInstance> instances = SettingsUtil.loadConfig();
      if (instances != null && instances.size() > 0)
      {
         log.info("Found " + instances.size() + " Quartz instances in settings file.");
         for (int i = 0; i < instances.size(); i++)
         {
            QuartzInstance quartzInstance = instances.get(i);
            QuartzInstanceMap.putQuartzInstance(quartzInstance);
            log.debug(quartzInstance.toString());
         }
      }
   }
}
