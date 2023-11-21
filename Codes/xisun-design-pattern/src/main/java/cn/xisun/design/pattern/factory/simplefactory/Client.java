package cn.xisun.design.pattern.factory.simple;

/**
 * 客户端
 *
 * @author XiSun
 * @since 2023/11/20 23:01
 */
public class Client {
    public static void main(String[] args) {
        Product product;
        product = Factory.getProduct("A");// 通过工厂类创建产品对象
        product.methodSame();
        product.methodDiff();
    }
}
