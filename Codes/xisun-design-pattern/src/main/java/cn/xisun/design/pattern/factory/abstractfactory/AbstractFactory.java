package cn.xisun.design.pattern.factory.abstractfactory;

/**
 * 抽象工厂：所有方法创建的对象，构成一个产品族
 *
 * @author XiSun
 * @since 2023/11/21 21:31
 */
public interface AbstractFactory {
    AbstractProductA createProductA();

    AbstractProductB createProductB();
}
