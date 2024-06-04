package cn.zero.cloud.platform.telemetry;

import cn.zero.cloud.platform.common.constants.TelemetryConstants;

import java.lang.annotation.*;

/**
 * 方法切面日志注解
 *
 * @author Xisun Wang
 * @since 2024/3/14 11:06
 */
@Inherited
@Documented
@Target({ElementType.METHOD}) // 指定该注解可以用在方法上
@Retention(RetentionPolicy.RUNTIME) // 指定注解在运行时可见
public @interface Telemetry {
    TelemetryConstants.ModuleType moduleType() default TelemetryConstants.ModuleType.DEFAULT_BLANK;

    TelemetryConstants.MetricType metricType() default TelemetryConstants.MetricType.DEFAULT_BLANK;

    TelemetryConstants.FeatureType featureType() default TelemetryConstants.FeatureType.DEFAULT_BLANK;

    TelemetryConstants.VerbType verbType() default TelemetryConstants.VerbType.DEFAULT_BLANK;

    TelemetryConstants.ObjectType objectType() default TelemetryConstants.ObjectType.DEFAULT_BLANK;

    String objectID() default "";

    String[] parameters() default {};
}
