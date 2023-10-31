package cn.xisun.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author WangDesong
 * @version 1.0
 * @date 2023/2/26 19:10
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosPaymentApplication9002 {
    public static void main(String[] args) {
        SpringApplication.run(NacosPaymentApplication9002.class, args);
    }
}
