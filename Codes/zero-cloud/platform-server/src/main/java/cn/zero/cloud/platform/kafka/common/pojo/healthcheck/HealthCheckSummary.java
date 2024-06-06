package cn.zero.cloud.platform.kafka.common.pojo.healthcheck;

import cn.zero.cloud.platform.kafka.common.pojo.result.impl.HealthCheckResult;

import java.util.List;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:29
 */
public class HealthCheckSummary {
    private String status;

    private List<HealthCheckResult> details;

    private HealthCheckSummary() {
    }

    public static HealthCheckSummary createHealthCheckSummary() {
        return new HealthCheckSummary();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HealthCheckResult> getDetails() {
        return details;
    }

    public void setDetails(List<HealthCheckResult> details) {
        this.details = details;
    }
}
