package geek.tomcat;

import javax.servlet.ServletContext;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 16:44
 */
public interface Context extends Container {
     static final String RELOAD_EVENT = "reload";

     String getDisplayName();

     void setDisplayName(String displayName);

     String getDocBase();

     void setDocBase(String docBase);

     String getPath();

     void setPath(String path);

     ServletContext getServletContext();

     int getSessionTimeout();

     void setSessionTimeout(int timeout);

     String getWrapperClass();

     void setWrapperClass(String wrapperClass);

     Wrapper createWrapper();

     String findServletMapping(String pattern);

     String[] findServletMappings();

     void reload();
}
