package cn.zero.cloud.platform.telemetry.decorator.impl;

import cn.zero.cloud.platform.common.constants.TelemetryConstants;
import cn.zero.cloud.platform.telemetry.decorator.TelemetryDecorator;
import cn.zero.cloud.platform.telemetry.filter.impl.GlobalFeatureNameFilter;
import cn.zero.cloud.platform.telemetry.logger.AbstractTelemetryLogger;
import cn.zero.cloud.platform.telemetry.pojo.TelemetryLog;
import cn.zero.cloud.platform.telemetry.factory.TelemetryDecoratorFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * @author Xisun Wang
 * @since 2024/3/20 15:52
 */
@Service
public class TestObjectTypeTelemetryDecorator implements TelemetryDecorator {

    @PostConstruct
    private void postConstruct() {
        TelemetryDecoratorFactory.register(TelemetryConstants.ObjectType.TEST_OBJECT.getName(), this);
        AbstractTelemetryLogger.setFilter(new GlobalFeatureNameFilter());
    }

    @Override
    public void buildTelemetryItems(Object result, TelemetryLog telemetryLog) {
        // TODO 针对result，当前decorator需进行的特定处理，如果没有，则忽略此decorator的注册

        // 切面日志的通用处理
        setCommonMessage(telemetryLog);
    }
}
