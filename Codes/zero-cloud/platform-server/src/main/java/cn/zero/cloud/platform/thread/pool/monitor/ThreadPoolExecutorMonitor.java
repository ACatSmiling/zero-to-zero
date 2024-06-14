package cn.zero.cloud.platform.thread.pool.monitor;

import cn.zero.cloud.platform.thread.excutor.DelegatingContextThreadPoolTaskExecutor;
import cn.zero.cloud.platform.thread.telemetry.ThreadPoolStateTelemetryLog;
import cn.zero.cloud.platform.thread.task.collector.CommonTaskCollector;
import cn.zero.cloud.platform.thread.task.collector.impl.MultipleThreadTaskCollector;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 定义一个Executor监视器
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
@Component
@ConditionalOnProperty(
        name = {"zero.cloud.thread.task.executor.monitor.enable"},
        havingValue = "true",
        matchIfMissing = false
)
@EnableScheduling
public class ThreadPoolExecutorMonitor {
    /**
     * Spring容器中注入的task收集器
     */
    private final List<CommonTaskCollector> taskCollectors;

    /**
     * Spring容器中注入的ThreadPool Executors
     */
    private final List<Executor> threadPoolTaskExecutors;

    public ThreadPoolExecutorMonitor(@Autowired(required = false) List<CommonTaskCollector> taskCollectors,
                                     @Autowired(required = false) List<Executor> threadPoolTaskExecutors) {
        this.taskCollectors = taskCollectors;
        this.threadPoolTaskExecutors = threadPoolTaskExecutors;
    }

    /**
     * 定时任务：监控Spring容器中注入的所有Executor，并记录每隔Executor的状态日志
     * 1.   0 0 * * * ?  每隔1小时执行一次
     * 2.   0/30 * * * * ?  默认每隔30秒执行一次
     */
    @Scheduled(cron = "${zero.cloud.thread.task.executor.monitor.cron:0/30 * * * * ?}")
    public void printThreadPoolStateLog() {
        if (CollectionUtils.isNotEmpty(taskCollectors)) {
            for (CommonTaskCollector commonTaskCollector : taskCollectors) {
                if (commonTaskCollector instanceof MultipleThreadTaskCollector multipleThreadTaskCollector) {
                    DelegatingContextThreadPoolTaskExecutor executor = multipleThreadTaskCollector.getExecutor();
                    writeThreadPoolStateTelemetryLog(executor);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(threadPoolTaskExecutors)) {
            for (Executor executor : threadPoolTaskExecutors) {
                if (executor instanceof ThreadPoolTaskExecutor) {
                    writeThreadPoolStateTelemetryLog((ThreadPoolTaskExecutor) executor);
                }
            }
        }
    }

    private void writeThreadPoolStateTelemetryLog(ThreadPoolTaskExecutor executor) {
        String poolName;
        if (executor instanceof DelegatingContextThreadPoolTaskExecutor) {
            poolName = ((DelegatingContextThreadPoolTaskExecutor) executor).getPoolName();
        } else {
            return;
        }
        ThreadPoolStateTelemetryLog telemetryLog = new ThreadPoolStateTelemetryLog(poolName);
        telemetryLog.setCorePoolSize(executor.getCorePoolSize());
        telemetryLog.setMaxPoolSize(executor.getMaxPoolSize());
        telemetryLog.setCurrentPoolSize(executor.getPoolSize());
        telemetryLog.setCurrentTaskCount(executor.getActiveCount());
        telemetryLog.setQueueWaitingCount(executor.getQueueSize());
        telemetryLog.writeLog();
    }
}