package geek.tomcat.logger;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/9 14:47
 */
public class SystemOutLogger extends LoggerBase {
    protected static final String info =
            "com.minit.logger.SystemOutLogger/1.0";
    public void log(String msg) {
        System.out.println(msg);
    }
}
