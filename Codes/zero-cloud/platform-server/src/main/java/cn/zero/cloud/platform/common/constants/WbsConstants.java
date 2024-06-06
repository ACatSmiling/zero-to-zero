package cn.zero.cloud.platform.common.constants;

/**
 * @author Xisun Wang
 * @since 2024/4/12 17:19
 */
public class WbsConstants {
    public static final String AIBRIDGE_DOCBASE = "wbxaibridge";

    public static final String SITE_KEY_NAME = "siteKeyName";
    public static final String KMS_KEY_INFO = "kmsKeyInfo";
    public static final String AUTH_INFO = "authInfo";

    public static final String AI_CONTENT_SUCCESS_STATUS = "Success";
    public static final String AI_CONTENT_FAILURE_STATUS = "Failure";
    public static final String AI_CONTENT_NONE_STATUS = "None";

    public static final String TRACKING_ID = "trackingID";
    public static final String PROMPT_TOKENS = "prompt_tokens";
    public static final String COMPLETION_TOKENS = "completion_tokens";
    public static final String TOTAL_TOKENS = "total_tokens";
    public static final String TOTAL_RETRY = "retry";
    public static final String SITE_UUID = "siteUUID";

    public static final String AI_CONTENT_ERROR_MSG = "AI_CONTENT_ERROR_MSG";

    private WbsConstants() {
        throw new IllegalStateException("Utility class");
    }
}
