package geek.tomcat.server;

import geek.tomcat.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lnd
 * @Description Servlet容器，负责后端 Servlet 管理
 * 1、创建（懒加载）
 * 2、销毁
 * @Date 2025/1/3 17:16
 */
@Getter
@Setter
public class ServletContainer {

    HttpConnector connector = null;

    ClassLoader loader = null;  // 通用（全局的）类加载器

    Map<String, String> servletClsMap = new ConcurrentHashMap<>(); // servletName - ServletClassName
    Map<String, ServletWrapper> servletInstanceMap = new ConcurrentHashMap<>(); // servletName - ServletWrapper

    public ServletContainer() {
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 从map中找到相关的servlet，然后调用
     */
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        ServletWrapper servletWrapper = null;
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        String servletClassName = servletName;
        servletWrapper = servletInstanceMap.get(servletName);
        try {
            if (Objects.isNull(servletWrapper)) {
                // 如果容器中没有这个servlet，先要load类，创建新实例
                servletWrapper = new ServletWrapper(servletClassName, this);
                servletClsMap.put(servletName, servletClassName);
                servletInstanceMap.put(servletName, servletWrapper);
            }
            // 然后调用 service()
            HttpRequestFacade requestFacade = new HttpRequestFacade(request);
            HttpResponseFacade responseFacade = new HttpResponseFacade(response);
            servletWrapper.invoke(requestFacade, responseFacade);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
