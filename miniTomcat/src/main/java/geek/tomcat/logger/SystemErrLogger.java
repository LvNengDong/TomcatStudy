package geek.tomcat.logger;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/9 14:47
 */
public class SystemErrLogger extends LoggerBase {
    protected static final String info = "com.minit.logger.SystemErrLogger/0.1";

    public void log(String msg) {
        System.err.println(msg);
    }
}
