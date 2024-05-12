package geek.tomcat.server;

import geek.tomcat.Constants;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lnd
 * @Description statistic
 * @Date 2024/5/12 13:38
 */
public class StatisticResourceProcessor {

    public void process(Request request, Response response) throws IOException {
        byte[] bytes = new byte[Constants.BUFFER_SIZE];
        FileInputStream fis = null;
        OutputStream output = null;
        try {
            output = response.getOutput();
            File file = new File(Constants.WEB_ROOT, request.getUri());
            if (file.exists()) {
                // 拼响应头
                String head = composeResponseHead(file);
                output.write(head.getBytes("utf-8"));
                // 读取文件内容，写入输出流
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, Constants.BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, Constants.BUFFER_SIZE);
                }
                output.flush();
            } else {
                output.write(Constants.fileNotFoundMessage.getBytes());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    private String composeResponseHead(File file) {
        long fileLength = file.length();
        Map valuesMap = new HashMap<>();
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
