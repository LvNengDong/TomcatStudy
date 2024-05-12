package geek.tomcat.server;


import lombok.Data;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * @Author lnd
 * @Description HTTP协议响应
 * @Date 2024/1/10 22:04
 */
@Data
public class Response implements ServletResponse {
    Request request;
    OutputStream output;
    String characterEncoding = null;

    public Response(OutputStream output) {
        this.output = output;
    }

    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        /*
         * 在 PrintWriter 构造函数中，我们目前设置了一个值为 true。这个值的含义为 autoflush，
         * 当为 true 时，println、printf 等方法会自动刷新输出流的缓冲。
         */
        return new PrintWriter(new OutputStreamWriter(output, getCharacterEncoding()), true);
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.characterEncoding = charset;
    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long len) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
