package geek.tomcat.test;

/**
 * @Author lnd
 * @Description
 * @Date 2024/5/12 21:26
 */
import geek.tomcat.Constants;
import geek.tomcat.server.Request;
import geek.tomcat.server.Response;
import geek.tomcat.server.Servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HelloServlet implements Servlet {
    @Override
    public void service(Request req, Response res) throws IOException {
        String doc = "<!DOCTYPE html> \n" +
                "<html>\n" +
                "<head><meta charset=\"utf-8\"><title>Test</title></head>\n"+
                "<body bgcolor=\"#f0f0f0\">\n" +
                "<h1 align=\"center\">" + "Hello World 你好" + "</h1>\n";
        res.getOutput().write(doc.getBytes(StandardCharsets.UTF_8));
    }
}
