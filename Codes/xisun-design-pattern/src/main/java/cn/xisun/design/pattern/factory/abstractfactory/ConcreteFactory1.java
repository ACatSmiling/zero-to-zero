package cn.xisun.design.pattern.factory.abstractfactory;

/**
 * 具体工厂1：所有方法创建的对象，构成一个产品族
 *
 * @author XiSun
 * @since 2023/11/21 21:33
 */
public class ConcreteFactory1 implements AbstractFactory {
    @Override
    public AbstractProductA createProductA() {
        return new ConcreteProductA1();
    }

    @Override
    public AbstractProductB createProductB() {
        return new ConcreteProductB1();
    }
}
