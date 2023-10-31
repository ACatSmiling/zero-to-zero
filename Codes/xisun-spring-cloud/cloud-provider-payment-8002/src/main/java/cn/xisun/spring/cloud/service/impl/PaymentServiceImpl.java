package cn.xisun.spring.cloud.service.impl;

import cn.xisun.spring.cloud.entities.Payment;
import cn.xisun.spring.cloud.mapper.PaymentMapper;
import cn.xisun.spring.cloud.service.PaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author WangDesong
 * @version 1.0
 * @date 2023/2/11 23:34
 * @description
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
}
