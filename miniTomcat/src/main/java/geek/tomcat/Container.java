package geek.tomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author lnd
 * @Description 所有容器层的共同契约
 * @Date 2025/1/8 15:32
 */
public interface Container {

    String ADD_CHILD_EVENT = "addChild";

    String REMOVE_CHILD_EVENT = "removeChild";

    // ==================================================
    String getInfo();

    ClassLoader getLoader();

    /* 每个容器层可以有自己的类加载器 */
    void setLoader(ClassLoader loader);

    String getName();

    void setName(String name);

    // ================================================== 树结构（父子双边）
    Container getParent();

    void setParent(Container container);

    void addChild(Container child);

    Container findChild(String name);

    Container[] findChildren();

    void removeChild(Container child);

    Logger getLogger();

    void setLogger(Logger logger);

    // ================================================== 行为（整个接口唯一的动词）
    /* 关键词 invoke 只有一个。上层调用下层时不需要知道下层是 Context 还是 Wrapper，一律 invoke
    —— 这是分层能成立的前提，也是后面 Valve 责任链能一路穿下去的基础。 */
    void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
