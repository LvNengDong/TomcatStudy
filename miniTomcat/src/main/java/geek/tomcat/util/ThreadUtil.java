package geek.tomcat.util;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/9 22:03
 */
public class ThreadUtil {

    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }
}
