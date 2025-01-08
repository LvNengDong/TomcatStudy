package geek.tomcat.server;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/3 17:16
 */
public class ServletWrapper {

    private Servlet instance = null;

    private String servletClass;

    private ClassLoader loader;

    private String name;

    protected ServletContainer parent = null;

    public ServletWrapper(String servletClass, ServletContainer parent) {
        this.parent = parent;
        this.servletClass = servletClass;
        try {
            loadServlet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ClassLoader getLoader() {
        if (Objects.nonNull(loader)) {
            return loader;
        }
        return parent.getLoader();
    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    public ServletContainer getParent() {
        return parent;
    }

    public void setParent(ServletContainer container) {
        parent = container;
    }

    public Servlet getServlet() {
        return this.instance;
    }

    private Servlet loadServlet() throws ServletException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (Objects.nonNull(instance)) {
            return instance;
        }
        Servlet servlet = null;
        String actualClass = servletClass;
        if (StringUtils.isEmpty(actualClass)) {
            throw new ServletException("servlet class has not been specified");
        }
        ClassLoader classLoader = getLoader();
        Class clazz = null;
        if (Objects.nonNull(classLoader)) {
            clazz = classLoader.loadClass(actualClass);
            servlet = (Servlet) clazz.newInstance();
            servlet.init(null);
            instance = servlet;
            return servlet;
        }
        return null;
    }

    public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (instance != null) {
            instance.service(request, response);
        }
    }

}
