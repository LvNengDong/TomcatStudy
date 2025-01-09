package geek.tomcat.startup;

import geek.tomcat.Logger;
import geek.tomcat.connector.http.HttpConnector;
import geek.tomcat.core.StandardContext;
import geek.tomcat.logger.FileLogger;


/**
 * @Author lnd
 * @Description
 * @Date 2024/1/10 22:04
 */
public class Bootstrap {

    private static int debug = 0;

    public static void main(String[] args) {
        if (debug >= 1) {
            log(".... startup ....");
        }

        // 创建connector和container
        HttpConnector connector = new HttpConnector();
        StandardContext container = new StandardContext();

        // connector和container互相指引
        connector.setContainer(container);
        container.setConnector(connector);

        Logger logger = new FileLogger();
        container.setLogger(logger); // 我们把 Logger 传给 Container 的原因在于，多个 Container 可以使用不同的 Logger，针对不同的目录和文件进行操作。

        connector.start();
    }

    private static void log(String message) {
        System.out.print("Bootstrap: ");
        System.out.println(message);
    }

    private static void log(String message, Throwable exception) {
        log(message);
        exception.printStackTrace(System.out);
    }
}

