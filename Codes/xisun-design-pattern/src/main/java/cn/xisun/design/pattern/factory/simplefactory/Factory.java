package cn.xisun.design.pattern.factory.simple;

/**
 * 工厂类
 *
 * @author XiSun
 * @since 2023/11/20 19:50
 */
public class Factory {
    public static Product getProduct(String arg) {
        Product product = null;
        // 实际开发时，可以将参数作为配置文件读取，避免修改代码
        if ("A".equalsIgnoreCase(arg)) {
            product = new ConcreteProductA();
            // 初始化设置product
        }
        return product;
    }
}
