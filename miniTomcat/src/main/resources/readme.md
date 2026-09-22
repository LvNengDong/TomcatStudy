## lesson10 —— Servlet 容器化
#### 要解决的问题：
每个请求都反射 newInstance 一个 Servlet，既浪费又不符合 Servlet 规范（规范要求单例且只 init 一次）。

#### 意义：
「 Wrapper 包一个 Servlet、Container 管一堆 Wrapper」的两层结构出现了，这是下一课接口化的现实基础。

#### 关键改动：
- 新增 ServletContainer：持有指向 webroot 的 URLClassLoader，以及 servletName → ServletWrapper 的映射；invoke() 时先查缓存，查不到才新建。
- 新增 ServletWrapper：构造时 loadServlet()，走 loadClass → newInstance → servlet.init(null)，实例缓存在字段里。
- ServletProcessor 退化为把请求转交给容器。

| 动作 | 文件                         | 为什么                                                       |
| :--- | :--------------------------- | :----------------------------------------------------------- |
| 增   | server/ServletContainer.java | 持有 URLClassLoader 和 servletName → Wrapper 映射，把「Servlet 在哪、活多久」从 ServletProcessor 里拿走 |
| 增   | server/ServletWrapper.java   | 包住单个 Servlet 实例，构造时 loadClass + newInstance + init，实例缓存起来。Servlet 规范要求单例且只 init 一次，之前每请求 newInstance 是不合规的 |
| 改   | server/ServletProcessor.java | 方法体缩成一行 `connector.getContainer().invoke(...)`，原来的反射加载逻辑被整段注释保留在文件里作对照 |
| 改   | server/HttpConnector.java    | 加 container 字段和读写方法，建立 Connector ↔ Container 的相互引用 |
| 改   | server/HttpServer.java       | main 里改为创建 Connector 与 Container 并互相注入 —— Tomcat 启动形态的雏形 |
| 改   | server/HttpProcessor.java    | 构造 ServletProcessor 时把 connector 传进去，好让它能拿到容器 |

