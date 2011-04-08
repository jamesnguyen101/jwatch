/*
 
*/
package org.jwatch.handler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jwatch.domain.instance.QuartzInstance;
import org.jwatch.domain.instance.QuartzInstanceMap;
import org.jwatch.util.GlobalConstants;

import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 8, 2011 4:31:24 PM
 */
public class QuartzInstanceHandler
{
   /**
    * Returns instances found in memory map
    *
    * @return
    * @see org.jwatch.domain.instance.QuartzInstanceMap
    */
   public static JSONObject getInstances()
   {
      JSONObject jsonObject = new JSONObject();

      Map qMap = QuartzInstanceMap.getQuartzInstanceMap();
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
      }
      return jsonObject;
   }
}
