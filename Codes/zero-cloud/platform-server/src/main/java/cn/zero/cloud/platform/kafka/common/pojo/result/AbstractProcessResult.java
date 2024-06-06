package cn.zero.cloud.platform.kafka.common.pojo.result;

import cn.zero.cloud.platform.kafka.common.constants.ResultType;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:30
 */
public abstract class AbstractProcessResult {
    private ResultType result;

    private Object additionalMessage;

    private String retryTopic;

    private String retryCount; // used for retry_delay telemetry


    public ResultType getResult() {
        return result;
    }


    public void setResult(ResultType result) {
        this.result = result;
    }

    public Object getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(Object additionalMessage) {
        this.additionalMessage = additionalMessage;
    }

    public String getRetryTopic() {
        return retryTopic;
    }

    public void setRetryTopic(String retryTopic) {
        this.retryTopic = retryTopic;
    }

    public String getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }
}
