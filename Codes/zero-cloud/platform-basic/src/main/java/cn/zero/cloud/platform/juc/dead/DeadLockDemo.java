package cn.zero.cloud.platform.juc.dead;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/9 0:20
 */
@Slf4j
public class DeadLockDemo {
    public static void main(String[] args) {
        final Object a = new Object();
        final Object b = new Object();

        new Thread(() -> {
            synchronized (a) {
                log.info("thread {} holds lock a and attempts to acquire lock b", Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (b) {
                    log.info("thread {} has acquired lock b", Thread.currentThread().getName());
                }
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (b) {
                log.info("thread {} holds lock b and attempts to acquire lock a", Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (a) {
                    log.info("thread {} has acquired lock a", Thread.currentThread().getName());
                }
            }
        }, "t2").start();
    }
}
