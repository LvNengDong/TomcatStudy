package geek.tomcat;

import javax.servlet.http.HttpSession;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 17:32
 */
public interface Session {
    public static final String SESSION_CREATED_EVENT = "createSession";
    public static final String SESSION_DESTROYED_EVENT = "destroySession";
    public long getCreationTime();
    public void setCreationTime(long time);
    public String getId();
    public void setId(String id);
    public String getInfo();
    public long getLastAccessedTime();
    public int getMaxInactiveInterval();
    public void setMaxInactiveInterval(int interval);
    public void setNew(boolean isNew);
    public HttpSession getSession();
    public void setValid(boolean isValid);
    public boolean isValid();
    public void access();
    public void expire();
    public void recycle();
}
