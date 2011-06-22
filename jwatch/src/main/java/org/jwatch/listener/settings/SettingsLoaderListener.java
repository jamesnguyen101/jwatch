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

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jwatch.domain.instance.QuartzInstance;
import org.jwatch.domain.instance.QuartzInstanceService;
import org.jwatch.listener.notification.EventService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

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

         ServletContext sc = event.getServletContext();
         if (sc != null)
         {
            String sMaxEvents = sc.getInitParameter("maxevents");
            int maxEvents = NumberUtils.toInt(sMaxEvents, EventService.DEFAULT_MAX_EVENT_LIST_SIZE);
            sc.setAttribute("maxevents", maxEvents);      // expose to other servlets
            EventService.setMaxEventListSize(maxEvents);
         }

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
      log.info("Shutting down SettingsLoaderListener service...");
      Map qMap = QuartzInstanceService.getQuartzInstanceMap();
      for (Iterator it = qMap.entrySet().iterator(); it.hasNext();)
      {
         Map.Entry entry = (Map.Entry) it.next();
         String k = (String) entry.getKey();
         QuartzInstance quartzInstance = (QuartzInstance) qMap.get(k);
         try
         {
            quartzInstance.getJmxConnector().close();
         }
         catch (IOException e)
         {
            log.error("Failed to close Connection: " + quartzInstance, e);
         }
      }
   }
}
