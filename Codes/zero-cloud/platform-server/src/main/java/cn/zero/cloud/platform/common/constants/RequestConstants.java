package cn.zero.cloud.platform.common.constants;

/**
 * @author Xisun Wang
 * @since 2024/4/12 17:18
 */
public class RequestConstants {
    public static final String PROTOCOL_HTTP = "http:";
    public static final String PROTOCOL_HTTPS = "https:";

    public static final String PROTOCOL_SEPARATOR = "//";

    //request header parameter
    public static final String HEARDER_ACCEPT = "Accept";
    public static final String HEARDER_ACCEPT_RANGES = "Accept-Ranges";

    public static final String HEARDER_APPNAME = "AppName";
    public static final String HEARDER_APPTOKEN = "AppToken";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final String HEARDER_SITEID = "siteId";
    public static final String HEARDER_ETAG = "ETag";
    public static final String HEARDER_EXPIRES = "Expires";

    public static final String HEARDER_RANGE = "Range";
    public static final String HEARDER_IF_RANGE = "If-Range";

    public static final String HEARDER_CONTENT_TYPE = "Content-Type";
    public static final String HEARDER_CONTENT_RANGE = "Content-Range";
    public static final String HEARDER_CONTENT_LENGTH = "Content-Length";
    public static final String HEARDER_CONTENT_DISPOSITION = "Content-Disposition";

    public static final String USER_AGENT = "User-Agent";
    public static final String USER_AGENT_AIBRIDGE = "AIBridge Service";
    //request format
    public static final String CONTENT_FORMAT_JSON = "application/json";
    public static final String CONTENT_FORMAT_TEXT = "text/plain";
    public static final String CONTENT_FORMAT_AUDIO = "audio/mpeg";


    //APPTOKEN_NAME
    public static final String HEARDER_APPNAME_AIBRIDGE = "APP_AIBRIDGE";
    public static final String HEARDER_APPNAME_NGADMIN = "APP_NGADMIN";

    public static final String HEARDER_TRACKING_ID = "TrackingID";

    //URL Parameter
    public static final String REQUEST_PARAMETER_SITEURL = "siteurl";

    public static final String HEARDER_CONFID = "confId";

    //URL mark
    public static final String MARK_QUESTION = "?";
    public static final String MARK_SEPARATOR = "/";
    public static final String MARK_VAR_INTERVAL = "&";
    public static final String MARK_EQUAL = "=";

    //IO
    public static final long ERROR_IO = 90100;

    public static final String CISCOONBEHALFOFWBXUID = "Cisco-On-Behalf-Of-WbxUID";

    private RequestConstants() {
        throw new IllegalStateException("Utility class");
    }
}
