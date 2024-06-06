package cn.zero.cloud.platform.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Spring Application Context工具类
 *
 * @author Xisun Wang
 * @since 2024/4/15 12:15
 */
@Component
public class SpringApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * Spring容器启动时自动被调用，并传递ApplicationContext实例作为参数
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException BeansException
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContextUtil.applicationContext = applicationContext;
    }

    /**
     * 根据bean name获取容器中的bean
     *
     * @param beanName bean name
     * @return bean
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * 根据bean class获取容器中的bean
     *
     * @param clazz bean class
     * @param <T>   泛型
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 动态地向Spring容器中注册一个新的bean
     *
     * @param clazz       bean class
     * @param beanName    bean name
     * @param paramNames  bean param name
     * @param paramValues bean param value
     */
    public static void setBean(Class<?> clazz, String beanName, String[] paramNames, Object[] paramValues) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);

        for (int i = 0; i < paramNames.length; ++i) {
            beanDefinitionBuilder.addPropertyValue(paramNames[i], paramValues[i]);
        }

        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
    }
}
