package geek.tomcat.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/26 11:07
 */
@Setter
@Getter
public class ContainerListenerDef {
    private String description = null;
    private String displayName = null;
    private String listenerClass = null;
    private String listenerName = null;
    private Map<String, String> parameters = new ConcurrentHashMap<>();

    public void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }
}
