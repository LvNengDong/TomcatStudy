package geek.tomcat.server;

import java.io.IOException;

/**
 * @Author lnd
 * @Description
 * @Date 2024/5/12 13:38
 */
public interface Servlet {
    public void service(Request req, Response res) throws IOException;
}
