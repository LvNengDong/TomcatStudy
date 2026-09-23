package geek.tomcat.valves;

import geek.tomcat.Container;
import geek.tomcat.Valve;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author lnd
 * @Description
 * @Date 2025/1/9 15:51
 */
@Getter
@Setter
public abstract class ValveBase implements Valve {

    protected Container container = null;
    protected int debug = 0;
    protected static String info = "com.minit.valves.ValveBase/0.1";
}
