package cn.xisun.design.pattern.factory.factorymethod;

/**
 * 客户端
 *
 * @author XiSun
 * @since 2023/11/21 10:32
 */
public class Client {
    public static void main(String[] args) {
        Factory factory;
        // 具体工厂类的全类名，可以通过配置文件绑定，避免修改代码
        factory = new ConcreteFactory();
        Product product;
        product = factory.factoryMethod();
    }
}
