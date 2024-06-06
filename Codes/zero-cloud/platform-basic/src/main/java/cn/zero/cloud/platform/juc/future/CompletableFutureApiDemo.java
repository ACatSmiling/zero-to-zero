package cn.zero.cloud.platform.juc.future;

import java.util.concurrent.CompletableFuture;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/6 0:02
 */
public class CompletableFutureApiDemo {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            return "";
        });
    }
}
