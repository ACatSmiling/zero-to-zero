package cn.zero.cloud.platform.juc.fair;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/8 22:06
 */
@Slf4j
public class FairLockDemo {
    private static class Ticket {
        private int number = 20;

        // 默认为非公平锁，指定true为公平锁
        private final ReentrantLock lock = new ReentrantLock(true);

        public void saleTicket() {
            lock.lock();
            try {
                if (number > 0) {
                    number--;
                    log.info("thread {} sale one ticket, {} tickets remaining", Thread.currentThread().getName(), number);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                ticket.saleTicket();
            }
        }, "a").start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                ticket.saleTicket();
            }
        }, "b").start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                ticket.saleTicket();
            }
        }, "c").start();
    }
}
