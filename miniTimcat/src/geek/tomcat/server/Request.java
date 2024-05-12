package geek.tomcat.server;

import geek.tomcat.util.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author lnd
 * @Description HTTP协议请求
 * @Date 2024/1/10 22:04
 */
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
            // 从输入流中读取数据，并将读取的数据存储到指定的字节数组中
            i = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        uri = parseUri(request.toString());
        Logger.info("request parse result request:{0} uri:{1}", request, uri);
    }

    /**
     * 解析请求中的uri
     *
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