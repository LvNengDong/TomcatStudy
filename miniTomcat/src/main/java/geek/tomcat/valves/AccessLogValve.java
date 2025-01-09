package geek.tomcat.valves;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/9 15:54
 */
public final class AccessLogValve extends ValveBase {
    //下面的属性都是与访问日志相关的配置参数
    public static final String COMMON_ALIAS = "common";
    public static final String COMMON_PATTERN = "%h %l %u %t \"%r\" %s %b";
    public static final String COMBINED_ALIAS = "combined";
    public static final String COMBINED_PATTERN = "%h %l %u %t \"%r\" %s %b \"%{Referer}i\" \"%{User-Agent}i\"";

    public AccessLogValve() {
        super();
        setPattern("common");
    }

    private String dateStamp = "";
    private String directory = "logs";
    protected static final String info =
            "com.minit.valves.AccessLogValve/0.1";
    protected static final String months[] =
            {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private boolean common = false;
    private boolean combined = false;
    private String pattern = null;
    private String prefix = "access_log.";
    private String suffix = "";
    private PrintWriter writer = null;
    private DateTimeFormatter dateFormatter = null;
    private DateTimeFormatter dayFormatter = null;
    private DateTimeFormatter monthFormatter = null;
    private DateTimeFormatter yearFormatter = null;
    private DateTimeFormatter timeFormatter = null;
    private String timeZone = null;
    private LocalDate currentDate = null;
    private String space = " ";
    private long rotationLastChecked = 0L;

    //省略属性的getter/setter

    //这是核心方法invoke
    public void invoke(Request request, Response response, ValveContext context)
            throws IOException, ServletException {
        // 先调用context中的invokeNext，实现职责链调用
        // Pass this request on to the next valve in our pipeline
        context.invokeNext(request, response);

        //以下是本valve本身的业务逻辑
        LocalDate date = getDate();
        StringBuffer result = new StringBuffer();
        // Check to see if we should log using the "common" access log pattern
        //拼串
        if (common || combined) {
            //拼串，省略
        } else { //按照模式拼串
            // Generate a message based on the defined pattern
            boolean replace = false;
            for (int i = 0; i < pattern.length(); i++) {
                char ch = pattern.charAt(i);
                if (replace) {
                    result.append(replace(ch, date, request, response));
                    replace = false;
                } else if (ch == '%') {
                    replace = true;
                } else {
                    result.append(ch);
                }
            }
        }
        log(result.toString(), date);
    }

    private synchronized void close() {
        if (writer == null)
            return;
        writer.flush();
        writer.close();
        writer = null;
        dateStamp = "";
    }

    //按照日期生成日志文件，并记录日志
    public void log(String message, LocalDate date) {
        // Only do a logfile switch check once a second, max.
        long systime = System.currentTimeMillis();
        if ((systime - rotationLastChecked) > 1000) {
            // We need a new currentDate
            currentDate = LocalDate.now();
            rotationLastChecked = systime;
            // Check for a change of date
            String tsDate = dateFormatter.format(currentDate);
            // If the date has changed, switch log files
            if (!dateStamp.equals(tsDate)) {
                synchronized (this) {
                    if (!dateStamp.equals(tsDate)) {
                        close();
                        dateStamp = tsDate;
                        open();
                    }
                }
            }
        }
        // Log this message
        if (writer != null) {
            writer.println(message);
        }
    }

    //打开日志文件
    private synchronized void open() {
        // Create the directory if necessary
        File dir = new File(directory);
        if (!dir.isAbsolute())
            dir = new File(System.getProperty("minit.base"), directory);
        dir.mkdirs();
        // Open the current log file
        try {
            String pathname = dir.getAbsolutePath() + File.separator +
                    prefix + dateStamp + suffix;
            writer = new PrintWriter(new FileWriter(pathname, true), true);
        } catch (IOException e) {
            writer = null;
        }
    }

    //替换字符串
    private String replace(char pattern, LocalDate date, Request request,
                           Response response) {
        //省略
    }

    private LocalDate getDate() {
        // Only create a new Date once per second, max.
        long systime = System.currentTimeMillis();
        if ((systime - currentDate.getLong(ChronoField.MILLI_OF_SECOND)) > 1000) {
            currentDate = LocalDate.now();
        }
        return currentDate;
    }
}
