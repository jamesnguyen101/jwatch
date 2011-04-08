/*
 
*/
package org.jwatch.util;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 8, 2011 4:28:10 PM
 */
public class JSONUtil
{
   public static Map<String, String> convertRequestToMap(HttpServletRequest request)
   {
      Map returnMap = new HashMap();
      if (request.getParameterMap() != null)
      {
         for (Iterator it = request.getParameterMap().entrySet().iterator(); it.hasNext();)
         {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = request.getParameter(k);
            returnMap.put(k, v);
         }
      }
      return returnMap;
   }
}
