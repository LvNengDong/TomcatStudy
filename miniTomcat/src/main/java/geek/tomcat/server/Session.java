package geek.tomcat.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/2 17:07
 */
public class Session implements HttpSession {
    private String sessionId;
    private long creationTime;
    private boolean valid;
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(this.attributes.keySet());
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        this.attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    @Override
    public void removeValue(String name) {

    }

    @Override
    public void invalidate() {
        this.valid = false;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public void setValid(boolean b) {
        this.valid = b;
    }

    public void setCreationTime(long currentTimeMillis) {
        this.creationTime = currentTimeMillis;
    }

    public void setId(String sessionId) {
        this.sessionId = sessionId;
    }
}