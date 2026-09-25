package geek.tomcat.core;

import geek.tomcat.Request;
import geek.tomcat.Response;
import geek.tomcat.ValveContext;
import geek.tomcat.connector.HttpRequestFacade;
import geek.tomcat.connector.HttpResponseFacade;
import geek.tomcat.connector.http.HttpRequestImpl;
import geek.tomcat.connector.http.HttpResponseImpl;
import geek.tomcat.valves.ValveBase;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 15:58
 */
public class StandardWrapperValve extends ValveBase {
    @Override
    public String getInfo() {
        return "geek.tomcat.core.StandardWrapperValve";
    }

    @Override
    public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException {
        System.out.println("StandardWrapperValve invoke()");
        HttpServletRequest requestFacade = new HttpRequestFacade((HttpRequestImpl) request);
        HttpServletResponse responseFacade = new HttpResponseFacade((HttpResponseImpl) response);
        Servlet instance = ((StandardWrapper) getContainer()).getServlet();
        if (instance != null) {
            instance.service(requestFacade, responseFacade);
        }
    }
}
