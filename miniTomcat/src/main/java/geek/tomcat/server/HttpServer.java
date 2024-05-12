package geek.tomcat.server;

/**
 * @Author lnd
 * @Description
 * @Date 2024/1/10 22:04
 */
public class HttpServer {

    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        connector.start();
    }
}

