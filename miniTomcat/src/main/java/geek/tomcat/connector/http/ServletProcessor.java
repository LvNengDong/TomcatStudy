package geek.tomcat.connector.http;

import geek.tomcat.connector.http.HttpConnector;
import geek.tomcat.connector.http.HttpRequestImpl;
import geek.tomcat.connector.http.HttpResponseImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author lnd
 * @Description
 * @Date 2024/5/12 13:38
 */
@Slf4j
public class ServletProcessor {

    private HttpConnector connector;

    public ServletProcessor(HttpConnector connector) {
        this.connector = connector;
    }

    public void process(HttpRequestImpl request, HttpResponseImpl response) throws IOException, ServletException {
        this.connector.getContainer().invoke(request, response);
    }

    //public void process(HttpRequest request, HttpResponse response) {
    //    String servletName = null;
    //    try {
    //        // 首先根据uri最后一个/号来定位，后面的字符串认为是servlet名字(全限定类名)
    //        String uri = request.getUri();
    //        servletName = uri.substring(uri.lastIndexOf("/") + 1);
    //
    //        // 加载 servlet class
    //        Class<?> servletClass = ClassLoaderUtil.loadClassByDir(Constants.WEB_ROOT, servletName);
    //
    //        response.setCharacterEncoding(Constants.UTF_8);
    //        // 写响应头
    //        response.sendHeaders();
    //
    //        // 反射创建 servlet 实例，并执行 service() 方法
    //        Servlet servlet = (Servlet) servletClass.newInstance();
    //        HttpRequestFacade requestFacade = new HttpRequestFacade(request);
    //        HttpResponseFacade responseFacade = new HttpResponseFacade(response);
    //        servlet.service(requestFacade, responseFacade);
    //    } catch (Exception e) {
    //        log.error("ServletProcessor process exception servletName:{}", servletName, e);
    //    }
    //}
}
