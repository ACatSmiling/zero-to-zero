package cn.zero.cloud.platform.kafka.common.message.pojo;


import jakarta.validation.constraints.NotEmpty;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:49
 */
public class RecordingMetaData {
    @NotEmpty(message = "recordingID should not be empty!")
    private String recordingID;

    @NotEmpty(message = "suiteID should not be empty!")
    private String suiteID;

    private String title;

    private String fileType;

    private String durationMS;

    private String status;

    private String sizeByte;

    private String storageRegion;

    private String locationId;

    private String storageCluster;

    private Boolean enableAISummary;

    private Boolean enableActionItem;

    public Boolean getEnableAISummary() {
        return enableAISummary;
    }

    public void setEnableAISummary(Boolean enableAISummary) {
        this.enableAISummary = enableAISummary;
    }

    public Boolean getEnableActionItem() {
        return enableActionItem;
    }

    public void setEnableActionItem(Boolean enableActionItem) {
        this.enableActionItem = enableActionItem;
    }

    public String getStorageCluster() {
        return storageCluster;
    }

    public void setStorageCluster(String storageCluster) {
        this.storageCluster = storageCluster;
    }

    public String getStorageRegion() {
        return storageRegion;
    }

    public void setStorageRegion(String storageRegion) {
        this.storageRegion = storageRegion;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getRecordingID() {
        return recordingID;
    }

    public void setRecordingID(String recordingID) {
        this.recordingID = recordingID;
    }

    public String getSuiteID() {
        return suiteID;
    }

    public void setSuiteID(String suiteID) {
        this.suiteID = suiteID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDurationMS() {
        return durationMS;
    }

    public void setDurationMS(String durationMS) {
        this.durationMS = durationMS;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSizeByte() {
        return sizeByte;
    }

    public void setSizeByte(String sizeByte) {
        this.sizeByte = sizeByte;
    }
}
