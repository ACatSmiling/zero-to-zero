package cn.xisun.spring;

import cn.xisun.spring.cycle.A;
import cn.xisun.spring.cycle.B;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author XiSun
 * @since 2023/10/22 11:16
 */
public class SpringCycleTest {
    public static void main(String[] args) {
        // 1.加载Spring配置类，创建IoC容器对象
//        ApplicationContext context = new AnnotationConfigApplicationContext(CycleConfig.class);
        // 1.加载Spring配置文件，创建IoC容器对象
        ApplicationContext context = new ClassPathXmlApplicationContext("cycleConfig.xml");

        // 2.根据id值获取配置类中的Bean实例对象和容器中注册的组件，要求使用返回的Bean的类型
        A a = context.getBean(A.class);
        B b = context.getBean(B.class);

        // 3.打印Bean
        System.out.println(a);
        System.out.println(b);
    }
}
