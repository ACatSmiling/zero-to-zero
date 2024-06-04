package cn.zero.cloud.platform.thread.local.transmitter;

/**
 * Thread Pool ThreadLocal Transmitter
 */
public interface ThreadPoolThreadLocalTransmitter<T> {
    /**
     * 获取ThreadLocal
     *
     * @return ThreadLocal
     */
    T get();

    /**
     * 设置ThreadLocal
     *
     * @param context
     */
    void set(T context);

    /**
     * 清空ThreadLocal
     */
    void clear();
}
