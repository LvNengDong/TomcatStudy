package geek.tomcat;

import javax.servlet.http.HttpSession;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 17:32
 */
 public interface Session {
     static final String SESSION_CREATED_EVENT = "createSession";
     static final String SESSION_DESTROYED_EVENT = "destroySession";
     long getCreationTime();
     void setCreationTime(long time);
     String getId();
     void setId(String id);
     String getInfo();
     long getLastAccessedTime();
     int getMaxInactiveInterval();
     void setMaxInactiveInterval(int interval);
     void setNew(boolean isNew);
     HttpSession getSession();
     void setValid(boolean isValid);
     boolean isValid();
     void access();
     void expire();
     void recycle();
}
