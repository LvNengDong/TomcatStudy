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

