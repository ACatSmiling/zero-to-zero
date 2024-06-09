package cn.zero.cloud.platform.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/8 10:39
 */
@Slf4j
public class LockDemo4 {
    private static class Phone {
        public synchronized void sendEmail() {
            // sendEmail方法暂停3秒钟
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("thread {} send email", Thread.currentThread().getName());
        }

        public synchronized void sendSms() {
            log.info("thread {} send sms", Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(phone1::sendEmail, "a").start();

        // main线程暂停，主要是保证线程a先启动
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(phone2::sendSms, "b").start();
    }
}
