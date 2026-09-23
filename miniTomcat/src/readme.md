#### 要解决的问题：
所有类堆在 geek.tomcat.server 一个包里，没有接口、没有层次，加任何新能力都要改已有类。

#### 关键改动：
文件从 28 个涨到 37 个，包结构彻底重排。

| 包                  | 内容                                                         |
| :------------------ | :----------------------------------------------------------- |
| geek.tomcat（顶层） | 纯接口：Connector / Container / Context / Wrapper / Request / Response / Session |
| connector.http      | HttpConnector、HttpProcessor、HttpRequestImpl、HttpResponseImpl、SocketInputStream、两个 Processor |
| core                | ContainerBase（抽象父类）、StandardContext、StandardWrapper  |
| session             | StandardSession、StandardSessionFacade                       |
| startup             | Bootstrap                                                    |
| tmp                 | 旧的 Request / Response / Session 实现。注意它并非纯归档：HttpConnector 建 Session 用的就是 tmp.Session，Connector 接口 import 的也是 tmp 包里的 Request / Response |









## Pipeline.java、Valve.java、ValveContext.java 的作用分别是什么？

这三个接口是同一套责任链机制的三个角色，拆开是为了各管一件事：



```
Valve         链上的一个节点 —— 一段可插拔的处理逻辑
Pipeline      管这条链的容器 —— 增删、排序、持有那个特殊的 basic
ValveContext  链的运行时游标 —— 只负责"把请求推给下一个"
```

#### `Valve` —— 一个可插拔的处理节点

```java
public interface Valve {
    String getInfo();
    Container getContainer();
    void setContainer(Container container);
    void invoke(Request request, Response response, ValveContext context);
}
```

核心就是 `invoke` 的第三个参数。把它和 Servlet 规范的 Filter 放一起看：



```java
void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)   // Filter
void invoke  (Request        req, Response        resp, ValveContext context) // Valve
```

**签名完全同构**——第三个参数都是"推进器"。Valve 之于容器，就是 Filter 之于 Servlet：一段横切逻辑，自己不关心前后是谁。

`getContainer` / `setContainer` 让 Valve 知道自己挂在哪个容器上，lesson13 的 `StandardWrapperValve` 就是靠 `getContainer()` 拿到 `StandardWrapper` 的。

