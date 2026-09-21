package geek.tomcat.server;

/**
 * @Author lnd
 * @Description
 * @Date 2024/1/10 22:04
 */
public class HttpServer {

    public static void main(String[] args) {
        // 创建connector和container（connector负责接收客户端请求，container负责处理请求并返回响应）
        HttpConnector connector = new HttpConnector();
        ServletContainer container = new ServletContainer();

        // connector和container互相引用
        connector.setContainer(container);
        container.setConnector(connector);

        connector.start();
    }
}

