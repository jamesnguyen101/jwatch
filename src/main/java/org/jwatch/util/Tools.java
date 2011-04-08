/*
 
*/
package org.jwatch.util;

import java.util.UUID;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 7, 2011 3:19:10 PM
 */
public class Tools
{
   public static String generateUUID()
   {
      UUID id = UUID.randomUUID();
      return id.toString();
   }

}
