package cn.zero.cloud.platform.juc.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/7 23:13
 */
@Slf4j
public class CompletableFutureCommonApiDemo {
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(3);

    private static void thenApply() {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 1;
        }, THREAD_POOL).thenApply(f -> {
            // 输出：1
            log.info("f: {}", f);
            return f + 2;
        }).handle((f, e) -> {
            // 输出：3
            log.info("f: {}", f);

            return f + 2;
        }).whenComplete((v, e) -> {
            if (e == null) {
                // 输出：5
                log.info("result: {}", v);
            }
        }).exceptionally(e -> {
            if (e != null) {
                log.info("exception: ", e);
            }
            return null;
        });
    }

    public static void main(String[] args) {
        thenApply();

        THREAD_POOL.shutdown();
    }
}
