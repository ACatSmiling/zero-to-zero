package cn.zero.cloud.platform.kafka.producer.resending;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    public String getMessageTopic() {
        return messageTopic;
    }

    public void setMessageTopic(String messageTopic) {
        this.messageTopic = messageTopic;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getRecordHeaders() {
        return recordHeaders;
    }

    public void setRecordHeaders(String recordHeaders) {
        this.recordHeaders = recordHeaders;
    }


    private String messageTopic;
    private String messageKey;
    private String messageBody;
    private String recordHeaders;

    @JsonCreator
    public Message(@JsonProperty("messageTopic") String messageTopic, @JsonProperty("messageKey") String messageKey,
                   @JsonProperty("messageBody") String messageBody, @JsonProperty("recordHeaders") String recordHeaders) {
        this.messageTopic = messageTopic;
        this.messageKey = messageKey;
        this.messageBody = messageBody;
        this.recordHeaders = recordHeaders;
    }

    public Message() {

    }

}