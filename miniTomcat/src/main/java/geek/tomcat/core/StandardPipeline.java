package geek.tomcat.core;

import com.google.common.collect.Lists;
import geek.tomcat.*;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * @Author lnd
 * @Description
 * @Date 2026/9/25 15:12
 */
public class StandardPipeline implements Pipeline {

    private String info = "geek.tomcat.core.StandardPipeline/0.1";

    private Valve basic;
    @Getter@Setter
    private Container container;
    private int debug;
    private final List<Valve> valves = Lists.newArrayList();

    public StandardPipeline(Container container) {
        super();
        this.setContainer(container);
    }



    @Override
    public Valve getBasic() {
        return this.basic;
    }

    @Override
    public void setBasic(Valve valve) {
        if (valve == null) {
            return;
        }
        Valve oldBasic = this.basic;
        if (oldBasic == valve) {
            return;
        }
        valve.setContainer(container);
        this.basic = valve;
    }

    @Override
    public void addValve(Valve valve) {
        synchronized (valves) {
            valve.setContainer(container);
            valves.add(valve);
        }
    }

    @Override
    public List<Valve> getValves() {
        return valves;
    }

    @Override
    public void removeValve(Valve valve) {
        synchronized (valves) {
            valves.remove(valve);
        }
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        System.out.println("StandardPipeline invoke()");
        ValveContext valveContext = new StandardPipelineValveContext();
        valveContext.invokeNext(request, response);
    }

    /**
     * 维护了stage，表示valves数组中的位置，逐个invoke
     */
    protected class StandardPipelineValveContext implements ValveContext {

        protected int stage = 0;

        @Override
        public String getInfo() {
            return "geek.tomcat.core.StandardPipeline.StandardPipelineValveContext/v1.0";
        }

        @Override
        public void invokeNext(Request request, Response response) throws IOException, ServletException {
            System.out.println("StandardPipelineValveContext invokeNext()");
            int subscript = stage; // 下标
            if (subscript < valves.size()) {
                Valve valve = valves.get(subscript);
                valve.invoke(request, response, this);
            } else if (subscript == valves.size() && basic != null) {
                basic.invoke(request, response, this);
            } else {
                throw new ServletException("standardPipeline.noValve");
            }
        }
    }
}
