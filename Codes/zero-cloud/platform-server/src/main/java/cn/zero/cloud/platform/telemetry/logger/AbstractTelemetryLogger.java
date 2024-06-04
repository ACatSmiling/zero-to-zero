package cn.zero.cloud.platform.telemetry.logger;

import cn.zero.cloud.platform.telemetry.pojo.FeatureName;
import cn.zero.cloud.platform.telemetry.pojo.TelemetryLog;
import cn.zero.cloud.platform.telemetry.filter.FeatureNameFilter;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;

import java.util.Collections;
import java.util.Set;

/**
 * @author Xisun Wang
 * @since 2024/3/21 14:17
 */
public abstract class AbstractTelemetryLogger implements TelemetryLogger {
    private static FeatureNameFilter filter;

    public AbstractTelemetryLogger() {
    }

    public void info(TelemetryLog log) {
        log.validate();
        if (!isFiltered(log)) {
            String logAsJson = PlatFormJsonUtil.serializeToJson(log);
            writeLog(logAsJson);
        }
    }

    protected abstract void writeLog(String logAsJson);

    public static void setFilter(FeatureNameFilter logFilter) {
        filter = logFilter;
    }

    private boolean isFiltered(TelemetryLog telemetryLog) {
        if (filter == null) {
            return false;
        } else {
            FeatureName featureName = new FeatureName(telemetryLog.getMetricType(), telemetryLog.getFeatureType());
            Set<FeatureName> ignoredFeatureNames = filter.getIgnoredFeatureName();
            if (ignoredFeatureNames == null) {
                ignoredFeatureNames = Collections.emptySet();
            }

            return ignoredFeatureNames.contains(featureName);
        }
    }
}
