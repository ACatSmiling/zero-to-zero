package cn.xisun.jvm;

/**
 * @author XiSun
 * @since 2024/1/4 22:50
 */
public class ClassLoaderTest2 {
    public static void main(String[] args) {
        try {
            ClassLoader classLoader = Class.forName("java.lang.String").getClassLoader();
            System.out.println(classLoader);// null
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader);// sun.misc.Launcher$AppClassLoader@18b4aac2

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);// sun.misc.Launcher$AppClassLoader@18b4aac2

    }
}
