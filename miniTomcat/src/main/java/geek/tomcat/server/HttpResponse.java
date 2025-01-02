package geek.tomcat.server;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lnd
 * @Description
 * @Date 2024/12/15 22:38
 */
public class HttpResponse implements HttpServletResponse {

    HttpRequest request;
    OutputStream output;
    PrintWriter writer;

    String contentType = null;
    long contentLength = -1;
    String charset = null;
    String characterEncoding = null;
    String protocol = "HTTP/1.1";

    Map<String, String> headers = new ConcurrentHashMap<>();
    // 默认返回OK
    String message = getStatusMessage(HttpServletResponse.SC_OK);
    int status = HttpServletResponse.SC_OK;

    public HttpResponse(OutputStream output) {
        this.output = output;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    protected String getStatusMessage(int status) {
        switch (status) {
            case SC_OK:
                return "OK";
            case SC_ACCEPTED:
                return ("Accepted");
            case SC_BAD_GATEWAY:
                return ("Bad Gateway");
            case SC_BAD_REQUEST:
                return ("Bad Request");
            case SC_CONTINUE:
                return ("Continue");
            case SC_FORBIDDEN:
                return ("Forbidden");
            case SC_INTERNAL_SERVER_ERROR:
                return ("Internal Server Error");
            case SC_METHOD_NOT_ALLOWED:
                return ("Method Not Allowed");
            case SC_NOT_FOUND:
                return ("Not Found");
            case SC_NOT_IMPLEMENTED:
                return ("Not Implemented");
            case SC_REQUEST_URI_TOO_LONG:
                return ("Request URI Too Long");
            case SC_SERVICE_UNAVAILABLE:
                return ("Service Unavailable");
            case SC_UNAUTHORIZED:
                return ("Unauthorized");
            default:
                return ("HTTP Response Status " + status);
        }
    }

    /**
     * 按照HTTP协议的规定，将Header打印出来
     *
     * @throws IOException
     */
    public void sendHeaders() throws IOException {
        PrintWriter outputWriter = getWriter();
        //下面这一端是输出状态行
        outputWriter.print(this.getProtocol());
        outputWriter.print(" ");
        outputWriter.print(status);
        if (message != null) {
            outputWriter.print(" ");
            outputWriter.print(message);
        }
        outputWriter.print("\r\n");

        if (getContentType() != null) {
            outputWriter.print("Content-Type: " + getContentType() + "\r\n");
        }
        if (getContentLength() >= 0) {
            outputWriter.print("Content-Length: " + getContentLength() + "\r\n");
        }

        //输出头信息
        Iterator<String> names = headers.keySet().iterator();
        while (names.hasNext()) {
            String name = names.next();
            String value = headers.get(name);
            outputWriter.print(name);
            outputWriter.print(": ");
            outputWriter.print(value);
            outputWriter.print("\r\n");
        }

        //最后输出空行
        outputWriter.print("\r\n");
        outputWriter.flush();
    }

    private long getContentLength() {
        return this.contentLength;
    }

    private String getProtocol() {
        return this.protocol;
    }

    public OutputStream getOutput() {
        return this.output;
    }

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String name) {
        return (headers.get(name) != null);
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return null;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {

    }

    @Override
    public void sendError(int sc) throws IOException {

    }

    @Override
    public void sendRedirect(String location) throws IOException {

    }

    @Override
    public void setDateHeader(String name, long date) {

    }

    @Override
    public void addDateHeader(String name, long date) {

    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
        if (name.toLowerCase() == DefaultHeaders.CONTENT_LENGTH_NAME) {
            setContentLength(Integer.parseInt(value));
        }
        if (name.toLowerCase() == DefaultHeaders.CONTENT_TYPE_NAME) {
            setContentType(value);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        headers.put(name, value);
        if (name.toLowerCase() == DefaultHeaders.CONTENT_LENGTH_NAME) {
            setContentLength(Integer.parseInt(value));
        }
        if (name.toLowerCase() == DefaultHeaders.CONTENT_TYPE_NAME) {
            setContentType(value);
        }
    }

    @Override
    public void setIntHeader(String name, int value) {

    }

    @Override
    public void addIntHeader(String name, int value) {

    }

    @Override
    public void setStatus(int status) {
        this.status = status;
        this.message = this.getStatusMessage(status);
    }

    @Override
    public void setStatus(int sc, String sm) {

    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        writer = new PrintWriter(new OutputStreamWriter(output, getCharacterEncoding()), true);
        return writer;
    }

    @Override
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    @Override
    public void setContentLength(int length) {
        this.contentLength = length;
    }

    @Override
    public void setContentLengthLong(long length) {
        this.contentLength = length;
    }

    @Override
    public void setContentType(String type) {
        this.contentType = type;
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
