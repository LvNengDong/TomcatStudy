package geek.tomcat.server;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author lnd
 * @Description Servlet 的容器，用于管理 Servlet
 * @Date 2025/1/3 17:16
 */
@Getter
@Setter
public class ServletWrapper {

    private Servlet instance = null;

    private String servletClass;

    private ClassLoader loader;

    private String name;
    /**
     * ServletWrapper 里没有 setLoader()，loader 字段永远是 null，于是 getLoader() 每次都落到 parent.getLoader()，拿到的是 ServletContainer 构造时建好的那个指向 WEB_ROOT 的 URLClassLoader。
     *  原则：「自己有就用自己的，没有就问父容器」
     */
    protected ServletContainer parent = null; // 当前唯一生效的作用是向 parent 借 ClassLoader

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


    public Servlet getServlet() {
        return this.instance;
    }

    private Servlet loadServlet() throws ServletException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (Objects.nonNull(instance)) {
            return instance;
        }
        Servlet servlet;
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
