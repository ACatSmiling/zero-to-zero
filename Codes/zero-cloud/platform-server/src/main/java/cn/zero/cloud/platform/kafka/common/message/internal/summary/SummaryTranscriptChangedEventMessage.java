package cn.zero.cloud.platform.kafka.common.message.internal.summary;

import cn.zero.cloud.platform.utils.PlatFormDateUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryTranscriptChangedEventMessage extends SummaryTranscriptMessage {
    private String version = "2.0";

    private String messageUUID = UUID.randomUUID().toString();

    private long timestamp = System.currentTimeMillis();

    private Date createTime = PlatFormDateUtil.getCurrentDate();

    private String trackingID = StringUtils.isEmpty(MDC.get("trackingID")) ? this.instanceTrackingID() : MDC.get("trackingID").toString();

    private String summaryID;

    private String instanceTrackingID() {
        String trackingSessionID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        this.trackingID = trackingSessionID + "_" + System.currentTimeMillis();
        return trackingSessionID;
    }
}
