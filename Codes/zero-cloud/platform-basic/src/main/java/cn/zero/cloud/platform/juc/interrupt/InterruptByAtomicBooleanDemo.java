package cn.zero.cloud.platform.juc.interrupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/13 23:56
 */
@Slf4j
public class InterruptByAtomicBooleanDemo {
    private static final AtomicBoolean ATOMIC_BOOLEAN = new AtomicBoolean(false);

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                if (ATOMIC_BOOLEAN.get()) {
                    log.info("thread {} ends execution", Thread.currentThread().getName());
                    break;
                }
                // 不要使用日志输出
                log.info("thread {} is running", Thread.currentThread().getName());
                
                try {
                    TimeUnit.MILLISECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            ATOMIC_BOOLEAN.set(true);
            log.info("thread {} ends execution", Thread.currentThread().getName());
        }, "t2").start();
    }
}
