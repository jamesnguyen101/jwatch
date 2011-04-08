/*
 
*/
package org.jwatch.domain.settings;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import org.apache.log4j.Logger;
import org.jwatch.domain.instance.QuartzInstance;
import org.jwatch.domain.instance.QuartzInstanceMap;
import org.jwatch.util.GlobalConstants;
import org.jwatch.util.SettingsUtil;
import org.jwatch.util.Tools;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Config in web.xml. loads settings file on boot.
 *
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 7, 2011 1:34:12 PM
 */
public class SettingsLoaderListener implements ServletContextListener
{
   static Logger log = Logger.getLogger(SettingsLoaderListener.class);

   public void contextInitialized(ServletContextEvent event)
   {
      try
      {
         log.info("Starting Settings Load...");
         long start = Calendar.getInstance().getTimeInMillis();

         QuartzInstanceMap.initQuartzInstanceMap();

         long end = Calendar.getInstance().getTimeInMillis();
         log.info("Settings startup completed in: " + (end - start) + " ms");
      }
      catch (Throwable t)
      {
         log.error("Failed to initialize Settings.", t);
      }
   }

   public void contextDestroyed(ServletContextEvent event)
   {
      // TODO: maybe some sort of file handle close calls here?
      log.info("Shutting down SettingsLoaderListener service...");
   }

   public static void main(String[] args)
   {
      try
      {
         QuartzInstance quartzInstance = new QuartzInstance(Tools.generateUUID(), "localhost", 11, null, null);
         QuartzInstance quartzInstance2 = new QuartzInstance(Tools.generateUUID(), "localhost1", 12, "foo", null);
         QuartzInstance quartzInstance3 = new QuartzInstance(Tools.generateUUID(), "localhost2", 13, null, "bar");
         List list = new ArrayList();
         list.add(quartzInstance);
         list.add(quartzInstance2);
         list.add(quartzInstance3);
         XStream xStream = new XStream(new JettisonMappedXmlDriver());
         xStream.setMode(XStream.NO_REFERENCES);
         xStream.alias(GlobalConstants.JSON_DATA_ROOT_KEY, ArrayList.class);
         String json = xStream.toXML(list);
         System.out.println(json);

         SettingsUtil.saveConfig(list, SettingsUtil.getConfigPath());

         xStream.alias(GlobalConstants.JSON_DATA_ROOT_KEY, ArrayList.class);
         List instances = SettingsUtil.loadConfig();
         for (int i = 0; i < instances.size(); i++)
         {
            QuartzInstance instance = (QuartzInstance) instances.get(i);
            System.out.println(instance);
         }
      }
      catch (Throwable t)
      {
         log.error(t);
      }
   }

}
