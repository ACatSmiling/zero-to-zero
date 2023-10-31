package cn.xisun.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author WangDesong
 * @version 1.0
 * @date 2023/2/26 21:00
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosOrderApplication83 {
    public static void main(String[] args) {
        SpringApplication.run(NacosOrderApplication83.class, args);
    }
}
