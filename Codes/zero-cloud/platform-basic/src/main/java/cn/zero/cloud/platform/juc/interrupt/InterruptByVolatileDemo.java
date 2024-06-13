package cn.zero.cloud.platform.juc.interrupt;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/13 23:46
 */
@Slf4j
public class InterruptByVolatileDemo {
    // volatile表示的变量具有可见性
    private static volatile boolean isStop = false;

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                if (isStop) {
                    log.info("thread {} ends execution", Thread.currentThread().getName());
                    break;
                }
                // 不要使用日志输出
                // log.info("thread {} is running", Thread.currentThread().getName());
            }
        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            isStop = true;
            log.info("thread {} ends execution", Thread.currentThread().getName());
        }, "t2").start();
    }
}
