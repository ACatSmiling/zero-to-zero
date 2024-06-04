package cn.zero.cloud.platform.telemetry.handler;

import cn.zero.cloud.platform.telemetry.pojo.AsyncLogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xisun Wang
 * @since 2024/3/21 13:10
 */
public class AsyncLogEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncLogEventHandler.class);

    private static final AsyncLogEventHandler ASYNC_LOG_EVENT_HANDLER_INSTANCE = new AsyncLogEventHandler();

    private static final int QUEUE_CAPACITY = 1000;

    // 线程安全的阻塞队列
    private final LinkedBlockingQueue<AsyncLogEvent> logQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
        // 自定义ThreadFactory
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setDaemon(true);
        return thread;
    });

    private final AtomicBoolean running = new AtomicBoolean(true);

    private AsyncLogEventHandler() {
        this.executor.submit(() -> {
            while (running.get()) {
                AsyncLogEventHandler.this.consumeLog();
            }
        });
    }

    public static AsyncLogEventHandler getAsyncLogEventHandlerInstance() {
        return ASYNC_LOG_EVENT_HANDLER_INSTANCE;
    }

    /**
     * 添加日志
     *
     * @param asyncLogEvent 日志事件
     */
    public void handle(AsyncLogEvent asyncLogEvent) {
        // 如果logQueue已满，会返回false
        boolean isSuccess = this.logQueue.offer(asyncLogEvent);
        if (!isSuccess) {
            // 当队列满时，立即同步写入日志
            LOGGER.warn("Asynchronous log buffer is full, will output it without buffer!");
            this.write(asyncLogEvent);
        }
    }

    /**
     * 消费日志
     */
    private void consumeLog() {
        try {
            // take()方法会阻塞，如果队列为空，consumeLog方法将会等待，直到队列中有新的日志事件可用
            AsyncLogEvent logEvent = this.logQueue.take();
            this.write(logEvent);
        } catch (Exception e) {
            LOGGER.error("Asynchronous log consume failed!", e);
        }
    }

    /**
     * 输出日志
     *
     * @param asyncLogEvent 日志事件
     */
    private void write(AsyncLogEvent asyncLogEvent) {
        try {
            Logger logger = asyncLogEvent.getLogger();
            String logContent = asyncLogEvent.getLog();
            // 因为是切面日志，使用info级别输出
            logger.info(logContent);
        } catch (Exception e) {
            LOGGER.error("Asynchronous log write failed!", e);
        }
    }

    public void stopHandler() {
        running.set(false);
        executor.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executor.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("ExecutorService did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
