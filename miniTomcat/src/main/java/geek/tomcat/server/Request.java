package geek.tomcat.server;

import lombok.Getter;
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
    @Getter
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    /**
     * 解析请求
     * <br/> 注意：这里最大只解析 2048 byte的数据，超过的数据会被抛弃掉
     */
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer); // 将 input 内容读到 buffer 数组中，i 是读到字符的数量
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]); // 将 buffer 数组转换成 String 字符串
        }
        uri = parseUri(request.toString());
        log.info("解析客户端请求 uri={}", uri);
    }

    /**
     * 解析请求中的uri
     * <br/>
     * HTTP 协议规定，在请求格式第一行的内容中，包含请求方法、请求路径、使用的协议以及版本，用一个空格分开。
     * 下面代码的功能在于，获取传入参数第一行两个空格中的一段，作为请求的 URI。
     * 如果格式稍微有点出入，这个解析就会失败。
     * <br/>例子：GET /hello.txt HTTP/1.1
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

}