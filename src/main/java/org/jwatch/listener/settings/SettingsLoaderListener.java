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
package org.jwatch.listener.settings;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import org.apache.log4j.Logger;
import org.jwatch.domain.instance.QuartzInstance;
import org.jwatch.domain.instance.QuartzInstanceService;
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

         QuartzInstanceService.initQuartzInstanceMap();
         
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
}
