package geek.tomcat.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 16:40
 */
@Getter
@Setter
public final class FilterDef {
    private String description = null;

    private String displayName = null;

    private String filterClass = null;

    private String filterName = null;

    private String largeIcon = null;

    private Map<String, String> parameterMap = new ConcurrentHashMap<>();

    private String smallIcon = null;

    public void addInitParameter(String name, String value) {
        parameterMap.put(name, value);
    }
}
