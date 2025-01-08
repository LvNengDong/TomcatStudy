package geek.tomcat;

import geek.tomcat.tmp.Request;
import geek.tomcat.tmp.Response;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/8 16:44
 */
public interface Connector {

    public Container getContainer();

    public void setContainer(Container container);

    public String getInfo();

    public String getScheme();

    public void setScheme(String scheme);

    public Request createRequest();

    public Response createResponse();

    public void initialize();
}
