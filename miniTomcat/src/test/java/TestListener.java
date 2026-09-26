import geek.tomcat.ContainerEvent;
import geek.tomcat.ContainerListener;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/26 11:39
 */
public class TestListener implements ContainerListener {
    @Override
    public void containerEvent(ContainerEvent event) {
        System.out.println(event);
    }
}
