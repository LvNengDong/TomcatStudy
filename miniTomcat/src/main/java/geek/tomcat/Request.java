package geek.tomcat;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 17:30
 */
 public interface Request {
     Connector getConnector();
     void setConnector(Connector connector);
     Context getContext();
     void setContext(Context context);
     String getInfo();
     ServletRequest getRequest();
     Response getResponse();
     void setResponse(Response response);
     Socket getSocket();
     void setSocket(Socket socket);
     InputStream getStream();
     void setStream(InputStream stream);
     Wrapper getWrapper();
     void setWrapper(Wrapper wrapper);
     ServletInputStream createInputStream() throws IOException;
     void finishRequest() throws IOException;
     void recycle();
     void setContentLength(int length);
     void setContentType(String type);
     void setProtocol(String protocol);
     void setRemoteAddr(String remote);
     void setScheme(String scheme);
     void setServerPort(int port);
}
