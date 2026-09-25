package geek.tomcat.core;

import geek.tomcat.*;
import geek.tomcat.connector.HttpRequestFacade;
import geek.tomcat.connector.HttpResponseFacade;
import geek.tomcat.connector.http.HttpRequestImpl;
import geek.tomcat.valves.ValveBase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 15:49
 */
public class StandardContextValve extends ValveBase {
    @Override
    public String getInfo() {
        return "geek.tomcat.core.StandardContextValve";
    }

    private FilterDef filterDef = null;

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext) throws IOException, ServletException {
        StandardWrapper standardWrapper = null;
        String uri = ((HttpRequestImpl) request).getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        String servletClassName = servletName;
        //从容器中获取servlet wrapper
        StandardContext context = (StandardContext) getContainer();
        Wrapper servletWrapper = context.getWrapper(servletName);
        try {
            servletWrapper.invoke(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
