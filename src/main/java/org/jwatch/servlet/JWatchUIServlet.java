/*
 
*/
package org.jwatch.servlet;

import org.apache.commons.lang.StringUtils;
import org.jwatch.util.JSONUtil;
import org.jwatch.handler.QuartzInstanceHandler;

import javax.management.MBeanServerConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 4, 2011 9:29:14 AM
 */
public class JWatchUIServlet extends HttpServlet
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

      String subject = StringUtils.trimToNull(req.getParameter("action"));

      if (subject != null)
      {
         try
         {
            Object returnO = new Object();
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();

            if (subject.equalsIgnoreCase(ActionConstants.LOAD_INSTANCES))
            {
               Map map = JSONUtil.convertRequestToMap(req);
               returnO = QuartzInstanceHandler.getInstances();
            }

            out.print(returnO);
            out.flush();
            out.close();

/*         HashMap<String, String> map =
      new HashMap<String, String>();
map.put("java.naming.factory.initial",
        RegistryContextFactory.class.getName());
JMXConnector connector = JMXConnectorFactory
      .connect(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:2911/jmxrmi"));
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
connector.close();*/
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }
}

