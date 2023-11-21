package cn.xisun.design.pattern.singleton;

/**
 * IoDH： Initialization on Demand Holder
 *
 * @author XiSun
 * @since 2023/11/20 10:39
 */
public class IoDHSingleton {
    private IoDHSingleton() {

    }

    private static class HolderClass {
        private static final IoDHSingleton INSTANCE = new IoDHSingleton();
    }

    /**
     *
     * @return
     */
    public static IoDHSingleton getInstance() {
        return HolderClass.INSTANCE;
    }
}
