package geek.tomcat.server;

import geek.tomcat.Constants;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author lnd
 * @Description HttpConnector 实现了 Runnable 接口，把它看作一个线程，支持并发处理，提高整个服务器的吞吐量
 * @Date 2024/5/12 22:22
 */
@Slf4j
public class HttpConnector implements Runnable {

    int minProcessors = 3;
    int maxProcessors = 10;
    int curProcessors = 0;
    //存放多个processor的池子
    Deque<HttpProcessor> processors = new ArrayDeque<>();

    @Override
    public void run() {
        try {
            // 创建 ServerSocket 并监听指定端口
            ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT,
                    Constants.SERVER_BACK_LOG,
                    InetAddress.getByName(Constants.SERVER_HOST));

            log.info("初始化processors start");
            for (int i = 0; i < minProcessors; i++) {
                HttpProcessor processor = new HttpProcessor();
                processors.push(processor);
            }
            curProcessors = minProcessors;
            log.info("初始化processors 当前处理器池中的处理器数量 curProcessorSize:{}", curProcessors);
            log.info("初始化processors end");

            while (true) {
                log.info("启动ServerSocket，等待客户端连接请求");
                // 阻塞等待，直到有有客户端发起连接请求。
                // 当与客户端三次握手成功后，分配给当前连接一个socket
                Socket socket = serverSocket.accept();
                log.info("ServerSocket与客户端建立连接成功，分配一个socket给当前连接 socket: {}", socket);
                HttpProcessor processor = getProcessor();
                if (processor == null) {
                    log.info("获取processor失败，关闭当前连接 socket:{}", socket);
                    socket.close();
                    continue;
                }
                // 处理
                processor.process(socket);
                // 处理完毕后归还processor
                processors.push(processor);
                // close the socket
                log.info("请求处理完毕，关闭本次连接对应的socket(非ServerSocket) socket: {}", socket);
                socket.close();
            }
        } catch (Exception e) {
            log.error("http connector occur exception, close JVM processor", e);
            System.exit(1); // 退出JVM进程
        }
    }

    /**
     * 从池子中获取一个processor，如果池子为空且小于最大限制，则新建一个
     * @return
     */
    private HttpProcessor getProcessor() {
        if (!processors.isEmpty()) { // 不为空直接取
            return processors.pop();
        } else if (curProcessors < maxProcessors) { // 为空&&允许创建，则创建一个
            return new HttpProcessor();
        } else {
            return null;
        }
    }

    public void start() {
        // this 是一个 Runnable 任务
        Thread thread = new Thread(this);
        thread.start();
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