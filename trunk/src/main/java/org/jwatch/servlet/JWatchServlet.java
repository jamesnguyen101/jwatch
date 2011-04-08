/*
 
*/
package org.jwatch.servlet;

import com.sun.jndi.rmi.registry.RegistryContextFactory;
import org.quartz.core.jmx.QuartzSchedulerMBean;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 4, 2011 9:29:14 AM
 */
public class JWatchServlet extends HttpServlet
{
   protected MBeanServerConnection mbsc = null;

   public final void init(ServletConfig servletConfig) throws ServletException
   {
      System.out.println("******************************");
   }

   public void doGet(HttpServletRequest req, HttpServletResponse res)
         throws ServletException
   {
      doPost(req, res);
   }

   public void doPost(HttpServletRequest req, HttpServletResponse res)
         throws ServletException
   {
      try
      {
         HashMap<String, String> map =
               new HashMap<String, String>();
         map.put("java.naming.factory.initial",
                 RegistryContextFactory.class.getName());
         JMXConnector connector = JMXConnectorFactory
               .connect(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9211/jmxrmi"));
         mbsc = connector.getMBeanServerConnection();

         System.out.println("\nDomains:");
         String domains[] = mbsc.getDomains();
         Arrays.sort(domains);
         for (String domain : domains)
         {
            System.out.println("\tDomain = " + domain);
         }

         System.out.println("\nMBeanServer default domain = " + mbsc.getDefaultDomain());

         System.out.println("\nMBean count = " + mbsc.getMBeanCount());
         System.out.println("\nQuery MBeanServer MBeans:");
         Set names = new TreeSet(mbsc.queryNames(null, null));

         // quartz:type=QuartzScheduler,name=MyScheduler,instance=NON_CLUSTERED
         ObjectName mbeanName = new ObjectName("quartz:instance=NON_CLUSTERED,name=MyScheduler,type=QuartzScheduler");
         QuartzSchedulerMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName, QuartzSchedulerMBean.class, true);
         System.out.println(mbeanProxy.getSchedulerName());


         List jobGroups = mbeanProxy.getJobGroupNames();
         Map pmap = mbeanProxy.getPerformanceMetrics();
         TabularData tdata = mbeanProxy.getCurrentlyExecutingJobs();
         for (int i = 0; i < jobGroups.size(); i++)
         {
            String groupName = (String) jobGroups.get(i);
            List<String> jobNames = mbeanProxy.getJobNames(groupName);
            for (int j = 0; j < jobNames.size(); j++)
            {
               String jobName = jobNames.get(j);
               CompositeData data = mbeanProxy.getJobDetail(jobName, groupName);

               System.out.println();
            }
            System.out.println();
         }
         connector.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}

