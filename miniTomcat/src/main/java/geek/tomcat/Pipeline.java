package geek.tomcat;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;

/**
 * @Author lnd
 * @Description 链的管理者
 *   Pipeline 表示的是 Container 中的 Valve 链条，其中有特殊的 basic。Pipeline 启动 Valve 链条的调用。
 * @Date 2025/1/9 15:49
 */
public interface Pipeline {

     Valve getBasic();

     void setBasic(Valve valve);
     //
     void addValve(Valve valve);

     List<Valve> getValves();

     void removeValve(Valve valve);

     void invoke(Request request, Response response) throws IOException, ServletException;
}

/*
每个容器持有一条 Pipeline，容器的 invoke() 实际上就是转发给 pipeline.invoke()。

为什么 basic 要单独拎出来?

 */