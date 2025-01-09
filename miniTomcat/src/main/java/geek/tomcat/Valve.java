package geek.tomcat;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/9 15:47
 */
public interface Valve {
    public String getInfo();

    public Container getContainer();

    public void setContainer(Container container);

    /**
     *
     * @param request
     * @param response
     * @param context  ValveContext 接口负责调用下一个 Valve
     * @throws IOException
     * @throws ServletException
     */
    public void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException;
}
