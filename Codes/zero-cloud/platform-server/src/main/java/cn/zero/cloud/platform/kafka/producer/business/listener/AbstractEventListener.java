package cn.zero.cloud.platform.kafka.producer.business.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

/**
 * 事件监听处理模板
 */
public abstract class AbstractEventListener<T extends ApplicationEvent> implements ApplicationListener<T> {
    /**
     * 监听事件
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(T event) {
        if (isIgnoredEvent(event)) {
            return;
        }
        onEvent(event);
    }

    /**
     * 判断是否应该忽略一个事件，默认为true，子类需重写
     *
     * @param event 事件
     * @return 是否忽略
     */
    protected boolean isIgnoredEvent(T event) {
        return true;
    }

    /**
     * 处理未被忽略的事件
     *
     * @param event 事件
     */
    protected abstract void onEvent(T event);
}
