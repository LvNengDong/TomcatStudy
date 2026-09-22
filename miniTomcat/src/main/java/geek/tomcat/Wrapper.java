package geek.tomcat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 16:44
 */
 public interface Wrapper {

     int getLoadOnStartup();

     void setLoadOnStartup(int value);

     String getServletClass();

     void setServletClass(String servletClass);

     void addInitParameter(String name, String value);

     Servlet allocate() throws ServletException;

     String findInitParameter(String name);

     String[] findInitParameters();

     void load() throws ServletException;

     void removeInitParameter(String name);
}
