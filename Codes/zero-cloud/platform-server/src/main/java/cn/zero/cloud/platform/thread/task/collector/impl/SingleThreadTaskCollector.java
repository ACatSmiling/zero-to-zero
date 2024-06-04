package cn.zero.cloud.platform.thread.task.collector.impl;

import cn.zero.cloud.platform.thread.task.collector.CommonTaskCollector;

import java.util.concurrent.Callable;

/**
 * The best practice is to create a business-level thread pool to use.
 */
public class SingleThreadTaskCollector extends CommonTaskCollector {
    @Override
    protected Object submitTask(Runnable task) {
        return task;
    }

    @Override
    protected Object submitTask(Callable task) {
        return task;
    }

    @Override
    protected boolean asyncDone(Object task) {
        if (task instanceof Runnable){
            Runnable runnable = (Runnable) task;
            runnable.run();
            return true;
        }
        return false;
    }

    @Override
    protected Object asyncGetResult(Object task) {
        if (task instanceof Callable) {
            Callable callable = (Callable) task;
            try {
                return callable.call();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
            }
        }
        return null;
    }
}
