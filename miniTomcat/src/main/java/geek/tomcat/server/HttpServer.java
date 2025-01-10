package geek.tomcat.server;

import geek.tomcat.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author lnd
 * @Description
 * @Date 2024/1/10 22:04
 */
@Slf4j
public class HttpServer {

    public static void main(String[] args) {
        log.info("Server start, time={}, threadName={}", new Date(), ThreadUtil.getCurThreadName());
        HttpConnector connector = new HttpConnector();
        connector.start();
    }
}

