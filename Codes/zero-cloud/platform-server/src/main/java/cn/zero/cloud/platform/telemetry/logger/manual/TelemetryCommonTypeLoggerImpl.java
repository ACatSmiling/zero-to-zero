package cn.zero.cloud.platform.telemetry.logger.manual;

import cn.zero.cloud.platform.common.constants.TelemetryConstants;
import cn.zero.cloud.platform.telemetry.pojo.TelemetryLog;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

import static cn.zero.cloud.platform.common.constants.TelemetryConstants.FeatureType.COMMON_FEATURE;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ModuleType.COMMON_API;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.MetricType.COMMON_METRIC;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.VerbType.*;

/**
 * 手动记录日志
 *
 * @author Xisun Wang
 * @since 2024/3/25 11:08
 */
public class TelemetryCommonTypeLoggerImpl extends TelemetryLog {
    private Stopwatch stopwatch;

    public TelemetryCommonTypeLoggerImpl(TelemetryConstants.ObjectType objectType, String ObjectID) {
        this.setModuleType(COMMON_API.getName());
        this.setMetricType(COMMON_METRIC.getName());
        this.setObjectType(objectType.getName());
        this.setObjectID(ObjectID);
        this.setFeatureType(COMMON_FEATURE.getName());
    }

    public TelemetryCommonTypeLoggerImpl forCreate() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerbType(CREATE.getName());
        return this;
    }

    public TelemetryCommonTypeLoggerImpl forUpdate() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerbType(UPDATE.getName());
        return this;
    }

    public TelemetryCommonTypeLoggerImpl forDelete() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerbType(DELETE.getName());
        return this;
    }

    public TelemetryCommonTypeLoggerImpl forSelect() {
        this.stopwatch = Stopwatch.createStarted();
        this.setVerbType(SELECT.getName());
        return this;
    }

    public void createSuccess() {
        this.setProcessTime(this.stopwatch.elapsed(TimeUnit.MILLISECONDS));
        this.logForSuccess();
        this.stopwatch.stop();
    }

    public void createFailure(String msg) {
        this.setProcessTime(this.stopwatch.elapsed(TimeUnit.MILLISECONDS));
        this.logForFailure(msg);
        this.stopwatch.stop();
    }
}
