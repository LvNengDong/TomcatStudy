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
public class HttpProcessor implements Runnable {

    // Connector分配给Processor的Socket对象
    Socket socket;
    // 表示当前是否有需要处理的Socket，初始值为false，表示当前没有需要处理的Socket
    boolean available = false;

    private HttpConnector connector;

    public HttpProcessor(HttpConnector connector) {
        this.connector = connector;
    }


    /* processor线程 */
    @Override
    public void run() {
        while (true) {
            // 等待connector分配一个socket
            Socket socket = await();
            if (socket == null) {
                continue;
            }
            // 处理来自这个socket的请求
            process(socket);

            // 回收processor（处理完毕后将processor交给connector回收，实际就是放回池里）
            connector.recycle(this);
        }
    }

    /**
     * 启动Processor
     */
    public void start() {
        new Thread(this).start();
    }

    public void process(Socket socket) {
        try {
            Thread.sleep(3000); // TODO 为什么

            log.info("HttpProcessor开始处理 本次处理的socket为: {}", socket);
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            // create Request object and parse
            HttpRequest request = new HttpRequest(input);
            request.parse(socket);
            log.info("从socket中解析客户端请求uri: {}", request.getUri());

            // create Response object
            HttpResponse response = new HttpResponse(output);
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

            // 关闭Socket，因为现在Connector和Processor是不同线程执行的，Connector不知道分配给Processor的Socket
            // 什么时候处理完毕，所以将关闭Socket的动作放在了Processor里面，Processor处理完毕后直接关闭Socket即可
            // close the socket
            log.info("请求处理完毕，关闭本次连接对应的socket(非ServerSocket) socket: {}", socket);
            socket.close();
        } catch (Exception e) {
            log.error("http processor occur exception, socket:{}", socket, e);
        }
    }

    /**
     * 虽然 assign() 方法写在了 Processor 类里，但是该方法是由 Connector 线程来执行的，因为它的调用者是Connector
     * Connector 通过这个方法分配一个 Socket 给 Processor
     *
     * @param socket
     */
    synchronized void assign(Socket socket) {
        // 如果标志为true，表示当前Processor正在处理上一个Socket，Connector线程就继续死等，
        // 直到Processor处理完上一个Socket，处理完后Processor会把标志位设置为false，表示有能力处理新的Socket了
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                log.info("TODO 响应中断");
                throw new RuntimeException(e);
            }
        }
        // 存储Connector分配的Socket对象，并通知processor线程处理
        this.socket = socket;
        available = true; // 改变标志位
        notifyAll(); //唤醒等待的Processor，表示分配给Processor的Socket已经被接收了，这样Connector就可以全身而退，去处理下一个Socket了，而不用一直等待分配给Processor的Socket处理完毕。
    }

    /**
     * Processor线程使用的方法
     * 等待 Connector 提供一个新的 Socket
     *
     * @return
     */
    synchronized Socket await() {
        // 当processor发现没有需要处理的Socket时，就会进入阻塞状态
        while (!available) {
            try {
                log.info("当前没有需要处理的Socket，进入阻塞状态 processor:{}", this);
                wait(); // 阻塞
            } catch (InterruptedException e) {
                log.info("响应中断");
                throw new RuntimeException(e);
            }
        }
        // 当processor被分配到Socket后，标志位会改为true，退出循环，继续向下执行
        log.info("当前processor分配到了新的Socket，退出阻塞状态 processor:{} socket:{}", this, this.socket);
        // 获得这个新的Socket
        Socket socket = this.socket;
        // 重新初始化标志位，通知Connector线程可以分配新的Socket连接给Processor了
        available = false;
        // 唤醒Connector线程，因为Connector线程在调用 assign 方法时会陷入阻塞，这里当 Socket 分配成功后，就需要马上调用 notifyAll 唤醒 Connector 线程
        notifyAll();
        return socket;
    }

}
