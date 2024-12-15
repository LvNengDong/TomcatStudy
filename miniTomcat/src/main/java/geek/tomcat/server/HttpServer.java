package geek.tomcat.server;

import geek.tomcat.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author lnd
 * @Description
 * @Date 2024/1/10 22:04
 */
@Slf4j
public class HttpServer {


    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    /**
     * 服务器循环等待请求并处理
     */
    public void await() {
        try {
            // 创建 ServerSocket 并监听指定端口
            ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT, Constants.SERVER_BACK_LOG, InetAddress.getByName(Constants.SERVER_HOST));

            while (true) {
                log.info("ServerSocket已启动，等待客户端连接请求");
                // 阻塞等待，直到有有客户端发起连接请求。
                // 当与客户端三次握手成功后，为每一个连接生成一个socket
                Socket socket = serverSocket.accept();
                log.info("ServerSocket与客户端建立连接成功，本次连接的socket为: {}", socket);

                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                // create Request object and parse
                Request request = new Request(input);
                request.parse();
                log.info("客户端请求解析 uri:{}", request.getUri());

                // create Response object
                Response response = new Response(output);
                response.setRequest(request);

                if (request.getUri().startsWith("/servlet/")) {
                    log.info("客户端请求解析 servlet请求 uri:{}", request.getUri());
                    ServletProcessor processor = new ServletProcessor();
                    processor.process(request, response);
                } else {
                    log.info("客户端请求解析 静态文件请求 uri:{}", request.getUri());
                    StatisticResourceProcessor processor = new StatisticResourceProcessor();
                    processor.process(request, response);
                }

                // close the socket
                socket.close();
            }
        } catch (Exception e) {
            log.error("HttpServer exception", e);
            System.exit(1); // 退出JVM进程
        }

    }
}

/*
    1、ServerSocket(int port, int backlog, InetAddress bindAddr)的backlog作用是什么
        在Java中，ServerSocket类的构造函数ServerSocket(int port, int backlog, InetAddress bindAddr)中的backlog参数表示服务器套接字的连接请求队列的最大长度。
        当服务器套接字正在处理连接请求时，如果有新的连接请求到达，但连接请求队列已满，那么新的连接请求将被拒绝。backlog参数就是用来设置连接请求队列的最大长度，它决定了服务器可以同时处理的最大连接数。
        具体来说，当服务器套接字正在处理一个连接请求时，如果有新的连接请求到达，它们会被放入连接请求队列中等待处理。如果连接请求队列已满，新的连接请求将被拒绝。只有当服务器套接字空闲并且连接请求队列未满时，才能接受新的连接请求。
        需要注意的是，backlog参数的实际影响可能会受到操作系统的限制。在某些操作系统中，该参数可能会被忽略或限制为操作系统默认值。
        你可以根据实际情况选择适当的backlog值来平衡服务器的性能和可靠性。

    2、Socket的accept方法会阻塞吗
        是的，Socket的accept方法会阻塞。当调用accept方法时，如果没有新的连接请求到达，该方法会一直阻塞，直到有新的连接请求到达为止。
        只有当有新的连接请求到达时，accept方法才会返回一个新的Socket对象，用于与客户端进行通信。
        在阻塞期间，程序会停止在accept方法处等待，不会执行后续的代码。这种阻塞机制可以保证服务器能够及时响应客户端的连接请求。
 */