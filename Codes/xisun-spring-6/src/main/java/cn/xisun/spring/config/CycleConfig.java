package cn.xisun.spring.config;

import cn.xisun.spring.cycle.A;
import cn.xisun.spring.cycle.B;
import jakarta.annotation.Resource;
import org.springframework.aot.hint.annotation.Reflective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author XiSun
 * @since 2023/10/22 11:13
 */
// @Configuration
@ComponentScan("cn.xisun.spring")
public class CycleConfig {
    @Resource
    private A a;

    @Resource
    private B b;

    @Bean(name = "a")
    public A getA() {
        System.out.println("11: " + b);
        A a = new A();
        a.setName("this is a");
        a.setB(b);
        return a;
    }

    @Bean(name = "b")
    public B getB() {
        System.out.println("22: " + a);
        B b = new B();
        b.setName("this is b");
        b.setA(a);
        return b;
    }
}
