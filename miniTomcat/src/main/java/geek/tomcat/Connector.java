package geek.tomcat;

import geek.tomcat.tmp.Request;
import geek.tomcat.tmp.Response;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 16:44
 */
public interface Connector {

     Container getContainer();

     void setContainer(Container container);

     String getInfo();

     String getScheme();

     void setScheme(String scheme);

     Request createRequest();

     Response createResponse();

     void initialize();
}
