package cn.zero.cloud.platform.thread.task.collector;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * The best practice is to create a business-level thread pool to use.
 *
 * @author Xisun Wang
 * @since 2024/4/12 17:17
 */
public abstract class CommonTaskCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonTaskCollector.class);

    private final ThreadLocal<List<String>> threadTaskKeys;

    private final ThreadLocal<Map<String, Object>> threadTasks;

    protected abstract Object submitTask(Runnable task);

    protected abstract Object submitTask(Callable task);

    protected abstract boolean asyncDone(Object task);

    protected abstract Object asyncGetResult(Object task);

    protected CommonTaskCollector() {
        this.threadTaskKeys = new ThreadLocal<>();
        this.threadTasks = new ThreadLocal<>();
    }

    /**
     * submit a runnable task, then return a taskKey
     *
     * @param runnable
     * @return
     */
    public String submit(Runnable runnable) {
        String hashCode = String.valueOf(runnable.hashCode());
        addTask(hashCode, submitTask(runnable));
        return hashCode;
    }

    /**
     * submit a callable task, then return a taskKey
     *
     * @param callable
     * @param <T>
     * @return
     */
    public <T> String submit(Callable<T> callable) {
        String hashCode = String.valueOf(callable.hashCode());
        addTask(hashCode, submitTask(callable));
        return hashCode;
    }

    /**
     * for {@link #submit(Runnable)}
     * waiting the task to be done
     * this task will be removed from tasks
     *
     * @param taskKey
     * @return whether the task was successfully completed
     */
    public boolean asyncDone(String taskKey) {
        Object task = getTasks().get(taskKey);
        boolean result = false;
        try {
            result = asyncDone(task);
        } finally {
            removeTask(taskKey);
        }
        return result;
    }

    /**
     * for {@link #submit(Callable)}
     * waiting for the task to be done, and return a cast value
     * this task will be removed from tasks
     *
     * @param taskKey
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T asyncGetResult(String taskKey, Class<T> clz) {
        Object task = getTasks().get(taskKey);
        if (task != null) {
            try {
                Object result = asyncGetResult(task);
                if (null == result) {
                    return null;
                }
                return clz.cast(result);
            } finally {
                removeTask(taskKey);
            }
        }
        return null;
    }

    /**
     * for {@link #submit(Callable)}
     * waiting for all previously async submitted tasks to be done, then return results
     * all tasks will be cleared before return
     *
     * @return
     */
    public Map<String, Object> asyncGetResults() {
        List<String> taskKeys = threadTaskKeys.get();
        Map<String, Object> results = new LinkedHashMap<>();
        Map<String, Object> tasks = getTasks();
        if (CollectionUtils.isEmpty(taskKeys)) {
            return results;
        }
        for (String taskKey : taskKeys) {
            Object task = tasks.get(taskKey);
            Object result = null;
            if (task != null) {
                try {
                    result = asyncGetResult(task);
                } catch (Exception e) {
                    LOGGER.error("thread pool task execute error!", e);
                }
            }
            results.put(taskKey, result);
        }
        clear();
        return results;
    }

    /**
     * clear thread local
     */
    public void clear() {
        threadTasks.remove();
        threadTaskKeys.remove();
    }

    private void addTask(String taskKey, Object task) {
        List<String> taskKeys = threadTaskKeys.get();
        if (taskKeys == null) {
            List<String> newTaskKeys = new ArrayList<>();
            newTaskKeys.add(taskKey);
            threadTaskKeys.set(newTaskKeys);
        } else {
            taskKeys.add(taskKey);
        }

        Map<String, Object> tasks = threadTasks.get();
        if (tasks == null) {
            Map<String, Object> newTasks = new HashMap<>();
            newTasks.put(taskKey, task);
            threadTasks.set(newTasks);
        } else {
            tasks.put(taskKey, task);
        }
    }

    private Map<String, Object> getTasks() {
        if (threadTasks.get() == null) {
            Map<String, Object> newTasks = new HashMap<>();
            threadTasks.set(newTasks);
            return newTasks;
        } else {
            return threadTasks.get();
        }
    }

    private void removeTask(String taskKey) {
        Map<String, Object> tasks = threadTasks.get();
        if (tasks != null) {
            tasks.remove(taskKey);
        }
        List<String> taskKeys = threadTaskKeys.get();
        if (taskKeys != null) {
            taskKeys.remove(taskKey);
        }
    }
}
