package cn.zero.cloud.platform.kafka.config;

import cn.zero.cloud.platform.kafka.producer.resending.config.RetryConfigItems;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/3/6 14:25
 */
@Configuration
public class KafkaParameterConfig {
    @Value("${zero.cloud.kafka.notification.enabled:true}")
    private boolean enabled;

    @Value("${zero.cloud.kafka.notification.message.summary.topic:hf1_meeting_summary_webex_notification}")
    private String summaryNotificationTopic;

    @Value("${zero.cloud.kafka.notification.message.summary-transcript.topic:hf1_meeting_summarytranscript_webex_notification}")
    private String summaryTranscriptNotificationTopic;

    @Value("${zero.cloud.kafka.notification.message.suite-recording.topic:dmz1_suite_recording_webex_notification}")
    private String suiteRecordingNotificationTopic;

    @Bean
    @ConfigurationProperties(prefix = "zero.cloud.kafka.producer.resending")
    public RetryConfigItems retryConfigItems() {
        return new RetryConfigItems();
    }

    /**
     * ###############################################################
     * #kafka send ratelimit
     * ###############################################################
     * <p>
     * webex.kafka.producer.ratelimit[meeting_cache_update]=60
     */
    @Bean
    @ConfigurationProperties(prefix = "platform.kafka.producer.rate-limit")
    public Map<String, String> senderRatelimiterMap() {
        return new HashMap<>();
    }

    public boolean enabled() {
        return enabled;
    }

    public String getSummaryNotificationTopic() {
        return summaryNotificationTopic;
    }

    public void setSummaryNotificationTopic(String summaryNotificationTopic) {
        this.summaryNotificationTopic = summaryNotificationTopic;
    }

    public String getSummaryTranscriptNotificationTopic() {
        return summaryTranscriptNotificationTopic;
    }

    public void setSummaryTranscriptNotificationTopic(String summaryTranscriptNotificationTopic) {
        this.summaryTranscriptNotificationTopic = summaryTranscriptNotificationTopic;
    }

    public String getSuiteRecordingNotificationTopic() {
        return suiteRecordingNotificationTopic;
    }

    public void setSuiteRecordingNotificationTopic(String suiteRecordingNotificationTopic) {
        this.suiteRecordingNotificationTopic = suiteRecordingNotificationTopic;
    }
}
