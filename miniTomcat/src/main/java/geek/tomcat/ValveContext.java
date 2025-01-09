package geek.tomcat;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/9 15:48
 */
public interface ValveContext {
    public String getInfo();

    public void invokeNext(Request request, Response response) throws IOException, ServletException;
}
