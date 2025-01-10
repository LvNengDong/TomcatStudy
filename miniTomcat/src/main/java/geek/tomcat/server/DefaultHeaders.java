package geek.tomcat.server;

/**
 * @Author lnd
 * @Description
 *
 * @Date 2024/12/15 22:37
 */
public class DefaultHeaders {
    /*
        GET /servlet/HelloServlet HTTP/1.1
        User-Agent: PostmanRuntime-ApipostRuntime/1.1.0
        Cache-Control: no-cache
        Accept-Encoding: gzip, deflate, br
        Connection: keep-alive
        Host: localhost:8080
    */
    static final String HOST_NAME = "host";
    static final String CONNECTION_NAME = "connection";
    static final String ACCEPT_LANGUAGE_NAME = "accept-language";
    static final String CONTENT_LENGTH_NAME = "content-length";
    static final String CONTENT_TYPE_NAME = "content-type";
    static final String TRANSFER_ENCODING_NAME = "transfer-encoding";
}
