package geek.tomcat;

import java.util.EventObject;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 21:22
 */
public final class SessionEvent extends EventObject {
    private Object data = null;
    private Session session = null;
    private String type = null;
    public SessionEvent(Session session, String type, Object data) {
        super(session);
        this.session = session;
        this.type = type;
        this.data = data;
    }

    public Object getData() {
        return (this.data);
    }

    public Session getSession() {
        return (this.session);
    }

    public String getType() {
        return (this.type);
    }

    public String toString() {
        return ("SessionEvent['" + getSession() + "','" +
                getType() + "']");
    }
}

