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
            log.info("socket process start, socket={}", socket);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            // create Request object and parse
            Request request = new Request(input);
            request.parse();

            // create Response object
            Response response = new Response(output);
            response.setRequest(request);

            if (request.getUri().startsWith("/servlet/")) {
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            } else {
                StatisticResourceProcessor processor = new StatisticResourceProcessor();
                processor.process(request, response);
            }
        } catch (Exception e) {
            log.error("socket process exception, socket={}", socket, e);
        }
    }
}
