package cn.xisun.jvm;

import com.sun.awt.SecurityWarning;
import com.sun.javafx.PlatformUtil;
import sun.misc.Launcher;

import java.net.URL;

/**
 * @author XiSun
 * @since 2024/1/4 21:59
 */
public class ClassLoaderTest1 {
    public static void main(String[] args) {

        System.out.println("**********引导类加载器**********");
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urLs) {
            System.out.println(url.toExternalForm());
        }

        // 从上面的路径中随意选择一个类，查看它的类加载器是什么
        // 以file:/C:/Users/XiSun/AppData/Local/Programs/Java/jdk1.8.0_201/jre/lib/rt.jar为例，取com.sun.awt.SecurityWarning
        ClassLoader classLoader = SecurityWarning.class.getClassLoader();
        System.out.println(classLoader);// null -> 类加载器是引导类加载器

        /*System.out.println("**********扩展类加载器**********");
        String extDirs = System.getProperty("java.ext.dirs");
        for (String path : extDirs.split(";")) {
            System.out.println(path);
        }*/

        // 从上面的路径中随意选择一个类，查看它的类加载器是什么
        // 以C:\Users\XiSun\AppData\Local\Programs\Java\jdk1.8.0_201\jre\lib\ext\jfxrt.jar为例，取com.sun.javafx.PlatformUtil
        ClassLoader classLoader1 = PlatformUtil.class.getClassLoader();
        System.out.println(classLoader1);// sun.misc.Launcher$ExtClassLoader@1540e19d -> 扩展类加载器
    }
}
