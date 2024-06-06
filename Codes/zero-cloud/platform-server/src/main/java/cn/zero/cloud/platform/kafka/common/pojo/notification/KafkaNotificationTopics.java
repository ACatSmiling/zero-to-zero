package cn.zero.cloud.platform.kafka.common.pojo.notification;

/**
 * @author Xisun Wang
 * @since 2024/3/6 14:25
 */
public class KafkaNotificationTopics {
    private String summaryTopic;

    private String summaryTranscriptTopic;

    private String suiteRecordingTopic;

    public String getSummaryTopic() {
        return summaryTopic;
    }

    public void setSummaryTopic(String summaryTopic) {
        this.summaryTopic = summaryTopic;
    }

    public String getSummaryTranscriptTopic() {
        return summaryTranscriptTopic;
    }

    public void setSummaryTranscriptTopic(String summaryTranscriptTopic) {
        this.summaryTranscriptTopic = summaryTranscriptTopic;
    }

    public String getSuiteRecordingTopic() {
        return suiteRecordingTopic;
    }

    public void setSuiteRecordingTopic(String suiteRecordingTopic) {
        this.suiteRecordingTopic = suiteRecordingTopic;
    }
}
