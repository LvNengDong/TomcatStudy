package geek.tomcat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 16:44
 */
public interface Wrapper {

    public int getLoadOnStartup();

    public void setLoadOnStartup(int value);

    public String getServletClass();

    public void setServletClass(String servletClass);

    public void addInitParameter(String name, String value);

    public Servlet allocate() throws ServletException;

    public String findInitParameter(String name);

    public String[] findInitParameters();

    public void load() throws ServletException;

    public void removeInitParameter(String name);
}
