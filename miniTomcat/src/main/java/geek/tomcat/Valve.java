package geek.tomcat;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author lnd
 * @Description 链上的一个节点 —— 一段可插拔的处理逻辑
 * @Date 2025/1/9 15:47
 */
public interface Valve {
     String getInfo();

     // getContainer / setContainer 让 Valve 知道自己挂在哪个容器上
     Container getContainer();

     void setContainer(Container container);

    /**
     *
     * @param request
     * @param response
     * @param context  链的运行时游标 —— 负责"把请求推给下一个"
     * @throws IOException
     * @throws ServletException
     */
     void invoke(Request request, Response response, ValveContext context) throws IOException, ServletException;
}
