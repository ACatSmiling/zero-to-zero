package cn.zero.cloud.platform.thread.task.collector.impl;

import cn.zero.cloud.platform.thread.excutor.DelegatingContextThreadPoolTaskExecutor;
import cn.zero.cloud.platform.thread.task.collector.CommonTaskCollector;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The best practice is to create a business-level thread pool to use.
 */
public class MultipleThreadTaskCollector extends CommonTaskCollector {

    private final DelegatingContextThreadPoolTaskExecutor executor;

    public MultipleThreadTaskCollector(DelegatingContextThreadPoolTaskExecutor executor) {
        super();
        this.executor = executor;
    }

    public DelegatingContextThreadPoolTaskExecutor getExecutor() {
        return executor;
    }

    @Override
    protected Object submitTask(Runnable task) {
        return executor.submit(task);
    }

    @Override
    protected Object submitTask(Callable task) {
        return executor.submit(task);
    }

    @Override
    protected boolean asyncDone(Object task) {
        if (task instanceof Future<?>) {
            try {
                getResultFromFuture((Future<?>) task);
                return true;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception ignore) {

            }
        }
        return false;
    }

    @Override
    protected Object asyncGetResult(Object task) {
        if (task instanceof Future<?>) {
            try {
                return getResultFromFuture((Future<?>) task);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception ignore) {

            }
        }
        return null;
    }

    private Object getResultFromFuture(Future<?> taskFuture) throws Exception {
        try {
            return taskFuture.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw e;
            }
        }
    }

}
