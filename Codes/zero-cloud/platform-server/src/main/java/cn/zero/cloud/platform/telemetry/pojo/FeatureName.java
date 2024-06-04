package cn.zero.cloud.platform.telemetry.pojo;

/**
 * @author Xisun Wang
 * @since 2024/3/21 14:38
 */
public class FeatureName {
    private final String metricName;

    private final String featureName;

    public FeatureName(String metricName, String featureName) {
        this.metricName = metricName;
        this.featureName = featureName;
    }

    public String getMetricName() {
        return this.metricName;
    }

    public String getFeatureName() {
        return this.featureName;
    }

    @Override
    public int hashCode() {
        int result = this.metricName.hashCode();
        result = 31 * result + this.featureName.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            FeatureName that = (FeatureName) obj;
            return this.metricName.equals(that.metricName) && this.featureName.equals(that.featureName);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return " metricName: " + this.metricName + ",featureName: " + this.featureName;
    }
}
