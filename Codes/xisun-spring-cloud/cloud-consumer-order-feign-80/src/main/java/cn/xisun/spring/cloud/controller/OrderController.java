package cn.xisun.spring.cloud.controller;

import cn.xisun.spring.cloud.entities.CommonResult;
import cn.xisun.spring.cloud.entities.Payment;
import cn.xisun.spring.cloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangDesong
 * @version 1.0
 * @date 2023/3/12 1:11
 * @description
 */
@Slf4j
@RestController
public class OrderController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/consumer/payment/feign/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping(value = "/consumer/payment/feign/timeout")
    public String paymentFeignTimeOut() {
        return paymentService.paymentFeignTimeOut();
    }
}
