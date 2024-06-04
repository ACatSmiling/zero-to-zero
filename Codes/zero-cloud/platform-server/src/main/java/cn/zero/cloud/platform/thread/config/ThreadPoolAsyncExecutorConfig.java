package cn.zero.cloud.platform.thread.config;

import cn.zero.cloud.platform.thread.builder.DelegatingContextThreadPoolTaskExecutorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池任务执行器配置
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
@Configuration
@EnableAsync
public class ThreadPoolAsyncExecutorConfig implements AsyncConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolAsyncExecutorConfig.class);

    private final ThreadPoolParameterConfig threadPoolParameterConfig;

    @Autowired
    public ThreadPoolAsyncExecutorConfig(ThreadPoolParameterConfig threadPoolParameterConfig) {
        this.threadPoolParameterConfig = threadPoolParameterConfig;
    }

    @Bean(name = "SendNotificationExecutor")
    public ThreadPoolTaskExecutor sendNotificationTaskExecutor() {
        ThreadPoolTaskExecutor executor = DelegatingContextThreadPoolTaskExecutorBuilder.newInstance();
        executor.setCorePoolSize(threadPoolParameterConfig.getHighConcurrentThreadSize());
        executor.setKeepAliveSeconds(threadPoolParameterConfig.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("SendNotificationExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "VoiceaCallbackExecutor")
    public ThreadPoolTaskExecutor voiceaCallbackExecutor() {
        ThreadPoolTaskExecutor executor = DelegatingContextThreadPoolTaskExecutorBuilder.newInstance();
        executor.setCorePoolSize(threadPoolParameterConfig.getLowConcurrentThreadSize());
        executor.setKeepAliveSeconds(threadPoolParameterConfig.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("VoiceaCallbackExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "VoiceaConnectorExecutor")
    public ThreadPoolTaskExecutor voiceaConnectorExecutor() {
        ThreadPoolTaskExecutor executor = DelegatingContextThreadPoolTaskExecutorBuilder.newInstance();
        executor.setCorePoolSize(threadPoolParameterConfig.getHighConcurrentThreadSize());
        executor.setKeepAliveSeconds(threadPoolParameterConfig.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("VoiceaConnectorExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "RetryTaskExecutor")
    public ThreadPoolTaskExecutor retryTaskExexcutor() {
        ThreadPoolTaskExecutor executor = DelegatingContextThreadPoolTaskExecutorBuilder.newInstance();
        executor.setCorePoolSize(threadPoolParameterConfig.getLowConcurrentThreadSize());
        executor.setKeepAliveSeconds(threadPoolParameterConfig.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("RetryTaskExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "CreateRetryTaskExecutor")
    public ThreadPoolTaskExecutor createRetryTaskExecutor() {
        ThreadPoolTaskExecutor executor = DelegatingContextThreadPoolTaskExecutorBuilder.newInstance();
        executor.setCorePoolSize(threadPoolParameterConfig.getLowConcurrentThreadSize());
        executor.setKeepAliveSeconds(threadPoolParameterConfig.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("CreateRetryTaskExecutor-");
        executor.initialize();
        return executor;
    }

    /**
     * 1. 当异步方法执行抛出异常时，由于它不在主线程中，所以不能直接通过常规的try-catch块来捕获
     * 2. getAsyncUncaughtExceptionHandler()方法允许自定义如何处理这些未捕获的异常
     * 3. 当异步方法抛出异常时，Spring将调用配置的AsyncUncaughtExceptionHandler的handleUncaughtException()方法来处理这些异常
     * 4. 默认情况下，如果没有提供自定义的AsyncUncaughtExceptionHandler，Spring将使用一个默认的处理器，它仅仅会打印异常堆栈到标准错误输出
     * 5. 自定义异步异常的处理逻辑（例如，记录到日志、发送通知等），可以通过实现AsyncConfigurer接口并覆盖getAsyncUncaughtExceptionHandler()方法来提供你自己的处理器
     *
     * @return AsyncUncaughtExceptionHandler
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> LOGGER.error("Async Task Has Some Error: {}, {}, {}",
                ex.getMessage(),
                method.getDeclaringClass().getName() + "." + method.getName(),
                Arrays.toString(params));
    }
}
