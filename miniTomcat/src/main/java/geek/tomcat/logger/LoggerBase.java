package geek.tomcat.logger;

import geek.tomcat.Logger;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.ServletException;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/9 14:47
 */
public abstract class LoggerBase implements Logger {
    @Getter
    @Setter
    protected int debug = 0;
    @Getter
    protected String info = "geek.tomcat.logger.LoggerBase/1.0";
    @Getter
    @Setter
    protected int verbosity = ERROR;

    public void setVerbosityLevel(String verbosity) {
        if ("FATAL".equalsIgnoreCase(verbosity))
            this.verbosity = FATAL;
        else if ("ERROR".equalsIgnoreCase(verbosity))
            this.verbosity = ERROR;
        else if ("WARNING".equalsIgnoreCase(verbosity))
            this.verbosity = WARNING;
        else if ("INFORMATION".equalsIgnoreCase(verbosity))
            this.verbosity = INFORMATION;
        else if ("DEBUG".equalsIgnoreCase(verbosity))
            this.verbosity = DEBUG;
    }

    //这个log方法由上层业务程序员实现
    public abstract void log(String msg);

    public void log(Exception exception, String msg) {
        log(msg, exception);
    }

    //核心方法，printStackTrace，然后调用一个业务实现的log(msg)
    public void log(String msg, Throwable throwable) {
        CharArrayWriter buf = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buf);
        writer.println(msg);
        throwable.printStackTrace(writer);
        Throwable rootCause = null;
        if (throwable instanceof ServletException)
            rootCause = ((ServletException) throwable).getRootCause();
        if (rootCause != null) {
            writer.println("----- Root Cause -----");
            rootCause.printStackTrace(writer);
        }
        log(buf.toString());
    }

    public void log(String message, int verbosity) {
        if (this.verbosity >= verbosity)
            log(message);
    }

    public void log(String message, Throwable throwable, int verbosity) {
        if (this.verbosity >= verbosity)
            log(message, throwable);
    }
}
