package cn.zero.cloud.platform.kafka.common.pojo.result.impl;

import cn.zero.cloud.platform.kafka.common.constants.ResultType;
import cn.zero.cloud.platform.kafka.common.pojo.result.AbstractProcessResult;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:29
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthCheckResult extends AbstractProcessResult {
    private String componentName;

    private HealthCheckResult() {
    }

    public static HealthCheckResult createHealthCheckResult() {
        return new HealthCheckResult();
    }

    /**
     * 生成成功的结果
     *
     * @return HealthCheckResult
     */
    public HealthCheckResult generateSuccessResult() {
        this.setResult(ResultType.SUCCESS);
        return this;
    }

    /**
     * 生成忽略的结果
     *
     * @param additionalMessage 附加信息
     * @return HealthCheckResult
     */
    public HealthCheckResult generateIgnoreResult(Object additionalMessage) {
        this.setResult(ResultType.IGNORE);
        this.setAdditionalMessage(additionalMessage);
        return this;
    }

    /**
     * 生成失败的结果
     *
     * @param additionalMessage 附加信息
     * @return HealthCheckResult
     */
    public HealthCheckResult generateFailureResult(Object additionalMessage) {
        this.setResult(ResultType.FAILURE);
        this.setAdditionalMessage(additionalMessage);
        return this;
    }

    /**
     * 追加 component name，并返回当前实例
     *
     * @param componentName componentName
     * @return HealthCheckResult
     */
    public HealthCheckResult appendComponentName(String componentName) {
        this.setComponentName(componentName);
        return this;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
