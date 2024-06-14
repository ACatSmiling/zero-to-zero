package cn.zero.cloud.platform.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XiSun
 * @version 1.0
 * @since 2024/6/14 23:21
 */
@Slf4j
@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    public void addOrder() {
        orderService.addOrder();
    }

    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
    public String findUserById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }
}
