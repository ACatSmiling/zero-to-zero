package cn.zero.cloud.platform.thread.local.context;

import java.util.Objects;

/**
 * @author Xisun Wang
 * @since 2024/4/15 12:20
 */
public class WbxServerContextToken {
    private long siteID;
    private String siteUrl;

    private Long languageId = 1L;
    private Long timeZoneId = Long.valueOf(-1);
    private Long regionId = Long.valueOf(2);
    private String jodaTimeZoneId = null;
    private String language = null;
    private String region = null;

    // store siteurl which is used to query this site basic DTO
    private String parameterSiteURL;

    private String serverName;
    private String remoteAddress;
    private String userAgent;
    //userAgent maybe from hearder:UserAgent, agent always from User-Agent
    private String agent;

    // Stores the User-Agent header information from the original request, it could from the "X-Original-User-Agent" request header.
    private String originalUserAgent;

    // Controller method name, mappedHandler generated in DispactherServelet
    private String handlerName;
    private boolean fedrampCluster;

    private String endUserClientIp;

    private String requestUrl;

    //TODO put local here.

    public long getSiteID() {
        return siteID;
    }

    public void setSiteID(long siteID) {
        this.siteID = siteID;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public Long getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(Long timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getParameterSiteURL() {
        return parameterSiteURL;
    }

    public void setParameterSiteURL(String parameterSiteURL) {
        this.parameterSiteURL = parameterSiteURL;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getUserAgent() {
        return Objects.isNull(userAgent) ? "" : userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public boolean isFedrampCluster() {
        return fedrampCluster;
    }

    public void setFedrampCluster(boolean fedrampCluster) {
        this.fedrampCluster = fedrampCluster;
    }

    public String getEndUserClientIp() {
        return endUserClientIp;
    }

    public void setEndUserClientIp(String endUserClientIp) {
        this.endUserClientIp = endUserClientIp;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getJodaTimeZoneId() {
        return jodaTimeZoneId;
    }

    public void setJodaTimeZoneId(String jodaTimeZoneId) {
        this.jodaTimeZoneId = jodaTimeZoneId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getOriginalUserAgent() {
        return originalUserAgent;
    }

    public void setOriginalUserAgent(String originalUserAgent) {
        this.originalUserAgent = originalUserAgent;
    }
}
