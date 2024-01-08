package cn.xisun.jvm;

/**
 * @author XiSun
 * @since 2024/1/5 22:29
 */
public class StackErrorTest {

    /**
     * 默认情况下，count值最大为：11410
     * 设置-Xss256k，count值最大为：2455
     */
    private static int count = 1;

    /**
     * 执行到最后，抛出异常：Exception in thread "main" java.lang.StackOverflowError
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(count);
        count++;
        main(args);
    }
}
