package cn.zero.cloud.platform.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/7 23:34
 */
@Slf4j
public class LockDemo1 {
    private static class Phone {
        public synchronized void sendEmail() {
            log.info("thread {} send email", Thread.currentThread().getName());
        }

        public synchronized void sendSms() {
            log.info("thread {} send sms", Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        Phone phone = new Phone();

        new Thread(phone::sendEmail, "a").start();

        // main线程暂停，主要是保证线程a先启动
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(phone::sendSms, "b").start();
    }
}