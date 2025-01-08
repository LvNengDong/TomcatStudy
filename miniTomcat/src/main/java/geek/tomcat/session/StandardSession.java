package geek.tomcat.session;

import geek.tomcat.Session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 16:45
 */
public class StandardSession implements HttpSession, Session {
    @Override
    public void setCreationTime(long time) {

    }

    @Override
    public void setId(String id) {

    }

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
}
