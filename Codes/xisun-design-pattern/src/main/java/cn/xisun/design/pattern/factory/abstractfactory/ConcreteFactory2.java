package cn.xisun.design.pattern.factory.abstractfactory;

/**
 * 具体工厂2：所有方法创建的对象，构成一个产品族
 *
 * @author XiSun
 * @since 2023/11/21 21:38
 */
public class ConcreteFactory2 implements AbstractFactory {
    @Override
    public AbstractProductA createProductA() {
        return new ConcreteProductA2();
    }

    @Override
    public AbstractProductB createProductB() {
        return new ConcreteProductB2();
    }
}
