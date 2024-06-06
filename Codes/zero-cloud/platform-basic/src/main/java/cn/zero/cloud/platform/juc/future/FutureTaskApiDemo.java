package cn.zero.cloud.platform.juc.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/5 23:10
 */
@Slf4j
public class FutureTaskApiDemo {
    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            log.info("{} thread come in", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                log.error("exception: ", e);
            }
            return "task over";
        });

        Thread t1 = new Thread(futureTask, "t1");
        t1.start();

        // 方式一：调用get()，通过阻塞的方式，获取futureTask的结果，main线程会一直阻塞，直到futureTask返回结果
        /*try {
            String result = futureTask.get();
            log.info("{} thread get the result of future task: {}", Thread.currentThread().getName(), result);
        } catch (InterruptedException | ExecutionException e) {
            log.error("exception: ", e);
        }*/

        // 方式二：指定阻塞的时间，超时后未获取到结果直接抛出异常java.util.concurrent.TimeoutException
        /*try {
            String result = futureTask.get(3, TimeUnit.SECONDS);
            log.info("{} thread get the result of future task: {}", Thread.currentThread().getName(), result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("exception: ", e);
        }*/

        // 方式三：轮询，调用isDone()
        while (true) {
            if (futureTask.isDone()) {
                try {
                    String result = futureTask.get();
                    log.info("{} thread get the result of future task: {}", Thread.currentThread().getName(), result);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("exception: ", e);
                }
                break;
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    log.error("exception: ", e);
                }

                log.info("{} thread is processing", Thread.currentThread().getName());
            }
        }
    }
}
