package geek.tomcat.server;


import geek.tomcat.Constants;
import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author lnd
 * @Description HTTP协议响应
 * @Date 2024/1/10 22:04
 */
@Data
public class Response {
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }
}