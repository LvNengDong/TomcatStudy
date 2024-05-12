package geek.tomcat.util;

import java.text.MessageFormat;

/**
 * @Author lnd
 * @Description
 * @Date 2024/5/10 14:15
 */
public class Logger {


    public static void info(String format, Object... args) {
        String msg = MessageFormat.format(format, args);
        System.out.println("Logger_Info: " + msg);
    }

    public static void error(String msg) {
        System.out.println("Logger_Error: " + msg);
    }
}
