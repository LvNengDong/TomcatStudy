package geek.tomcat.server;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author lnd
 * @Description 自定义了 SocketInputStream 类，它扩展了 Java 标准库中的 InputStream 类，用于从 socket 连接中读取数据并将其缓冲到一个字节数组中。
 * @Date 2024/12/15 22:38
 */
public class SocketInputStream extends InputStream {
    private static final byte CR = (byte) '\r'; // 回车
    private static final byte LF = (byte) '\n'; // 换行
    private static final byte SP = (byte) ' ';
    private static final byte HT = (byte) '\t'; // Tab，制表符
    private static final byte COLON = (byte) ':';
    private static final int LC_OFFSET = 'A' - 'a'; // 在 ASCII 表中，大写字母 'A' 的十进制值是 65，小写字母 'a' 的十进制值是 97，所以 'A' - 'a' 等于 -32。这个偏移量可以用来将小写字母转换为大写字母，或者用于比较字符时忽略大小写

    protected byte buf[];   // 内部缓冲区数组
    protected int count; // 缓冲区中有效字节的数量
    protected int pos; // 当前读取位置

    protected InputStream is;


    public SocketInputStream(InputStream is, int bufferSize) {
        this.is = is;
        buf = new byte[bufferSize];
    }

    /**
     * 从输入流中解析出 HTTP 请求的第一行（包含方法、URI 和 HTTP 版本）
     */
    public void readRequestLine(HttpRequestLine requestLine) throws IOException {
        int chr = 0;
        // 跳过空行
        do {
            try {
                chr = read();
            } catch (IOException e) {
            }
        } while ((chr == CR) || (chr == LF));

        //第一个非空位置
        pos--;
        int maxRead = requestLine.method.length;
        int readStart = pos;
        int readCount = 0;
        boolean space = false;
        //解析第一段method，以空格结束
        while (!space) {
            if (pos >= count) {
                int val = read();
                if (val == -1) {
                    throw new IOException("requestStream.readline.error");
                }
                pos = 0;
                readStart = 0;
            }
            if (buf[pos] == SP) {
                space = true;
            }
            requestLine.method[readCount] = (char) buf[pos];
            readCount++;
            pos++;
        }
        requestLine.methodEnd = readCount - 1; //method段的结束位置

        maxRead = requestLine.uri.length;
        readStart = pos;
        readCount = 0;
        space = false;
        boolean eol = false;
        //解析第二段uri，以空格结束
        while (!space) {
            if (pos >= count) {
                int val = read();
                if (val == -1)
                    throw new IOException("requestStream.readline.error");
                pos = 0;
                readStart = 0;
            }
            if (buf[pos] == SP) {
                space = true;
            }
            requestLine.uri[readCount] = (char) buf[pos];
            readCount++;
            pos++;
        }
        requestLine.uriEnd = readCount - 1; //uri结束位置

        maxRead = requestLine.protocol.length;
        readStart = pos;
        readCount = 0;
        //解析第三段protocol，以eol结尾
        while (!eol) {
            if (pos >= count) {
                int val = read();
                if (val == -1)
                    throw new IOException("requestStream.readline.error");
                pos = 0;
                readStart = 0;
            }
            if (buf[pos] == CR) {
                // Skip CR.
            } else if (buf[pos] == LF) {
                eol = true;
            } else {
                requestLine.protocol[readCount] = (char) buf[pos];
                readCount++;
            }
            pos++;
        }
        requestLine.protocolEnd = readCount;
    }

    /**
     * 从输入流中解析出 HTTP 头部
     */
    public void readHeader(HttpHeader header) throws IOException {
        int chr = read();
        if ((chr == CR) || (chr == LF)) { // Skipping CR
            if (chr == CR)
                read(); // Skipping LF
            header.nameEnd = 0;
            header.valueEnd = 0;
            return;
        } else {
            pos--;
        }
        // 正在读取 header name
        int maxRead = header.name.length;
        int readStart = pos;
        int readCount = 0;
        boolean colon = false;
        while (!colon) {
            // 我们处于内部缓冲区的末尾
            if (pos >= count) {
                int val = read();
                if (val == -1) {
                    throw new IOException("requestStream.readline.error");
                }
                pos = 0;
                readStart = 0;
            }
            if (buf[pos] == COLON) {
                colon = true;
            }
            char val = (char) buf[pos];
            if ((val >= 'A') && (val <= 'Z')) {
                val = (char) (val - LC_OFFSET);
            }
            header.name[readCount] = val;
            readCount++;
            pos++;
        }
        header.nameEnd = readCount - 1;
        // 读取 header 值（可以跨越多行）
        maxRead = header.value.length;
        readStart = pos;
        readCount = 0;
        int crPos = -2;
        boolean eol = false;
        boolean validLine = true;
        while (validLine) {
            boolean space = true;
            // 跳过空格
            // 注意：仅删除前面的空格，后面的不删。
            while (space) {
                // 我们已经到了内部缓冲区的尽头
                if (pos >= count) {
                    // 将内部缓冲区的一部分（或全部）复制到行缓冲区
                    int val = read();
                    if (val == -1)
                        throw new IOException("requestStream.readline.error");
                    pos = 0;
                    readStart = 0;
                }
                if ((buf[pos] == SP) || (buf[pos] == HT)) {
                    pos++;
                } else {
                    space = false;
                }
            }
            while (!eol) {
                // 我们已经到了内部缓冲区的尽头
                if (pos >= count) {
                    // 将内部缓冲区的一部分（或全部）复制到行缓冲区
                    int val = read();
                    if (val == -1)
                        throw new IOException("requestStream.readline.error");
                    pos = 0;
                    readStart = 0;
                }
                if (buf[pos] == CR) {
                } else if (buf[pos] == LF) {
                    eol = true;
                } else {
                    // FIXME：检查二进制转换是否正常
                    int ch = buf[pos] & 0xff;
                    header.value[readCount] = (char) ch;
                    readCount++;
                }
                pos++;
            }
            int nextChr = read();
            if ((nextChr != SP) && (nextChr != HT)) {
                pos--;
                validLine = false;
            } else {
                eol = false;
                header.value[readCount] = ' ';
                readCount++;
            }
        }
        header.valueEnd = readCount;
    }

    /**
     * 重写 InputStream 的 read 方法，从内部输入流中读取下一个字节，并更新读取位置。
     */
    @Override
    public int read() throws IOException {
        if (pos >= count) { // 每次从 buf 中读到当前的字节返回，如果 pos >= count 表示当前的 byte 已获取完毕，内部就调用 fill 方法获取新的字节流。因此，对上层程序员来说，使用 read() 就相当于可以连续读取缓存中的数据。
            fill();
            if (pos >= count) {
                return -1; // 返回 -1 表示读取出错
            }
        }
        return buf[pos++] & 0xff;
    }

    /**
     * 返回估计的可读字节数
     */
    public int available() throws IOException {
        return (count - pos) + is.available();
    }

    /**
     * 关闭流并释放资源
     */
    public void close() throws IOException {
        if (is == null) {
            return;
        }
        is.close();
        is = null;
        buf = null;
    }

    /**
     *  从底层 InputStream 读取数据到缓冲区 buf
     *  从内部输入流中填充缓冲区。如果读取的字节数大于0，则更新 count 为读取的字节数。
     */
    protected void fill() throws IOException {
        pos = 0;
        count = 0;
        int nRead = is.read(buf, 0, buf.length);
        if (nRead > 0) {
            count = nRead;
        }
    }
}
