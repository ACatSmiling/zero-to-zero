package java.lang;

/**
 * @author XiSun
 * @since 2024/1/5 11:21
 */
public class String {

    static {
        System.out.println("加载自定义的java.lang.String");
    }

    /**
     * 执行报错：
     *  错误: 在类 java.lang.String 中找不到 main 方法, 请将 main 方法定义为:
     *      public static void main(String[] args)
     *  否则 JavaFX 应用程序类必须扩展javafx.application.Application
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("执行自定义的java.lang.String中的main方法");
    }
}
