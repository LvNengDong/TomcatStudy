package geek.tomcat.core;

import com.google.common.collect.Lists;
import geek.tomcat.Context;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 16:46
 */

public class ApplicationFilterConfig implements FilterConfig { // javax.servlet.FilterConfig
    @Getter
    @Setter
    private Context context = null;
    @Getter
    @Setter
    private Filter filter = null;
    @Getter
    private FilterDef filterDef = null;

    public ApplicationFilterConfig(Context context, FilterDef filterDef) throws ServletException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        super();
        this.context = context;
        this.setFilterDef(filterDef);
    }

    @Override
    public String getFilterName() {
        return filterDef.getFilterName();
    }

    @Override
    public ServletContext getServletContext() {
        return this.context.getServletContext();
    }

    @Override
    public String getInitParameter(String name) {
        Map<String, String> map = filterDef.getParameterMap();
        return map == null ? null : map.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        Map<String, String> map = filterDef.getParameterMap();
        return MapUtils.isEmpty(map) ? Collections.enumeration(Lists.newArrayList()) : Collections.enumeration(map.keySet());
    }

    Filter getFilter() throws ClassCastException, ClassNotFoundException,
            IllegalAccessException, InstantiationException, ServletException {
        if (this.filter != null) {
            return this.filter;
        }
        // 创建并返回
        String filterClass = filterDef.getFilterClass();
        ClassLoader loader = context.getLoader();
        ClassLoader oldCtxClassLoader = Thread.currentThread().getContextClassLoader();
        Class<?> clazz = loader.loadClass(filterClass);
        this.filter = (Filter) clazz.newInstance();
        filter.init(this);
        return filter;
    }

    void release() {
        if (filter != null) {
            this.filter.destroy();
        }
        this.filter = null;
    }

    void setFilterDef(FilterDef filterDef) throws ClassCastException, ClassNotFoundException, IllegalAccessException, InstantiationException, ServletException {
        this.filterDef = filterDef;
        if (filterDef == null) { // 释放之前分配的所有过滤器实例
            if (this.filter != null) {
                this.filter.destroy();
            }
            this.filter = null;
        } else { // 分配一个新的过滤器实例
            Filter filter = getFilter();
        }
    }
}


