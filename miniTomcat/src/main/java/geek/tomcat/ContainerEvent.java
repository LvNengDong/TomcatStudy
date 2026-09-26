package geek.tomcat;

import java.util.EventObject;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 17:49
 */
public final class ContainerEvent extends EventObject {
    private Container container = null;
    private Object data = null;
    private String type = null;

    public ContainerEvent(Container container, String type, Object data) {
        super(container);
        this.container = container;
        this.type = type;
        this.data = data;
    }

    public Object getData() {
        return (this.data);
    }

    public Container getContainer() {
        return (this.container);
    }

    public String getType() {
        return (this.type);
    }

}
