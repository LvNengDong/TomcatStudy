package geek.tomcat.server;

import geek.tomcat.Constants;
import geek.tomcat.util.ClassLoaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;

import javax.servlet.Servlet;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lnd
 * @Description
 * @Date 2024/5/12 13:38
 */
@Slf4j
public class ServletProcessor {

    public void process(HttpRequest request, HttpResponse response) {
        String uri = request.getUri();
        try {
            log.info("ServletProcessor处理开始 uri={}", uri);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.sendHeaders();

            // 首先根据uri最后一个/号来定位，后面的字符串认为是servlet名字(全限定类名)
            String servletName = uri.substring(uri.lastIndexOf("/") + 1);
            // 反射创建 servlet 实例，并执行 service() 方法
            Class<?> servletClass = ClassLoaderUtil.loadClassByDir(Constants.WEB_ROOT, servletName);
            Servlet servlet = (Servlet) servletClass.newInstance();
            HttpRequestFacade requestFacade = new HttpRequestFacade(request);
            HttpResponseFacade responseFacade = new HttpResponseFacade(response);
            servlet.service(requestFacade, responseFacade);
            log.info("ServletProcessor处理结束 uri={}", uri);
        } catch (Exception e) {
            log.error("ServletProcessor处理异常 uri={}", uri, e);
        }
    }
}
