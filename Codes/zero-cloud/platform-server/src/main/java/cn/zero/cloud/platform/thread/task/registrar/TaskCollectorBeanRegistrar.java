package cn.zero.cloud.platform.thread.task.registrar;

import cn.zero.cloud.platform.thread.excutor.DelegatingContextThreadPoolTaskExecutor;
import cn.zero.cloud.platform.thread.task.collector.CommonTaskCollector;
import cn.zero.cloud.platform.thread.task.collector.impl.MultipleThreadTaskCollector;
import cn.zero.cloud.platform.thread.task.collector.impl.SingleThreadTaskCollector;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * task收集器初始化并注册
 */
@Component
public class TaskCollectorBeanRegistrar implements BeanPostProcessor {
    public static final String CORE_POOL_SIZE_KEY = "corePoolSize";

    public static final int DEFAULT_CORE_POOL_SIZE = 1;

    public static final String MAX_POOL_SIZE_KEY = "maxPoolSize";

    public static final int DEFAULT_MAX_POOL_SIZE = 5;

    public static final String KEEP_LIVE_TIME_KEY = "keepLiveTime";

    public static final int DEFAULT_KEEP_LIVE_SECONDS = 60;

    public static final String WAIT_QUEUE_SIZE_KEY = "waitQueueSize";

    public static final int DEFAULT_WAIT_QUEUE_SIZE = 300;

    public static final String CONFIG_SEPARATOR = ",";

    public static final String KEY_VALUE_SEPARATOR = "=";


    @Value("${platform.task.collector.multiple.thread.enable:true}")
    private boolean multiThreadEnable;

    /**
     * 初始化并注册task收集器到Spring的应用上下文
     *
     * @param taskCollectorSpec          包含任务收集器配置的Map，容器中被注入的Map<String, String>对象
     * @param defaultListableBeanFactory Spring的bean工厂，用于注册和管理应用程序中的bean
     */
    @Autowired
    public void init(Map<String, String> taskCollectorSpec, DefaultListableBeanFactory defaultListableBeanFactory) {
        if (MapUtils.isNotEmpty(taskCollectorSpec)) {
            for (Map.Entry<String, String> entry : taskCollectorSpec.entrySet()) {
                String beanName = entry.getKey();
                if (!defaultListableBeanFactory.containsBeanDefinition(beanName)) {
                    CommonTaskCollector commonTaskCollector = multiThreadEnable ? buildMultipleThreadTaskCollector(entry.getKey(), entry.getValue()) : buildSingleThreadTaskCollector();
                    defaultListableBeanFactory.registerSingleton(beanName, commonTaskCollector);
                }
            }
        }
    }

    /**
     * 创建SingleThreadTaskCollector
     *
     * @return SingleThreadTaskCollector
     */
    private SingleThreadTaskCollector buildSingleThreadTaskCollector() {
        return new SingleThreadTaskCollector();
    }

    /**
     * 解析task收集器的参数配置，并创建MultipleThreadTaskCollector
     *
     * @param beanName task收集器名称
     * @param value    task收集器具体参数配置
     * @return MultipleThreadTaskCollector
     */
    private MultipleThreadTaskCollector buildMultipleThreadTaskCollector(String beanName, String value) {
        int corePoolSize = DEFAULT_CORE_POOL_SIZE;
        int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
        int keepAliveSeconds = DEFAULT_KEEP_LIVE_SECONDS;
        int queueSize = DEFAULT_WAIT_QUEUE_SIZE;
        if (StringUtils.isNotBlank(value)) {
            String[] configs = value.split(CONFIG_SEPARATOR);
            for (String config : configs) {
                String[] keyValue = config.split(KEY_VALUE_SEPARATOR);
                if (keyValue.length == 2) {
                    switch (keyValue[0]) {
                        case CORE_POOL_SIZE_KEY:
                            corePoolSize = Integer.parseInt(keyValue[1]);
                            break;
                        case MAX_POOL_SIZE_KEY:
                            maxPoolSize = Integer.parseInt(keyValue[1]);
                            break;
                        case KEEP_LIVE_TIME_KEY:
                            keepAliveSeconds = Integer.parseInt(keyValue[1]);
                            break;
                        case WAIT_QUEUE_SIZE_KEY:
                            queueSize = Integer.parseInt(keyValue[1]);
                            break;
                        default:
                    }
                }
            }
        }
        return new MultipleThreadTaskCollector(new DelegatingContextThreadPoolTaskExecutor(beanName, corePoolSize, maxPoolSize, keepAliveSeconds, queueSize));
    }
}
