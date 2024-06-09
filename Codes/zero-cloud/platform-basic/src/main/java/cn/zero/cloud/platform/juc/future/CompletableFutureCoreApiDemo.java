package cn.zero.cloud.platform.juc.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/6 0:02
 */
@Slf4j
public class CompletableFutureCoreApiDemo {
    public static void main(String[] args) {
        // 定义一个线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            log.info("current thread: {}", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPool);

        try {
            // 输出null，runAsync()没有返回值
            log.info("completableFuture result: {}", completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }


        CompletableFuture<String> objectCompletableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("current thread: {}", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello supplyAsync";
        }, threadPool);

        try {
            // 输出hello supplyAsync，supplyAsync()有返回值
            log.info("objectCompletableFuture result: {}", objectCompletableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 关闭线程池
        threadPool.shutdown();
    }
}
