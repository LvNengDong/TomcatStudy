package geek.tomcat.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @Author lnd
 * @Description 加载本地的字节码文件
 * @Date 2024/5/12 20:40
 */
public class ClassLoaderUtil {

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        String filePath = "/Volumes/DEV/code/TomcatStudy/miniTomcat/webroot/";
        String className = "geek.tomcat.test.HelloServlet";
        Class<?> aClass = loadClassByDir(filePath, className);
        System.out.println(aClass);
    }

    /**
     * 根据文件目录加载类
     * @param filePath 包含 .class 文件的目录（相对于当前工作目录或绝对路径）
     * @param className 类名（注意：这个类名必须是全限定类名）
     * @return
     */
    public static Class<?> loadClassByDir(String filePath, String className) throws ClassNotFoundException, MalformedURLException {
        File classFile = new File(filePath);
        if (!classFile.exists()) {
            throw new IllegalArgumentException("Class file not found: " + filePath);
        }
        // 将文件转换为 file://URL
        URL classUrl = classFile.toURI().toURL();
        URL[] urls = {classUrl};
        URLClassLoader classLoader = new URLClassLoader(urls);

        // 使用类加载器加载类
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            throw e;
        }
        return clazz;
    }
}
