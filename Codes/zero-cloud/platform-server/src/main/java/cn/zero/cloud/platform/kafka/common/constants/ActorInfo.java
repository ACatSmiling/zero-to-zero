package cn.zero.cloud.platform.kafka.common.constants;



/**
 * Added for Recording Message v2.0 and Transcript Message v2.0 (Feature SPARK-102523)
 * <p>
 * See Details:
 * - Recording Message v2.0: https://wiki.cisco.com/x/ibHUHw
 * - Meeting Message v2.0: https://wiki.cisco.com/display/HFWEB/Meeting+Message+v3.0
 */
/**
 * @author Xisun Wang
 * @since 2024/3/8 16:49
 */
public class ActorInfo {

    private ActorType actorType;

    private String ipAddress;

    private String ciUserID;

    private String orgID;

    private String webexUserID;

    private String siteUUID;

    public ActorInfo() {
        this.ciUserID = "";
        this.orgID = "";
        this.webexUserID = "";
        this.siteUUID = "";
    }

    public ActorType getActorType() {
        return actorType;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCiUserID() {
        return ciUserID;
    }

    public void setCiUserID(String ciUserID) {
        this.ciUserID = ciUserID;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getWebexUserID() {
        return webexUserID;
    }

    public void setWebexUserID(String webexUserID) {
        this.webexUserID = webexUserID;
    }

    public String getSiteUUID() {
        return siteUUID;
    }

    public void setSiteUUID(String siteUUID) {
        this.siteUUID = siteUUID;
    }


    public enum ActorType {
        LOGIN_USER, ANONYMOUS_USER, RECORDING_SERVICE, RETENTION_SERVICE
    }



}
