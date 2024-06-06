package cn.zero.cloud.platform.kafka.common.message.pojo;

public class AuthInfo {

    private String resourceOrgID;

    private String userOrgID;

    private String userID;

    public String getResourceOrgID() {
        return resourceOrgID;
    }

    public void setResourceOrgID(String resourceOrgID) {
        this.resourceOrgID = resourceOrgID;
    }

    public String getUserOrgID() {
        return userOrgID;
    }

    public void setUserOrgID(String userOrgID) {
        this.userOrgID = userOrgID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
