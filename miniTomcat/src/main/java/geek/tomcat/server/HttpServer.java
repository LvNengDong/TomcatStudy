package geek.tomcat.server;

/**
 * @Author lnd
 * @Description
 * @Date 2024/1/10 22:04
 */
public class HttpServer {

    public static void main(String[] args) {
        // 创建connector和container
        HttpConnector connector = new HttpConnector();
        ServletContainer container = new ServletContainer();

        // connector和container互相指引
        connector.setContainer(container);
        container.setConnector(connector);

        connector.start();
    }
}