[ValveBase.java](https://vibe.corp.qunar.com/nengdong.lv/stable-ddeb0a3de0321412c0633dffa85d35770005ae0f/static/out/vs/workbench/contrib/webview/browser/pre/TomcatStudy/miniTomcat/src/main/java/geek/tomcat/valves/ValveBase.java) 把 `container` 字段和 `getInfo` / `getDebug` 这些样板下沉，子类只需实现 `invoke` —— 和 `ContainerBase` 是同一个套路。

## `Pipeline` —— 链的管理者



```java
public interface Pipeline {
    Valve getBasic();
    void  setBasic(Valve valve);      // ← 注意它和 addValve 是分开的
    void  addValve(Valve valve);
    Valve[] getValves();
    void  removeValve(Valve valve);
    void  invoke(Request request, Response response);
}
```

**每个容器持有一条 Pipeline**，容器的 `invoke()` 实际上就是转发给 `pipeline.invoke()`。

### 为什么 `basic` 要单独拎出来

因为链上的 Valve 分两类：

|                     | basic valve            | 普通 valve      |
| ------------------- | ---------------------- | --------------- |
| 数量                | 恰好 1 个              | 0 到 N 个       |
| 位置                | 永远最后               | 都在 basic 之前 |
| 是否调 `invokeNext` | **不调**（它就是终点） | 调              |
| 语义                | 容器的**本职工作**     | **附加**能力    |

对应到你的代码：

- Context 的 basic 应该是 `StandardContextValve` —— 找到目标 Wrapper 并调用它
- Wrapper 的 basic 应该是 `StandardWrapperValve` —— 组装 Filter 链并调 `servlet.service()`
- `AccessLogValve` 这类是普通 valve，挂多少个都行

这样设计保证了：**不管你往链上挂多少附加能力，容器的本职工作总在最后，而且一定会执行。**

## `ValveContext` —— 只负责"下一个"



```java
public interface ValveContext {
    String getInfo();
    void invokeNext(Request request, Response response);
}
```

一个业务方法，这是整套设计里最见功力的一处。

Valve 需要知道"下一个是谁"，有三种做法：

1. **Valve 自己持有 next 指针** → Valve 变成有状态的链表节点，不能共享，调整顺序要改对象内部
2. **把 Pipeline 传给 Valve，让它自己找下一个** → Valve 得知道自己的下标，状态泄漏到调用方
3. **传一个只会说"该下一个了"的轻量对象** ← Tomcat 的选择

第三种的好处：

- **Valve 是无状态的**，可以被多个请求并发调用 —— 游标状态在 ValveContext 里，每个请求一份
- **Valve 完全不知道链的结构**，Pipeline 的内部实现（数组还是链表）可以随便换
- **接口隔离**：Pipeline 管配置（增删查），ValveContext 管运行（推进），是同一条链的两个视角

真实 Tomcat 里 `StandardPipeline` 用一个内部类 `StandardPipelineValveContext` 同时扮演这两个角色 —— 外部看到的是两个窄接口，内部是一个对象。

## 一个 Valve 有四种姿势

这是责任链比 if-else 硬编码强的地方。看 [AccessLogValve.invoke()](https://vibe.corp.qunar.com/nengdong.lv/stable-ddeb0a3de0321412c0633dffa85d35770005ae0f/static/out/vs/workbench/contrib/webview/browser/pre/TomcatStudy/miniTomcat/src/main/java/geek/tomcat/valves/AccessLogValve.java)：



```java
public void invoke(Request request, Response response, ValveContext context) {
    context.invokeNext(request, response);   // ① 先把请求交下去
    // ② 再记日志
}
```

**先 invokeNext 再干活**，因为访问日志要记录响应的状态码和字节数，这些只有等整条链跑完才有。

由此引出四种姿势：

| 姿势     | 写法                       | 典型用途                         |
| -------- | -------------------------- | -------------------------------- |
| 前置     | 干活 → `invokeNext`        | 请求改写、编码设置               |
| 后置     | `invokeNext` → 干活        | 访问日志（就是 AccessLogValve）  |
| 环绕     | 干活 → `invokeNext` → 干活 | 耗时统计                         |
| **短路** | **不调 `invokeNext`**      | 鉴权失败直接写 403，请求到此为止 |

第四种是责任链真正的价值 —— 拦截能力。

## 当前状态：三个接口都还没接线

| 事实                                 | 说明                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| `Pipeline` 没有任何实现类            | 全仓库只有它自己的文件里出现过这个词                         |
| `ContainerBase` 里没有 pipeline 字段 | 容器还没地方挂这条链                                         |
| 请求链路没变                         | 仍是 `StandardContext.invoke()` 直调 `StandardWrapper.invoke()` |
| `AccessLogValve` 从未被执行过        | 没有链可挂                                                   |

lesson13 新增的 `StandardContextValve` / `StandardWrapperValve`（都 `extends ValveBase`）就是在为接线做准备 —— 它们本该分别成为 Context 和 Wrapper 的 basic valve，但 Bootstrap 相比 lesson12 一行没改，所以这一步还没发生。

这也是我之前提到的那个阅读陷阱的最典型例子：**lesson12 的"责任链"目前只是一组接口定义，代码存在不等于它在跑。**

## 接线大概长这样



```java
// ContainerBase：每个容器持有一条 pipeline，invoke 转发过去
protected Pipeline pipeline = new StandardPipeline(this);
public Pipeline getPipeline() { return pipeline; }
public void invoke(Request req, Response resp) {
    pipeline.invoke(req, resp);            // ← 容器不再自己处理，交给链
}

// StandardContext 构造时：把本职工作设为 basic
pipeline.setBasic(new StandardContextValve());

// StandardWrapper 构造时
pipeline.setBasic(new StandardWrapperValve());

// Bootstrap 里想加访问日志，就挂一个普通 valve
context.getPipeline().addValve(new AccessLogValve());
```

还缺的那个 `StandardPipeline`，核心是一个内部类实现 `ValveContext` 做游标：



```java
protected class StandardPipelineValveContext implements ValveContext {
    protected int stage = 0;
    public void invokeNext(Request request, Response response) {
        int subscript = stage++;
        if (subscript < valves.length) {
            valves[subscript].invoke(request, response, this);   // 把 this 继续传下去
        } else {
            basic.invoke(request, response, this);               // 走完了，执行 basic
        }
    }
}
```

`stage` 这个游标状态放在 ValveContext 里而不是 Valve 里 —— 这就是前面说的"Valve 保持无状态"落到实处的地方。

