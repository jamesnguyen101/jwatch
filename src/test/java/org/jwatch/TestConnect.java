/*
 
*/
package test.java.org.jwatch;

import org.jwatch.domain.connection.QuartzConnectService;
import org.jwatch.domain.connection.QuartzConnectServiceImpl;
import org.jwatch.domain.connection.QuartzConnectUtil;
import org.jwatch.domain.instance.QuartzInstance;
import org.jwatch.listener.settings.QuartzConfig;
import org.jwatch.listener.notification.Listener;
import org.jwatch.util.Tools;

import javax.management.openmbean.TabularData;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.List;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 13, 2011 3:29:05 PM
 */
public class TestConnect
{
   public static void main(String[] args)
   {
      try
      {
         QuartzConfig config = new QuartzConfig(Tools.generateUUID(), "localhost", 2911, null, null);
         QuartzConnectService quartzConnectService = new QuartzConnectServiceImpl();
         QuartzInstance quartzInstance = quartzConnectService.initInstance(config);
         System.out.println(quartzInstance);
         QuartzConnectUtil.printMBeanProperties(quartzInstance);

         MBeanServerConnection connection = quartzInstance.getMBeanServerConnection();
         ObjectName objectName = quartzInstance.getObjectName();

         Listener listener = new Listener();
         connection.addNotificationListener(objectName, listener, null, null);

         List groupNames = (List) connection.getAttribute(objectName, "JobGroupNames");
         TabularData cdata = (TabularData) connection.getAttribute(objectName, "CurrentlyExecutingJobs");
         TabularData jdata = (TabularData) connection.getAttribute(objectName, "AllJobDetails");
         List tdata = (List) connection.getAttribute(objectName, "AllTriggers");
         String sid = (String) connection.getAttribute(objectName, "SchedulerInstanceId");
         for (int i = 0; i < groupNames.size(); i++)
         {
            String groupName = (String) groupNames.get(i);
            //List<String> jobNames = (List) connection.getAttribute(objectName, "getJobNames");
            //Object jmxResult = connection.invoke(objectName, "getAllJobDetails", new Object[] { sid }, new String[] { String.class.getName() });
/*            for (int j = 0; j < jobNames.size(); j++)
            {
               String jobName = jobNames.get(j);
               CompositeData data = (CompositeData) connection.getAttribute(objectName, "getJobDetail");

               System.out.println();
            }*/
            System.out.println();
         }
      }
      catch (Throwable t)
      {
         t.printStackTrace();
      }
   }

}
