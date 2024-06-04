package cn.zero.cloud.platform.thread.task;

import cn.zero.cloud.platform.common.utils.SpringApplicationContextUtil;
import cn.zero.cloud.platform.thread.pool.configurer.ThreadPoolConfigurationBean;
import cn.zero.cloud.platform.thread.telemetry.ThreadPoolDecoratorTelemetryLog;
import cn.zero.cloud.platform.thread.telemetry.ThreadPoolExecuteTelemetryLog;
import cn.zero.cloud.platform.thread.local.transmitter.ThreadPoolThreadLocalTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 托管任务，在task的基础上，包装一层
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
public class DelegatingContextTask<V> implements Callable<V>, Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DelegatingContextTask.class);

    private Runnable runnableTask;

    private Callable<V> callableTask;

    private String poolName;

    private Thread callerThread;

    private long submitTimestamp;

    /**
     * ThreadLocal Transmitter
     */
    private Map<ThreadPoolThreadLocalTransmitter, Object> threadLocalTransmitter;

    /**
     * Decorator TelemetryLog
     */
    private ThreadPoolDecoratorTelemetryLog decoratorTelemetryLog;

    private static final String TRACKING_ID = "trackingID";


    /**
     * Runnable task 封装
     *
     * @param runnableTask Runnable task
     * @param poolName     poolName
     */
    public DelegatingContextTask(Runnable runnableTask, String poolName) {
        this.runnableTask = runnableTask;
        initParams(poolName);
    }

    /**
     * Callable task 封装
     *
     * @param callableTask Callable task
     * @param poolName     poolName
     */
    public DelegatingContextTask(Callable<V> callableTask, String poolName) {
        this.callableTask = callableTask;
        initParams(poolName);
    }

    /**
     * 初始化参数
     *
     * @param poolName poolName
     */
    private void initParams(String poolName) {
        this.poolName = poolName;
        this.callerThread = Thread.currentThread();
        this.submitTimestamp = System.currentTimeMillis();
        // 获取Spring容器中的ThreadPoolConfigurationBean配置对象
        ThreadPoolConfigurationBean configuration = SpringApplicationContextUtil.getBean(ThreadPoolConfigurationBean.class);
        this.threadLocalTransmitter = configuration.getTransmitters().stream().collect(HashMap::new, (m, v) -> m.put(v, v.get()), HashMap::putAll);
        this.decoratorTelemetryLog = configuration.getDecorator();
    }

    /**
     * Runnable task run
     */
    @Override
    public void run() {
        try {
            doExecuteTask(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callable task run
     *
     * @return result
     * @throws Exception exception
     */
    @Override
    public V call() throws Exception {
        return doExecuteTask(false);
    }

    /**
     * 执行task，并按需包装
     *
     * @param isRunnableTask 是否是RunnableTask
     * @return task执行返回的结果
     * @throws Exception exception
     */
    private V doExecuteTask(boolean isRunnableTask) throws Exception {
        long startTime = System.currentTimeMillis();
        boolean isCallerThread = isTaskRunInCallerThread();
        if (!isCallerThread) {
            initThreadLocal();
        }
        Exception exception = null;
        try {
            if (isRunnableTask) {
                this.runnableTask.run();
                return null;
            } else {
                return callableTask.call();
            }
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            if (!isCallerThread) {
                clearThreadLocal();
            }
            writeTelemetryLog(isCallerThread, startTime, exception);
        }
    }

    /**
     * 判断task的caller thread是否是当前线程
     *
     * @return task的caller thread是否是当前线程
     */
    private boolean isTaskRunInCallerThread() {
        return Thread.currentThread().equals(callerThread);
    }

    /**
     * 初始化ThreadLocal
     */
    private void initThreadLocal() {
        threadLocalTransmitter.forEach(ThreadPoolThreadLocalTransmitter::set);
    }

    /**
     * 清空ThreadLocal
     */
    private void clearThreadLocal() {
        threadLocalTransmitter.keySet().forEach(ThreadPoolThreadLocalTransmitter::clear);
    }

    /**
     * 日志记录
     *
     * @param isCallerThread task的caller thread是否是当前线程
     * @param startTime      task执行时的开始时间
     * @param exception      task执行时抛出的异常
     */
    private void writeTelemetryLog(boolean isCallerThread, Long startTime, Exception exception) {
        // task等待时间
        long waitTime = startTime - submitTimestamp;
        // task执行时间
        long executeTime = System.currentTimeMillis() - startTime;
        boolean subClassInstance = false;
        ThreadPoolExecuteTelemetryLog telemetryLog;
        try {
            if (decoratorTelemetryLog != null) {
                telemetryLog = (ThreadPoolExecuteTelemetryLog) decoratorTelemetryLog.getTClass().getDeclaredConstructor().newInstance();
                subClassInstance = true;
            } else {
                telemetryLog = new ThreadPoolExecuteTelemetryLog();
            }
        } catch (Exception e) {
            LOGGER.error("create subclass instance error, use default instead, exception: ", e);
            telemetryLog = new ThreadPoolExecuteTelemetryLog();
        }

        telemetryLog.setTaskRunner(isCallerThread ? "UserThread" : "PoolThread");
        telemetryLog.setFeatureType(poolName);
        telemetryLog.setWaitTime(waitTime);
        telemetryLog.setCostTime(executeTime);
        telemetryLog.setTrackingID(MDC.get(TRACKING_ID));
        if (exception != null) {
            telemetryLog.setExecuteResult("fail");
            telemetryLog.setFailReason(exception.getMessage());
        }
        if (decoratorTelemetryLog != null && subClassInstance) {
            decoratorTelemetryLog.decorate(telemetryLog);
        }

        telemetryLog.writeLog();
    }
}
