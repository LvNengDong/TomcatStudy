package geek.tomcat.core;

import geek.tomcat.Container;
import geek.tomcat.Wrapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/3 17:16
 */
public class StandardWrapper extends ContainerBase implements Wrapper {

    // wrapper内含了一个servlet实例和类
    private Servlet instance = null;
    private String servletClass;

    protected StandardContext parent = null;

    public StandardWrapper(String servletClass, StandardContext parent) {
        this.parent = parent;
        this.servletClass = servletClass;
        try {
            loadServlet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getInfo() {
        return null;
    }

    public ClassLoader getLoader() {
        if (Objects.nonNull(loader)) {
            return loader;
        }
        return parent.getLoader();
    }

    @Override
    public int getLoadOnStartup() {
        return 0;
    }

    @Override
    public void setLoadOnStartup(int value) {

    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    @Override
    public void addInitParameter(String name, String value) {

    }

    @Override
    public Servlet allocate() throws ServletException {
        return null;
    }

    @Override
    public String findInitParameter(String name) {
        return null;
    }

    @Override
    public String[] findInitParameters() {
        return new String[0];
    }

    @Override
    public void load() throws ServletException {

    }

    @Override
    public void removeInitParameter(String name) {

    }

    public Container getParent() {
        return parent;
    }

    public void setParent(StandardContext container) {
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
