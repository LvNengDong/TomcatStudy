package geek.tomcat;

import java.io.File;

/**
 * @Author lnd
 * @Description 常量
 * @Date 2024/5/9 22:23
 */
public class Constants {
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    public static final String SERVER_HOST = "127.0.0.1";
    public static final int SERVER_PORT = 8080;
    /**
     * TCP 连接建立要经过三次握手。操作系统内核会维护两个队列：
     *      SYN 队列（半连接队列）：存放只收到 SYN、还没完成三次握手的请求
     *      Accept 队列（已完成队列）：三次握手全部完成，等待应用程序调用 accept() 取走的连接
     *
     * backlog 参数设置的就是 Accept 队列的最大长度。
     */
    public static final int SERVER_BACK_LOG = 1;

    // 下面的字符串是当文件没有找到时返回的 404 错误描述
    public static String fileNotFoundMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";

    // 下面的字符串是正常情况下返回的，根据http协议，里面包含了相应的变量。
    public static String OKMessage = "HTTP/1.1 ${StatusCode} ${StatusName}\r\n" + "Content-Type: ${ContentType}\r\n" + "Content-Length: ${ContentLength}\r\n" + "Server: minit\r\n" + "Date: ${ZonedDateTime}\r\n" + "\r\n";

    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        System.out.println("当前工作目录：" + currentDir); // /Volumes/DEV/code/TomcatStudy/miniTomcat
        System.out.println(WEB_ROOT); // /Volumes/DEV/code/TomcatStudy/miniTomcat/webroot
    }


}
