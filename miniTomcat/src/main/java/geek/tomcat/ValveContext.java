package geek.tomcat;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @Author lnd
 * @Description 链的运行时游标 —— 只负责"把请求推给下一个"
 *
 * ValveContext 接口负责调用下一个 Valve
 * @Date 2025/1/9 15:48
 */
public interface ValveContext {

    String getInfo();

    void invokeNext(Request request, Response response) throws IOException, ServletException;
}
