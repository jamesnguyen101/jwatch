/*
 
*/
package org.jwatch.domain.instance;

/**
 * @author <a href="mailto:royrusso@gmail.com">Roy Russo</a>
 *         Date: Apr 6, 2011 4:57:14 PM
 */
public class QuartzInstance
{
   private String uuid;
   private String host;
   private int port;
   private String userName;
   private String password;

   public QuartzInstance(String uuid, String host, int port, String userName, String password)
   {
      this.uuid = uuid;
      this.host = host;
      this.port = port;
      this.userName = userName;
      this.password = password;
   }

   public String getUuid()
   {
      return uuid;
   }

   public void setUuid(String uuid)
   {
      this.uuid = uuid;
   }

   public String getHost()
   {
      return host;
   }

   public void setHost(String host)
   {
      this.host = host;
   }

   public int getPort()
   {
      return port;
   }

   public void setPort(int port)
   {
      this.port = port;
   }

   public String getUserName()
   {
      return userName;
   }

   public void setUserName(String userName)
   {
      this.userName = userName;
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }@Override
    public String toString()
{
   return "QuartzInstance{" +
          "uuid='" + uuid + '\'' +
          ", host='" + host + '\'' +
          ", port=" + port +
          ", userName='" + userName + '\'' +
          ", password='" + password + '\'' +
          '}';
}
}
