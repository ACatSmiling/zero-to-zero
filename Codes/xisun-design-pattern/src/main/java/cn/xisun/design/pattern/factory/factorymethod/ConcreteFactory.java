package cn.xisun.design.pattern.factory.factorymethod;

/**
 * 具体工厂
 *
 * @author XiSun
 * @since 2023/11/21 10:30
 */
public class ConcreteFactory implements Factory {
    @Override
    public Product factoryMethod() {
        return new ConcreteProduct();
    }
}
