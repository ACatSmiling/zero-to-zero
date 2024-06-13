package cn.zero.cloud.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Xisun Wang
 * @since 2024/3/6 10:19
 */
@SpringBootApplication
public class PlatFormServerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(PlatFormServerApplication.class, args);

        String beanName = "SendNotificationExecutor";
        if (applicationContext.containsBean(beanName)) {
            Object myBean = applicationContext.getBean(beanName);
            // 现在你可以对 myBean 做任何你需要的操作
            System.out.println("Bean with name '" + beanName + "' is of type: " + myBean.getClass().getSimpleName());
        } else {
            System.out.println("No bean with name '" + beanName + "' is defined.");
        }
    }
}
