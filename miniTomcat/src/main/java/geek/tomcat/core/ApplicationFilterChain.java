package geek.tomcat.core;

import geek.tomcat.connector.HttpRequestFacade;
import geek.tomcat.connector.HttpResponseFacade;
import geek.tomcat.connector.http.HttpRequestImpl;
import geek.tomcat.connector.http.HttpResponseImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 17:13
 */
public class ApplicationFilterChain implements FilterChain { // javax.servlet.FilterChain

    public ApplicationFilterChain() {
        super();
    }

    private List<ApplicationFilterConfig> filters = new ArrayList<>();

    private Iterator<ApplicationFilterConfig> iterator = null;

    private Servlet servlet = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        System.out.println("FilterChain doFilter()");
        try {
            internalDoFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void internalDoFilter(ServletRequest request, ServletResponse response) throws Exception {
        if (this.iterator == null) {
            this.iterator = filters.iterator();
        }

        if (this.iterator.hasNext()) {
            ApplicationFilterConfig filterConfig = iterator.next();
            Filter filter = null;
            try {
                // 进行过滤，这是职责链模式，一个一个往下传
                filter = filterConfig.getFilter();
                System.out.println("Filter doFilter()");
                filter.doFilter(request, response, this);
            } catch (Exception e) {
                throw e;
            }
            return;
        }

        // 最后调用servlet
        try {
            HttpServletRequest requestFacade = new HttpRequestFacade((HttpRequestImpl) request);
            HttpServletResponse responseFacade = new HttpResponseFacade((HttpResponseImpl) response);
            servlet.service(requestFacade, responseFacade);
        } catch (Throwable e) {
            throw new ServletException("filterChain.servlet", e);
        }
    }

    void addFilter(ApplicationFilterConfig filterConfig) {
        this.filters.add(filterConfig);
    }

    void release() {
        this.filters.clear();
        this.iterator = iterator;
        this.servlet = null;
    }

    void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }
}
