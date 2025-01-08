package geek.tomcat.startup;

import geek.tomcat.connector.http.HttpConnector;
import geek.tomcat.core.StandardContext;

/**
 * @Author lnd
 * @Description
 * @Date 2024/1/10 22:04
 */
public class Bootstrap {

    public static void main(String[] args) {
        // 创建connector和container
        HttpConnector connector = new HttpConnector();
        StandardContext container = new StandardContext();

        // connector和container互相指引
        connector.setContainer(container);
        container.setConnector(connector);

        connector.start();
    }
}

