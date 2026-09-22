package geek.tomcat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * @Author lnd
 * @Description 一个 Servlet
 * 方法几乎就是 web.xml 里 <servlet> 标签的运行时化身：
 * @Date 2025/1/8 16:44
 */
public interface Wrapper extends Container {

    String getServletClass();

    void setServletClass(String servletClass);

    void addInitParameter(String name, String value);

    String findInitParameter(String name);

    String[] findInitParameters();

    void removeInitParameter(String name);

    int getLoadOnStartup();

    void setLoadOnStartup(int value);


    /**
     * 加载类并 init
     */
    void load() throws ServletException;

    /**
     * 取一个可用实例
     */
    Servlet allocate() throws ServletException;
}
