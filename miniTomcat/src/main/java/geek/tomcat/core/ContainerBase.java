package geek.tomcat.core;

import geek.tomcat.Container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lnd
 * @Description 因为存在多层 Container，很多特性是共有的，所以我们定义 ContainerBase 作为基础类
 * @Date 2025/1/8 15:32
 */
public abstract class ContainerBase implements Container {
    //子容器
    protected Map<String, Container> children = new ConcurrentHashMap<>();

    //类加载器
    protected ClassLoader loader = null;

    protected String name = null;

    //父容器
    protected Container parent = null;

    //下面是基本的get和set方法
    public abstract String getInfo();

    public ClassLoader getLoader() {
        if (loader != null) {
            return (loader);
        }
        if (parent != null) {
            return (parent.getLoader());
        }
        return (null);
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

    //下面是对children map的增删改查操作
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
}
