package geek.tomcat;

import javax.servlet.ServletContext;

/**
 * @Author lnd
 * @Description 一个 Web 应用
 * @Date 2025/1/8 16:44
 */
public interface Context extends Container {
    String RELOAD_EVENT = "reload";

    String getDisplayName();

    void setDisplayName(String displayName);

    String getDocBase();

    void setDocBase(String docBase);

    String getPath();

    void setPath(String path);

    ServletContext getServletContext();

    int getSessionTimeout();

    void setSessionTimeout(int timeout);

    // ================================================== Context 负责生产自己的子容器，而且可以配置用哪个 Wrapper 实现类
    String getWrapperClass();

    void setWrapperClass(String wrapperClass);

    Wrapper createWrapper();

    // ================================================== url-pattern → servletName 的映射，对应 web.xml 的 <servlet-mapping>
    String findServletMapping(String pattern);

    String[] findServletMappings();


    void reload();
}
