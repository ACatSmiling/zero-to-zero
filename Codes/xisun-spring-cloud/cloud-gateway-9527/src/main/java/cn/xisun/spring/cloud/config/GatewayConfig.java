package cn.xisun.spring.cloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangDesong
 * @version 1.0
 * @date 2023/2/16 0:08
 * @description
 */
@Configuration
public class GatewayConfig {
    /**
     * 配置了一个id为path_route_baidu的路由规则:
     * 当访问地址 http://localhost:9527/baidu 时, 会自动转发到地址：https://www.baidu.com/
     *
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route_get3", r -> r.path("/get3").uri("localhost:8001/payment/get/3"))
                .route("path_route_get4", r -> r.path("/get4").uri("https://news.baidu.com/"))
                .build();
    }
}
