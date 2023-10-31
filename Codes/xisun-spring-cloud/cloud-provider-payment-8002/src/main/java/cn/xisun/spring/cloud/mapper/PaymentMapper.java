package cn.xisun.spring.cloud.mapper;

import cn.xisun.spring.cloud.entities.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author WangDesong
 * @version 1.0
 * @date 2023/2/11 23:28
 * @description
 */
@Mapper
@Repository
public interface PaymentMapper extends BaseMapper<Payment> {
}
