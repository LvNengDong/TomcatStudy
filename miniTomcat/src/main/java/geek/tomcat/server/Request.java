package geek.tomcat.server;

import geek.tomcat.util.Logger;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author lnd
 * @Description HTTP协议请求
 * @Date 2024/1/10 22:04
 */
@Slf4j
public class Request {

    private InputStream input;
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    /**
     * 解析请求
     */
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer); //将input内容转换成byte数组
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]); //将byte数组转换成String字符串
        }
        uri = parseUri(request.toString());
    }

    /**
     * 解析请求中的uri
     *      HTTP 协议规定，在请求格式第一行的内容中，包含请求方法、请求路径、使用的协议以及版本，用一个空格分开。
     *      上述代码的功能在于，获取传入参数第一行两个空格中的一段，作为请求的 URI。
     *      如果格式稍微有点出入，这个解析就会失败。
     *      例子：GET /hello.txt HTTP/1.1
     * @param requestString
     * @return
     */
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }
}