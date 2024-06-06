package cn.zero.cloud.platform.kafka.common.exception.error;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xisun Wang
 * @since 2024/3/11 9:19
 */
public class ErrorSummary {
    private final String prefix;

    @NotNull
    private final ErrorCode errorCode;

    private final String reason;

    private final String messageUUID;

    private final String trackingID;

    private final String version;

    private final Map<String, String> additionalInfo;

    public ErrorSummary(final String prefix, final ErrorCode errorCode, final String reason, final String messageUUID, final String trackingID, final String version, final Map<String, String> additionalInfo) {
        this.prefix = prefix;
        this.errorCode = errorCode;
        this.reason = reason;
        this.messageUUID = messageUUID;
        this.trackingID = trackingID;
        this.version = version;
        this.additionalInfo = additionalInfo;
    }

    public String getErrorMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("ErrorCode=").append(errorCode.getCodeNum())
                .append(", ").append(errorCode.getErrorName());
        if (StringUtils.isNotBlank(reason)) {
            builder.append(". Reason: ").append(reason);
        }
        return builder.toString();
    }

    public String getErrorSummaryInfo() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(prefix)) {
            builder.append(prefix).append(" ");
        }
        builder.append(getErrorMessage());
        builder.append(". Details: ");
        builder.append("messageUUID=").append(messageUUID);
        builder.append(", version=").append(version);
        builder.append(", trackingID=").append(trackingID);
        if (additionalInfo != null) {
            for (Map.Entry<String, String> entry : additionalInfo.entrySet()) {
                builder.append(", ").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return builder.toString();
    }

    public static ErrorSummaryBuilder builder() {
        return new ErrorSummaryBuilder();
    }

    public static class ErrorSummaryBuilder {
        private String prefix;

        private ErrorCode errorCode;

        private String reason;

        private String messageUUID;

        private String trackingID;

        private String version;

        private final Map<String, String> additionalInfo = new HashMap<>();

        public ErrorSummaryBuilder() {
        }

        public ErrorSummaryBuilder prefix(final String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ErrorSummaryBuilder errorCode(final ErrorCode errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ErrorSummaryBuilder reason(final String reason) {
            this.reason = reason;
            return this;
        }

        public ErrorSummaryBuilder messageUUID(final String messageUUID) {
            this.messageUUID = messageUUID;
            return this;
        }

        public ErrorSummaryBuilder trackingID(final String trackingID) {
            this.trackingID = trackingID;
            return this;
        }

        public ErrorSummaryBuilder version(final String version) {
            this.version = version;
            return this;
        }

        public ErrorSummaryBuilder additionalInfo(final String itemName, final String itemValue) {
            if (!StringUtils.isEmpty(itemName)) {
                this.additionalInfo.put(itemName, itemValue);
            }
            return this;
        }

        public ErrorSummary build() {
            return new ErrorSummary(this.prefix, this.errorCode, this.reason, this.messageUUID, this.trackingID, this.version, this.additionalInfo);
        }

        public String toString() {
            return "ErrorSummary.ErrorSummaryBuilder(prefix=" + this.prefix + ", errorCode=" + this.errorCode + ", reason=" + this.reason + ", messageUUID=" + this.messageUUID +
                    ", trackingID=" + this.trackingID + ", messageVersion=" + this.version + ", additionalInfo=" + this.additionalInfo + ")";
        }
    }
}
