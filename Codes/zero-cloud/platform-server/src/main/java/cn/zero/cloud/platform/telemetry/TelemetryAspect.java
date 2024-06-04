package cn.zero.cloud.platform.telemetry;

import cn.zero.cloud.platform.common.constants.TelemetryConstants;
import cn.zero.cloud.platform.telemetry.pojo.TelemetryLog;
import cn.zero.cloud.platform.telemetry.service.TelemetryBuildService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Xisun Wang
 * @since 2024/3/14 11:16
 */
@Aspect
@Order(1)
@Component
public class TelemetryAspect {
    private final TelemetryBuildService telemetryBuildService;

    @Autowired
    public TelemetryAspect(TelemetryBuildService telemetryBuildService) {
        this.telemetryBuildService = telemetryBuildService;
    }

    @Around("@annotation(telemetry)") // 指定切点表达式
    public Object handle(ProceedingJoinPoint joinPoint, Telemetry telemetry) throws Throwable {
        // 随机生成一个UUID，放入ThreadLocal中，无实际意义
        MDC.put("UUID_KEY", UUID.randomUUID().toString());

        // 方法执行前
        long beginTime = System.currentTimeMillis();
        String resultStaus = TelemetryConstants.ProcessResult.SUCCESS.getName();
        TelemetryLog telemetryLog = telemetryBuildService.buildBasicTelemetryLog(joinPoint, telemetry);
        try {
            // 继续执行原方法
            Object result = joinPoint.proceed();
            telemetryLog = telemetryBuildService.decorateTelemetryLog(joinPoint, telemetry, telemetryLog, result);
            return result;
        } catch (Exception e) {
            // 异常处理
            resultStaus = TelemetryConstants.ProcessResult.FAILURE.getName();
            telemetryLog.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            // 方法执行后
            long endTime = System.currentTimeMillis();
            telemetryLog.setProcessTime(endTime - beginTime);
            telemetryLog.setProcessResult(resultStaus);

            // 日志输出
            telemetryLog.logInfo();
        }
    }
}
