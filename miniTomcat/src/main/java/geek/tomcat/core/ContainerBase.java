package geek.tomcat.core;

import geek.tomcat.*;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lnd
 * @Description 因为存在多层 Container，很多特性是共有的，所以我们定义 ContainerBase 作为基础类
 * ContainerBase 把子容器管理和类加载器向上委托这些共性下沉，避免每个容器层重复实现
 * @Date 2025/1/8 15:32
 */
public abstract class ContainerBase implements Container {
    // 父子容器
    protected Map<String, Container> children = new ConcurrentHashMap<>();
    protected Container parent = null;

    //类加载器
    protected ClassLoader loader = null;

    protected String name = null;

    // 下面是基本的get和set方法
    protected Logger logger = null;

    @Setter
    @Getter
    protected Pipeline pipeline = new StandardPipeline(this); //增加pipeline支持

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        System.out.println("ContainerBase invoke()");
        pipeline.invoke(request, response);
    }

    //下面是基本的get和set方法
    public abstract String getInfo();

    public ClassLoader getLoader() {
        if (loader != null) {
            return loader;
        }
        if (parent != null) {
            return parent.getLoader();
        }
        return null;
    }

    public synchronized void setLoader(ClassLoader loader) {
        ClassLoader oldLoader = this.loader;
        if (oldLoader == loader) {
            return;
        }
        this.loader = loader;
    }

    public String getName() {
        return (name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Container getParent() {
        return (parent);
    }

    public void setParent(Container container) {
        Container oldParent = this.parent;
        this.parent = container;
    }

    public void addChild(Container child) {
        addChildInternal(child);
    }

    private void addChildInternal(Container child) {
        synchronized (children) {
            if (children.get(child.getName()) != null) {
                throw new IllegalArgumentException("addChild:  Child name '" + child.getName() + "' is not unique");
            }
            child.setParent((Container) this);
            children.put(child.getName(), child);
        }
    }

    public Container findChild(String name) {
        if (name == null) {
            return (null);
        }
        synchronized (children) {       // Required by post-start changes
            return ((Container) children.get(name));
        }
    }

    public Container[] findChildren() {
        synchronized (children) {
            Container results[] = new Container[children.size()];
            return ((Container[]) children.values().toArray(results));
        }
    }

    public void removeChild(Container child) {
        synchronized (children) {
            if (children.get(child.getName()) == null) {
                return;
            }
            children.remove(child.getName());
        }
        child.setParent(null);
    }

    public Logger getLogger() {
        if (logger != null) {
            return logger;
        }
        if (parent != null) {
            return parent.getLogger();
        }
        return (null);
    }

    public synchronized void setLogger(Logger logger) {
        Logger oldLogger = this.logger;
        if (oldLogger == logger) {
            return;
        }
        this.logger = logger;
    }

    protected void log(String message) {
        Logger logger = getLogger();
        if (logger != null) {
            logger.log(logName() + ": " + message);
        } else {
            System.out.println(logName() + ": " + message);
        }
    }

    protected void log(String message, Throwable throwable) {
        Logger logger = getLogger();
        if (logger != null) {
            logger.log(logName() + ": " + message, throwable);
        } else {
            System.out.println(logName() + ": " + message + ": " + throwable);
            throwable.printStackTrace(System.out);
        }
    }

    protected String logName() {
        String className = this.getClass().getName();
        int period = className.lastIndexOf(".");
        if (period >= 0) className = className.substring(period + 1);
        return (className + "[" + getName() + "]");
    }

}
