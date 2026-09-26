package geek.tomcat.session;

import geek.tomcat.Session;
import geek.tomcat.SessionEvent;
import geek.tomcat.SessionListener;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 16:45
 */
public class StandardSession implements HttpSession, Session {
    @Override
    public void setCreationTime(long time) {

    }
    @Getter@Setter
    private String sessionid;

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void setNew(boolean isNew) {

    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public void setValid(boolean isValid) {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void access() {

    }

    @Override
    public void expire() {

    }

    @Override
    public void recycle() {

    }

    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
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
        return null;
    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public void putValue(String name, Object value) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public void removeValue(String name) {

    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }

    // ==================================================监听器处理
    private transient List<SessionListener> listeners = new ArrayList<>();

    public void addSessionListener(SessionListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeSessionListener(SessionListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public void fireSessionEvent(String type, Object data) {
        if (listeners.size() < 1) return;
        SessionEvent event = new SessionEvent(this, type, data);
        SessionListener list[] = new SessionListener[0];
        synchronized (listeners) {
            list = (SessionListener[]) listeners.toArray(list);
        }
        for (int i = 0; i < list.length; i++) ((SessionListener) list[i]).sessionEvent(event);
    }

    public void setId(String sessionId) {
        this.sessionid = sessionId;
        fireSessionEvent(Session.SESSION_CREATED_EVENT, null);
    }
}
