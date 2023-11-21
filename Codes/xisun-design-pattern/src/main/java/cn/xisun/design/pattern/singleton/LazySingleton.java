package cn.xisun.design.pattern.singleton;

/**
 * 懒汉式
 *
 * @author XiSun
 * @since 2023/11/20 10:25
 */
public class LazySingleton {

    private static volatile LazySingleton instance = null;

    private LazySingleton() {

    }

    public static LazySingleton getInstance() {
        // 第一重判断
        if (instance == null) {
            synchronized (LazySingleton.class) {
                // 第二重判断
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}
