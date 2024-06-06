package cn.zero.cloud.platform.kafka.common.message.pojo;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:48
 */
public class OwnerUserInfo extends SiteUserInfo {

    private String ownerType;

    private String ownerId;

    private String ownerEmail;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
}
