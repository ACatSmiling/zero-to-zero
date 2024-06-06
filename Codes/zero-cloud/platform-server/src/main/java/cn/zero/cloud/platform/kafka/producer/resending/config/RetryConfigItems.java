package cn.zero.cloud.platform.kafka.producer.resending.config;

/**
 * kafka通知消息发送者，消息重发配置
 *
 * @author Xisun Wang
 * @since 2024/3/6 14:25
 */
public final class RetryConfigItems {
    private boolean enable;

    private String appToken;

    private String wnsVip;

    private String className;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getWnsVip() {
        return wnsVip;
    }

    public void setWnsVip(String wnsVip) {
        this.wnsVip = wnsVip;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
