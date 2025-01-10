package geek.tomcat.server;

import geek.tomcat.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lnd
 * @Description statistic
 * @Date 2024/5/12 13:38
 */
@Slf4j
public class StatisticResourceProcessor {

    public static final int BUFFER_SIZE = 1024;

    public void process(HttpRequest request, HttpResponse response) throws IOException {
        String uri = request.getUri();
        log.info("StatisticResourceProcessor处理开始 uri={}", uri);

        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            OutputStream output = response.getOutput();
            File file = new File(Constants.WEB_ROOT, uri);
            if (file.exists()) {
                // 写响应头
                String responseHead = composeResponseHead(file);
                output.write(responseHead.getBytes(StandardCharsets.UTF_8));

                // 读取文件内容，写入输出流
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) { // 循环读写
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
                output.flush();
            } else {
                output.write(Constants.fileNotFoundMessage.getBytes());
            }
        } catch (Exception e) {
            log.info("StatisticResourceProcessor处理异常 uri={}", uri, e);
        } finally {
            if (fis != null) {
                fis.close();
                log.info("StatisticResourceProcessor处理结束 uri={}", uri);
            }
        }
    }

    private String composeResponseHead(File file) {
        long fileLength = file.length();
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("StatusCode", "200");
        valuesMap.put("StatusName", "OK");
        valuesMap.put("ContentType", "text/html;charset=utf-8");
        valuesMap.put("ContentLength", fileLength);
        valuesMap.put("ZonedDateTime", DateTimeFormatter.ISO_ZONED_DATE_TIME.format(ZonedDateTime.now()));
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String responseHead = sub.replace(Constants.OKMessage);
        return responseHead;
    }
}
