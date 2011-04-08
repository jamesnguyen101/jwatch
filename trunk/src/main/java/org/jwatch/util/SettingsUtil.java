/*
 
*/
package org.jwatch.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jwatch.domain.instance.QuartzInstance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * loads, edits, deleted settings file containing quartz instance connection data.
 *
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 6, 2011 4:57:49 PM
 */
public class SettingsUtil
{
   static Logger log = Logger.getLogger(SettingsUtil.class);
   private static final String jwatchConfigFile = "jwatch.json";

   /**
    * Returns FULL drive path to config file.
    *
    * @return
    */
   public static String getConfigPath()
   {
      String userHome = (String) System.getProperties().get("user.home");
      String cfgFile = userHome + File.separator + jwatchConfigFile;

      File file = new File(cfgFile);

      if (!file.canRead())
      {
         log.error("Cannot read JWatch config at: " + cfgFile);
      }

      if (!file.canWrite())
      {
         log.error("Cannot write to JWatch config at: " + cfgFile);
      }

      return cfgFile;
   }

   public static List loadConfig()
   {
      List instances = null;
      String configPath = SettingsUtil.getConfigPath();
      InputStream inputStream = null;
      try
      {
         File configFile = new File(configPath);
         if (!configFile.exists())
         {
            log.info("Settings file not found! Creating new file at " + configPath);
            FileUtils.touch(configFile);
            log.info("Settings file created at " + configPath);
         }
         else
         {
            inputStream = new FileInputStream(configFile);
         }
         XStream xStream = new XStream(new JettisonMappedXmlDriver());
         xStream.setMode(XStream.NO_REFERENCES);
         xStream.alias(GlobalConstants.JSON_DATA_ROOT_KEY, ArrayList.class);
         instances = ((List) xStream.fromXML(inputStream));
      }
      catch (Throwable t)
      {
         log.error(t);
      }
      finally
      {
         try
         {
            if (inputStream != null)
            {
               inputStream.close();
            }
         }
         catch (IOException ioe)
         {
            log.error("Unable to close config file handle at " + configPath, ioe);
         }
      }
      return instances;
   }

   public static void saveConfig(List<QuartzInstance> instances, String configFilePath)
   {
      FileOutputStream fileOutputStream = null;
      try
      {
         fileOutputStream = new FileOutputStream(configFilePath);

         XStream xStream = new XStream(new JettisonMappedXmlDriver());
         xStream.setMode(XStream.NO_REFERENCES);
         xStream.alias(GlobalConstants.JSON_DATA_ROOT_KEY, ArrayList.class);
         xStream.toXML(instances, fileOutputStream);
      }
      catch (Throwable t)
      {
         log.error(t);
      }
      finally
      {
         try
         {
            if (fileOutputStream != null)
            {
               fileOutputStream.close();
            }
         }
         catch (IOException ioe)
         {
            log.error("Unable to close config file handle at " + configFilePath, ioe);
         }
      }
   }
}
