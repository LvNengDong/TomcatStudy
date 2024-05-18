# TomcatStudy
Tomcat学习


请求路径 

http://localhost:8080/hello.txt

http://localhost:8080/servlet/HelloServlet


## 05
### 多线程HttpProcessor
思路：让Connector和Processor分别由不同的线程来运行，工作的基本流程是由Connector接收某个Socket连接后交给某个
Processor线程处理，而Processor线程等待某个Socket连接到来后进行处理，处理完毕后交回给Connector。因此，这里的
核心是要设计一个线程之间的同步机制。