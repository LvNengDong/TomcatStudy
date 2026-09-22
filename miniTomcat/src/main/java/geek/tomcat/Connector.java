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


     // getScheme / setScheme —— http 还是 https，一个 Connector 对应一种协议 + 一个端口
     String getScheme();
     void setScheme(String scheme);


     // createRequest() / createResponse() —— 最能说明 Connector 定位的两个方法：Request/Response 的具体实现由协议决定（HTTP 的和 AJP 的不一样），所以由 Connector 来生产。
     Request createRequest();
     Response createResponse();

     // 启动前的初始化钩子
     void initialize();
}
