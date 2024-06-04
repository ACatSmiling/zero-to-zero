package cn.zero.cloud.platform.telemetry.pojo;

import cn.zero.cloud.platform.common.constants.TelemetryConstants;
import cn.zero.cloud.platform.telemetry.factory.TelemetryLoggerFactory;
import cn.zero.cloud.platform.telemetry.logger.TelemetryLogger;
import cn.zero.cloud.platform.utils.PlatFormDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Xisun Wang
 * @since 2024/3/14 11:22
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TelemetryLog {
    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求路径
     */
    private String requestUrl;

    /**
     * class mapping
     */
    private String classMapping;

    /**
     * method mapping
     */
    private String methodMapping;

    /**
     * 方法全路径
     */
    private String fullPath;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 模块类型
     */
    private String moduleType;

    /**
     * 指标类型
     */
    private String metricType;

    /**
     * 特性类型
     */
    private String featureType;

    /**
     * 对象类型
     */
    private String objectType;

    /**
     * 对象主键
     */
    private String objectID;

    /**
     * 动作类型
     */
    private String verbType;

    /**
     * 方法参数
     */
    private Map<String, Object> parameters;

    /**
     * 方法处理结果
     */
    private String processResult;

    /**
     * 方法处理时间
     */
    private long processTime;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * ThreadLocal中的一个随机UUID，无实际意义
     */
    private String randomUUID;

    /**
     * 当前日志的生成时间
     */
    private final String timestamp = PlatFormDateUtil.getCurrentTimeDefaultTimeZone();

    public void setParameter(String name, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof String && StringUtils.isBlank((String) value)) {
            return;
        }

        Map<String, Object> m = this.getParameters();
        if (m == null) {
            m = new HashMap<>();
        }
        m.put(name, value);

        this.setParameters(m);
    }

    public void validate() throws IllegalArgumentException {
        if (StringUtils.isBlank(this.metricType) || StringUtils.isBlank(this.featureType)) {
            throw new IllegalArgumentException("metricType and featureType MUST have value for telemetry log!");
        }
    }

    public void logInfo() {
        TelemetryLogger telemetryLogger = TelemetryLoggerFactory.getTelemetryLogger();
        telemetryLogger.info(this);
    }

    public void logForSuccess() {
        logWithResultAndMessage(TelemetryConstants.ProcessResult.SUCCESS, null);
    }

    public void logForFailure(String errorMsg) {
        logWithResultAndMessage(TelemetryConstants.ProcessResult.FAILURE, errorMsg);
    }

    private void logWithResultAndMessage(TelemetryConstants.ProcessResult processResult, String errorMsg) {
        TelemetryLogger telemetryLogger = TelemetryLoggerFactory.getTelemetryLogger();
        this.setProcessResult(processResult.getName());
        if (StringUtils.isNotBlank(errorMsg)) {
            this.setErrorMsg(errorMsg);
        }
        telemetryLogger.info(this);
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getClassMapping() {
        return classMapping;
    }

    public void setClassMapping(String classMapping) {
        this.classMapping = classMapping;
    }

    public String getMethodMapping() {
        return methodMapping;
    }

    public void setMethodMapping(String methodMapping) {
        this.methodMapping = methodMapping;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getVerbType() {
        return verbType;
    }

    public void setVerbType(String verbType) {
        this.verbType = verbType;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(long processTime) {
        this.processTime = processTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getRandomUUID() {
        return randomUUID;
    }

    public void setRandomUUID(String randomUUID) {
        this.randomUUID = randomUUID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelemetryLog that = (TelemetryLog) o;
        return processTime == that.processTime && Objects.equals(requestType, that.requestType) && Objects.equals(requestUrl, that.requestUrl)
                && Objects.equals(classMapping, that.classMapping) && Objects.equals(methodMapping, that.methodMapping)
                && Objects.equals(fullPath, that.fullPath) && Objects.equals(methodName, that.methodName) && Objects.equals(moduleType, that.moduleType)
                && Objects.equals(metricType, that.metricType) && Objects.equals(featureType, that.featureType) && Objects.equals(objectType, that.objectType)
                && Objects.equals(objectID, that.objectID) && Objects.equals(verbType, that.verbType) && Objects.equals(parameters, that.parameters)
                && Objects.equals(processResult, that.processResult) && Objects.equals(errorMsg, that.errorMsg) && Objects.equals(randomUUID, that.randomUUID)
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestType, requestUrl, classMapping, methodMapping, fullPath, methodName, moduleType, metricType, featureType, objectType,
                objectID, verbType, parameters, processResult, processTime, errorMsg, randomUUID, timestamp);
    }

    @Override
    public String toString() {
        return "TelemetryLog{" +
                "requestType='" + requestType + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", classMapping='" + classMapping + '\'' +
                ", methodMapping='" + methodMapping + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", methodName='" + methodName + '\'' +
                ", moduleType='" + moduleType + '\'' +
                ", metricType='" + metricType + '\'' +
                ", featureType='" + featureType + '\'' +
                ", objectType='" + objectType + '\'' +
                ", objectID='" + objectID + '\'' +
                ", verbType='" + verbType + '\'' +
                ", parameters=" + parameters +
                ", processResult='" + processResult + '\'' +
                ", processTime=" + processTime +
                ", errorMsg='" + errorMsg + '\'' +
                ", randomUUID='" + randomUUID + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
