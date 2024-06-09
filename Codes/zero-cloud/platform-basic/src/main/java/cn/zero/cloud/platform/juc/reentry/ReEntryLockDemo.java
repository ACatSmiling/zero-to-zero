package cn.zero.cloud.platform.juc.reentry;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/8 22:42
 */
@Slf4j
public class ReEntryLockDemo {
    public static void main(String[] args) {
        final Object o = new Object();

        /*
         * 隐式锁：
         * ---------------外层调用
         * ---------------中层调用
         * ---------------内层调用
         */
        new Thread(() -> {
            synchronized (o) {
                log.info("thread {} outer call", Thread.currentThread().getName());
                synchronized (o) {
                    log.info("thread {} middle call", Thread.currentThread().getName());
                    synchronized (o) {
                        log.info("thread {} inner call", Thread.currentThread().getName());
                    }
                }
            }
        }, "t1").start();

        // 隔离开隐式锁和显式锁的输出
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*
         * 显式锁：注意，加锁几次就需要解锁几次
         * ---------------外层调用
         * ---------------中层调用
         * ---------------内层调用
         */
        Lock lock = new ReentrantLock();
        new Thread(() -> {
            lock.lock();
            try {
                log.info("thread {} outer call", Thread.currentThread().getName());

                lock.lock();
                try {
                    log.info("thread {} middle call", Thread.currentThread().getName());

                    lock.lock();
                    try {
                        log.info("thread {} inner call", Thread.currentThread().getName());
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
