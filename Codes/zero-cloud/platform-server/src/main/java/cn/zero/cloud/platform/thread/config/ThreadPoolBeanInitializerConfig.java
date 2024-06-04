package cn.zero.cloud.platform.thread.config;

import cn.zero.cloud.platform.thread.local.transmitter.ThreadPoolThreadLocalTransmitter;
import cn.zero.cloud.platform.thread.local.transmitter.impl.*;
import cn.zero.cloud.platform.thread.pool.configurer.ThreadPoolConfigurer;
import cn.zero.cloud.platform.thread.pool.configurer.ThreadPoolConfigurationBean;
import cn.zero.cloud.platform.thread.telemetry.ThreadPoolDecoratorTelemetryLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 线程池Bean初始化配置
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
@Configuration
public class ThreadPoolBeanInitializerConfig {
    /**
     * 根据platform.task.collector.spec配置，向容器中注入需要的task收集器
     *
     * @return task收集器集合
     */
    @Bean
    @ConfigurationProperties(prefix = "platform.task.collector.spec")
    public Map<String, String> taskCollectorSpec() {
        return new HashMap<>();
    }

    /**
     * 向容器中注入一个ThreadPoolConfig对象
     *
     * @param configurer ThreadPoolConfigurer
     * @return ThreadPoolConfig
     */
    @Bean("threadPoolConfigurationBean")
    public ThreadPoolConfigurationBean threadPoolConfiguration(@Autowired(required = false) ThreadPoolConfigurer configurer) {
        List<ThreadPoolThreadLocalTransmitter> transmitters = initDefaultTransmitter();
        ThreadPoolDecoratorTelemetryLog decorator = null;
        if (configurer != null) {
            configurer.addThreadPoolThreadLocalTransmitters(transmitters);
            decorator = configurer.getThreadPoolTelemetryLogDecorator();
        }
        return new ThreadPoolConfigurationBean(transmitters, decorator);
    }

    /**
     * 初始化默认的ThreadPool ThreadLocal Transmitter
     *
     * @return List<ThreadPoolThreadLocalTransmitter>
     */
    private List<ThreadPoolThreadLocalTransmitter> initDefaultTransmitter() {
        List<ThreadPoolThreadLocalTransmitter> transmitters = new ArrayList<>();
        transmitters.add(new MDCContextTransmitter());
        transmitters.add(new RequestContextTransmitter());
        transmitters.add(new SecurityContextHolderTransmitter());
        transmitters.add(new WbxServerContextTransmitter());
        transmitters.add(new DBRoutingContextTransmitter());
        return transmitters;
    }
}
