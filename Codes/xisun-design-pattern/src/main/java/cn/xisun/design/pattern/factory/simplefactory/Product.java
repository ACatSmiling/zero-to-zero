package cn.xisun.design.pattern.factory.simplefactory;

/**
 * 抽象产品类
 *
 * @author XiSun
 * @since 2023/11/20 19:48
 */
public abstract class Product {
    /**
     * 声明所有产品类的公共业务方法
     */
    public void methodSame() {
        // 公共方法的实现
    }

    /**
     * 声明抽象业务方法
     */
    public abstract void methodDiff();
}
