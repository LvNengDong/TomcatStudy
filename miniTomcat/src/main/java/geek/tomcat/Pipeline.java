package geek.tomcat;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 * @Author lnd
 * @Description Pipeline 表示的是 Container 中的 Valve 链条，其中有特殊的 basic。Pipeline 启动 Valve 链条的调用。
 * @Date 2025/1/9 15:49
 */
public interface Pipeline {

    public Valve getBasic();

    public void setBasic(Valve valve);

    public void addValve(Valve valve);

    public Valve[] getValves();

    public void invoke(Request request, Response response) throws IOException, ServletException;

    public void removeValve(Valve valve);

}
