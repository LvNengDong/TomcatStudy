package geek.tomcat.server;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author lnd
 * @Description
 * @Date 2024/5/12 13:38
 */
@Slf4j
public class ServletProcessor {

    private HttpConnector connector;

    public ServletProcessor(HttpConnector connector) {
        this.connector = connector;
    }

    public void process(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        this.connector.getContainer().invoke(request, response);
    }
}
