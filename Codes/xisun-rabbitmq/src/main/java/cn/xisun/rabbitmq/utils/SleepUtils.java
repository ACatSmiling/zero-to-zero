package cn.xisun.rabbitmq.utils;

/**
 * @author XiSun
 * @since 2023/10/13 13:46
 * <p>
 * 睡眠工具类
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
