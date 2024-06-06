package cn.zero.cloud.platform.kafka.common.pojo.result.impl;

import cn.zero.cloud.platform.kafka.common.message.BaseMessage;
import cn.zero.cloud.platform.kafka.common.exception.error.ErrorCode;
import cn.zero.cloud.platform.kafka.common.constants.ResultType;
import cn.zero.cloud.platform.kafka.common.pojo.result.AbstractProcessResult;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:06
 */
public class ConsumeResult extends AbstractProcessResult {
    protected final long produceStartTime;

    protected final long consumeStartTime;

    protected long consumeCostTime;

    protected long e2EChannelCostTime;

    protected ErrorCode errorCode;

    protected ConsumeResult(BaseMessage message) {
        this.consumeStartTime = System.currentTimeMillis();
        this.produceStartTime = (message == null) ? -1L : message.getTimestamp();
    }

    protected ConsumeResult(long produceStartTime) {
        this.consumeStartTime = System.currentTimeMillis();
        this.produceStartTime = produceStartTime;
    }

    public static ConsumeResult createConsumeResult(BaseMessage message) {
        return new ConsumeResult(message);
    }

    public static ConsumeResult createConsumeResult(long produceStartTime) {
        return new ConsumeResult(produceStartTime);
    }

    /**
     * 花费时间计算
     */
    protected void recordCostTime() {
        long consumeEndTime = System.currentTimeMillis();
        this.consumeCostTime = consumeEndTime - consumeStartTime;
        this.e2EChannelCostTime = (produceStartTime == -1L) ? -1L : (consumeEndTime - produceStartTime);
    }

    /**
     * 生成成功的结果
     *
     * @return ConsumeResult
     */
    public ConsumeResult generateSuccessResult() {
        this.setResult(ResultType.SUCCESS);
        recordCostTime();
        return this;
    }

    /**
     * 生成忽略的结果
     *
     * @param additionalMessage 附加信息
     * @return ConsumeResult
     */
    public ConsumeResult generateIgnoreResult(String additionalMessage) {
        this.setResult(ResultType.IGNORE);
        this.setAdditionalMessage(additionalMessage);
        recordCostTime();
        return this;
    }

    /**
     * 生成失败的结果
     *
     * @param additionalMessage 附加信息
     * @return ConsumeResult
     */
    public ConsumeResult generateFailureResult(String additionalMessage) {
        return generateFailureResult(additionalMessage, null);
    }

    /**
     * 生成失败的结果
     *
     * @param additionalMessage 附加信息
     * @param errorCode         错误信息
     * @return ConsumeResult
     */
    public ConsumeResult generateFailureResult(String additionalMessage, ErrorCode errorCode) {
        this.setResult(ResultType.FAILURE);
        this.setAdditionalMessage(additionalMessage);
        this.setErrorCode(errorCode);
        recordCostTime();
        return this;
    }

    /**
     * 生成待重试结果
     *
     * @param additionalMessage 附加信息
     * @return ConsumeResult
     */
    public ConsumeResult generatePendingRetryResult(String additionalMessage) {
        return generatePendingRetryResult(additionalMessage, null);
    }

    /**
     * 生成待重试结果
     *
     * @param additionalMessage 附加信息
     * @param errorCode         错误信息
     * @return ConsumeResult
     */
    public ConsumeResult generatePendingRetryResult(String additionalMessage, ErrorCode errorCode) {
        this.setResult(ResultType.PENDING_RETRY);
        this.setAdditionalMessage(additionalMessage);
        this.setErrorCode(errorCode);
        recordCostTime();
        return this;
    }

    /**
     * 生成待恢复结果
     *
     * @param additionalMessage 附加信息
     * @return ConsumeResult
     */
    public ConsumeResult generatePendingRestoreResult(String additionalMessage) {
        return generatePendingRestoreResult(additionalMessage, null);
    }

    /**
     * 生成待恢复结果
     *
     * @param additionalMessage 附加信息
     * @param errorCode         错误信息
     * @return ConsumeResult
     */
    public ConsumeResult generatePendingRestoreResult(String additionalMessage, ErrorCode errorCode) {
        this.setResult(ResultType.PENDING_RESTORE);
        this.setAdditionalMessage(additionalMessage);
        this.setErrorCode(errorCode);
        recordCostTime();
        return this;
    }

    public long getConsumeCostTime() {
        return this.consumeCostTime;
    }

    public long getE2EChannelCostTime() {
        return e2EChannelCostTime;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
