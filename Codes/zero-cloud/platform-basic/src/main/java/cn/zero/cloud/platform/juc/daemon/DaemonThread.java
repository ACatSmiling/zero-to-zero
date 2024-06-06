package cn.zero.cloud.platform.juc.daemon;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/4 23:04
 */
@Slf4j
public class DaemonThread {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.info("current thread: {}, it is a: {}", Thread.currentThread().getName(), (Thread.currentThread().isDaemon() ? "daemon thread" : "user thread"));

            while (true) {
                log.info("{} is executing business", Thread.currentThread().getName());

                // 便于查看日志
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    log.error("exception: ", e);
                }
            }
        }, "t1");
        // 通过设置属性Daemon来设置当前线程是否为守护线程
        t1.setDaemon(true);
        t1.start();

        // 便于查看日志
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error("exception: ", e);
        }

        log.info("{} thread ends execution", Thread.currentThread().getName());
    }
}
