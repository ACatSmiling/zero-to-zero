package cn.zero.cloud.platform.exception.type;

/**
 * @author Xisun Wang
 * @since 2024/3/29 9:49
 */
public interface CommonException {
    /**
     * 添加异常的附加信息
     *
     * @param description 信息描述
     */
    void addAdditionalMessage(String description);
}
