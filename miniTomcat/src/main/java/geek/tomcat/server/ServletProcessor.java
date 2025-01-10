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

    public void process(Request request, Response response) {
        String uri = request.getUri();
        try {
            log.info("ServletProcessor处理开始 uri={}", uri);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            // 写响应头
            PrintWriter writer = response.getWriter();
            writer.println(composeResponseHead());

            // 首先根据uri最后一个/号来定位，后面的字符串认为是servlet名字(全限定类名)
            String servletName = uri.substring(uri.lastIndexOf("/") + 1);
            // 反射创建 servlet 实例，并执行 service() 方法
            Class<?> servletClass = ClassLoaderUtil.loadClassByDir(Constants.WEB_ROOT, servletName);
            Servlet servlet = (Servlet) servletClass.newInstance();
            servlet.service(request, response);
            log.info("ServletProcessor处理结束 uri={}", uri);
        } catch (Exception e) {
            log.error("ServletProcessor处理异常 uri={}", uri, e);
        }
    }

    //生成响应头，填充变量值
    private String composeResponseHead() {
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("StatusCode", "200");
        valuesMap.put("StatusName", "OK");
        valuesMap.put("ContentType", "text/html;charset=uft-8");
        valuesMap.put("ZonedDateTime", DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now()));
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String responseHead = sub.replace(Constants.OKMessage);
        return responseHead;
    }
}
