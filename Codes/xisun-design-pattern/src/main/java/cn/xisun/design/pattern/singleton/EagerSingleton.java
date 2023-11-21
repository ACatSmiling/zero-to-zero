package cn.xisun.design.pattern.singleton;

/**
 * 饿汉式
 *
 * @author XiSun
 * @since 2023/11/17 20:34
 */
public class EagerSingleton {

    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() {
    }

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
}
