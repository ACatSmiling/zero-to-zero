package cn.zero.cloud.platform.kafka.common.exception.error;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * @author Xisun Wang
 * @since 2024/3/8 14:59
 */
public class ErrorCode {
    @NotNull
    private ErrorType errorType;

    @NotNull
    private ErrorDetail errorDetail;

    private boolean needRetry;

    private ErrorCode(ErrorType errorType, ErrorDetail errorDetail, boolean needRetry) {
        this.errorType = errorType;
        this.errorDetail = errorDetail;
        this.needRetry = needRetry;
    }

    public int getCodeNum() {
        return errorType.getCode() * 1000 + errorDetail.getCode();
    }

    public String getErrorName() {
        return errorType.name() + "-" + errorDetail.name();
    }

    public boolean isNeedRetry() {
        return this.needRetry;
    }

    public void setNeedRetry(boolean needRetry) {
        this.needRetry = needRetry;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    public ErrorDetail getErrorDetail() {
        return this.errorDetail;
    }

    public static ErrorCodeBuilder builder() {
        return new ErrorCodeBuilder();
    }

    public static class ErrorCodeBuilder {
        private ErrorType errorType;

        private ErrorDetail errorDetail;

        private Boolean needRetry;

        ErrorCodeBuilder() {
        }

        public ErrorCodeBuilder errorType(final ErrorType errorType) {
            this.errorType = errorType;
            return this;
        }

        public ErrorCodeBuilder errorDetail(final ErrorDetail errorDetail) {
            this.errorDetail = errorDetail;
            return this;
        }

        public ErrorCodeBuilder needRetry(final Boolean needRetry) {
            this.needRetry = Objects.requireNonNullElse(needRetry, false);
            return this;
        }

        public ErrorCode build() {
            if (this.errorType == null) {
                this.errorType = ErrorType.UNKNOWN;
            }

            if (this.errorDetail == null) {
                this.errorDetail = ErrorDetail.UNKNOWN;
            }

            if (this.needRetry == null) {
                this.needRetry = false;
            }

            return new ErrorCode(this.errorType, this.errorDetail, this.needRetry);
        }

        public Boolean getNeedRetry() {
            return needRetry;
        }
    }
}
