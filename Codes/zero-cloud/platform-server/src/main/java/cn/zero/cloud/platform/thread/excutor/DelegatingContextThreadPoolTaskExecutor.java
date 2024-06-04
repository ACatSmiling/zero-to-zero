package cn.zero.cloud.platform.thread.excutor;

import cn.zero.cloud.platform.thread.task.DelegatingContextTask;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public final class DelegatingContextThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    private String poolName;

    public DelegatingContextThreadPoolTaskExecutor() {

    }

    public DelegatingContextThreadPoolTaskExecutor(String beanName, int corePoolSize, int maxPoolSize, int keepAliveSeconds, int queueSize) {
        setBeanName(beanName);
        super.setCorePoolSize(corePoolSize);
        super.setMaxPoolSize(maxPoolSize);
        super.setKeepAliveSeconds(keepAliveSeconds);
        super.setQueueCapacity(queueSize);
        super.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        super.initialize();
    }

    @Override
    public void setBeanName(@NonNull String name) {
        super.setBeanName(name);
        this.poolName = name;
    }

    public String getPoolName() {
        return poolName;
    }

    @Override
    public void execute(@NonNull Runnable task) {
        super.execute(new DelegatingContextTask<>(task, poolName));
    }

    @Override
    public Future<?> submit(@NonNull Runnable task) {
        return super.submit((Runnable) new DelegatingContextTask<>(task, poolName));
    }

    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return super.submit((Callable<T>) new DelegatingContextTask<>(task, poolName));
    }

    @Override
    public ListenableFuture<?> submitListenable(@NonNull Runnable task) {
        return super.submitListenable((Runnable) new DelegatingContextTask<>(task, poolName));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(@NonNull Callable<T> task) {
        return super.submitListenable((Callable<T>) new DelegatingContextTask<>(task, poolName));
    }
}