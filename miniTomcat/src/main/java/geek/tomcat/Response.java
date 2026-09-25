package geek.tomcat;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 17:31
 */
 public interface Response {
     Connector getConnector();
     void setConnector(Connector connector);
     int getContentCount();
     Context getContext();
     void setContext(Context context);
     String getInfo();
     Request getRequest();
     void setRequest(Request request);
     ServletResponse getResponse();
     OutputStream getStream();
     void setStream(OutputStream stream);
     void setError();
     boolean isError();
     ServletOutputStream createOutputStream() throws IOException;
     void finishResponse() throws IOException;
     int getContentLength();
     String getContentType();
     PrintWriter getReporter();
     void recycle();

    void setContentLengthLong(int length);

    void resetBuffer();
     void sendAcknowledgement() throws IOException;
}

