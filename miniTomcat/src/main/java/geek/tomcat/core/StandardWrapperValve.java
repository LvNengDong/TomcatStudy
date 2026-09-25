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

    private FilterDef filterDef = null;

    @Override
    public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException {
        System.out.println("StandardWrapperValve invoke()");
        //创建filter Chain，再调用filter，然后调用servlet
        Servlet instance = ((StandardWrapper) getContainer()).getServlet();
        ApplicationFilterChain filterChain = createFilterChain(request, instance);
        HttpServletRequest requestFacade = new HttpRequestFacade((HttpRequestImpl) request);
        HttpServletResponse responseFacade = new HttpResponseFacade((HttpResponseImpl) response);

        if (instance != null) {
            instance.service(requestFacade, responseFacade);
        }
    }

    // 根据context中的filter map信息挑选出符合模式的filter，创建filterChain
    private ApplicationFilterChain createFilterChain(Request request, Servlet servlet) {
        System.out.println("createFilterChain()");
        if (servlet == null) {
            return null;
        }
        ApplicationFilterChain filterChain = new ApplicationFilterChain();
        filterChain.setServlet(servlet);
        StandardWrapper wrapper = (StandardWrapper) getContainer();
        StandardContext context = (StandardContext) wrapper.getParent();
        FilterMap filterMaps[] = context.findFilterMaps();
        if ((filterMaps == null) || (filterMaps.length == 0))
            return (filterChain);
        String requestPath = null;
        if (request instanceof HttpServletRequest) {
            String contextPath = "";
            String requestURI = ((HttpRequestImpl) request).getUri(); //((HttpServletRequest) request).getRequestURI();
            if (requestURI.length() >= contextPath.length())
                requestPath = requestURI.substring(contextPath.length());
        }
        String servletName = wrapper.getName();
        int n = 0;
        for (int i = 0; i < filterMaps.length; i++) {
            if (!matchFiltersURL(filterMaps[i], requestPath))
                continue;
            ApplicationFilterConfig filterConfig = (ApplicationFilterConfig)
                    context.findFilterConfig(filterMaps[i].getFilterName());
            if (filterConfig == null) {
                continue;
            }
            filterChain.addFilter(filterConfig);
            n++;
        }

        for (int i = 0; i < filterMaps.length; i++) {
            if (!matchFiltersServlet(filterMaps[i], servletName))
                continue;
            ApplicationFilterConfig filterConfig = (ApplicationFilterConfig)
                    context.findFilterConfig(filterMaps[i].getFilterName());
            if (filterConfig == null) {
                continue;
            }
            filterChain.addFilter(filterConfig);
            n++;
        }

        return (filterChain);

    }

    private boolean matchFiltersURL(FilterMap filterMap, String requestPath) {
        if (requestPath == null)
            return (false);
        String testPath = filterMap.getUrlPattern();
        if (testPath == null)
            return (false);
        if (testPath.equals(requestPath))
            return (true);
        if (testPath.equals("/*"))
            return (true);
        if (testPath.endsWith("/*")) {
            String comparePath = requestPath;
            while (true) {
                if (testPath.equals(comparePath + "/*"))
                    return (true);
                int slash = comparePath.lastIndexOf('/');
                if (slash < 0)
                    break;
                comparePath = comparePath.substring(0, slash);
            }
            return (false);
        }
        if (testPath.startsWith("*.")) {
            int slash = requestPath.lastIndexOf('/');
            int period = requestPath.lastIndexOf('.');
            if ((slash >= 0) && (period > slash))
                return (testPath.equals("*." + requestPath.substring(period + 1)));
        }
        return (false); // NOTE - Not relevant for selecting filters
    }

    private boolean matchFiltersServlet(FilterMap filterMap, String servletName) {
        if (servletName == null)
            return (false);
        else
            return (servletName.equals(filterMap.getServletName()));
    }
}
