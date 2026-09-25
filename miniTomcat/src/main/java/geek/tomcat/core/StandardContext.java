package geek.tomcat.core;

import geek.tomcat.*;
import geek.tomcat.connector.HttpRequestFacade;
import geek.tomcat.connector.HttpResponseFacade;
import geek.tomcat.connector.http.HttpConnector;
import geek.tomcat.connector.http.HttpRequestImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author lnd
 * @Description Servlet容器
 * @Date 2025/1/3 17:16
 */
public class StandardContext extends ContainerBase implements Context {

    HttpConnector connector = null;
    ClassLoader loader = null;  // 类加载器（构造方法中提供了默认的类加载器实现，同时也支持用户端使用 setLoader 方法设置自定义的类加载器）

    // 包含servlet类和实例的map
    Map<String, String> servletClsMap = new ConcurrentHashMap<>(); // servletName - ServletClassName
    Map<String, StandardWrapper> servletInstanceMap = new ConcurrentHashMap<>(); // servletName - servlet

    public StandardContext() {
        super();
        pipeline.setBasic(new StandardContextValve());
        try {
            // create a URLClassLoader
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        log("Container created.");
    }

    public String getInfo() {
        return "Minit Servlet Context, version 0.1";
    }

    public ClassLoader getLoader() {
        return this.loader;
    }

    public void setLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public HttpConnector getConnector() {
        return connector;
    }

    public void setConnector(HttpConnector connector) {
        this.connector = connector;
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {
    }

    /**
     * 从map中找到相关的servlet，然后调用
     */
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        super.invoke(request, response);
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String displayName) {

    }

    @Override
    public String getDocBase() {
        return null;
    }

    @Override
    public void setDocBase(String docBase) {

    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String path) {

    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int timeout) {

    }

    @Override
    public String getWrapperClass() {
        return null;
    }

    @Override
    public void setWrapperClass(String wrapperClass) {

    }

    public Wrapper getWrapper(String name){
        StandardWrapper servletWrapper = servletInstanceMap.get(name);
        if ( servletWrapper == null) {
            String servletClassName = name;
            servletWrapper = new StandardWrapper(servletClassName,this);
            this.servletClsMap.put(name, servletClassName);
            this.servletInstanceMap.put(name, servletWrapper);
        }
        return servletWrapper;
    }

    @Override
    public Wrapper createWrapper() {
        return null;
    }

    @Override
    public String findServletMapping(String pattern) {
        return null;
    }

    @Override
    public String[] findServletMappings() {
        return new String[0];
    }

    @Override
    public void reload() {

    }
}
