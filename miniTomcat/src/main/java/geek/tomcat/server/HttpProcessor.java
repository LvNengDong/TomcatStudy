package geek.tomcat.server;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author lnd
 * @Description
 * @Date 2024/5/12 22:22
 */
@NoArgsConstructor
@Slf4j
public class HttpProcessor {

    public void process(Socket socket) {
        try {
            log.info("HttpProcessor开始处理 本次处理的socket为: {}", socket);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            // create Request object and parse
            Request request = new Request(input);
            request.parse();
            log.info("从socket中解析客户端请求uri: {}", request.getUri());

            // create Response object
            Response response = new Response(output);
            response.setRequest(request);

            if (request.getUri().startsWith("/servlet/")) {
                log.info("从socket中解析客户端请求_servlet请求_uri:{}", request.getUri());
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            } else {
                log.info("客户端请求解析_静态文件请求_uri:{}", request.getUri());
                StatisticResourceProcessor processor = new StatisticResourceProcessor();
                processor.process(request, response);
            }
        } catch (Exception e) {
            log.error("http processor occur exception, socket:{}", socket, e);
        }
    }
}
