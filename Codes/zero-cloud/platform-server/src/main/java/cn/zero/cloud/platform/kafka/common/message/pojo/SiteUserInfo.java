package cn.zero.cloud.platform.kafka.common.message.pojo;



/**
 * This object is shared by many different messages (meeting, meetinginstance, recording, etc.)
 * So here the validation condition need consider all the message types in which the SiteUserInfo is referenced
 * <p>
 * To make the validation effective, just add @Valid annotation when referring the SiteUserInfo, e.g:
 * <p>
 * public class SampleObj {
 *
 * @NotEmpty private String fieldA;
 * @NotNull private Date fieldB;
 * @NotNull
 * @Valid private SiteUserInfo user;
 * <p>
 * // Getter and Setter
 * }
 */

import cn.zero.cloud.platform.kafka.common.constants.type.KafkaEventListType;
import cn.zero.cloud.platform.kafka.common.message.util.validator.NotEmptyUnderConditions;

/**
 * @author Xisun Wang
 * @since 2024/3/8 16:18
 */
public class SiteUserInfo {
    @NotEmptyUnderConditions(
            kafkaEventList = {
                    /*KafkaBusinessEvent.MEETINGINSTANCE_END_EVENT,
                    KafkaBusinessEvent.MEETINGINSTANCE_END_DATA_READY_EVENT,
                    KafkaBusinessEvent.RECORDING_CREATE_EVENT,
                    KafkaBusinessEvent.RECORDING_UPDATE_EVENT,
                    KafkaBusinessEvent.RECORDING_TRASH_EVENT,
                    KafkaBusinessEvent.RECORDING_RESTORE_EVENT*/
            },
            listType = KafkaEventListType.INCLUDE,
            message = "DisplayName should not be empty!"
    )
    private String displayName;

    @NotEmptyUnderConditions(
            kafkaEventList = {
                    /*KafkaBusinessEvent.MEETINGINSTANCE_END_EVENT,
                    KafkaBusinessEvent.MEETINGINSTANCE_END_DATA_READY_EVENT,
                    KafkaBusinessEvent.RECORDING_CREATE_EVENT,
                    KafkaBusinessEvent.RECORDING_UPDATE_EVENT,
                    KafkaBusinessEvent.RECORDING_TRASH_EVENT,
                    KafkaBusinessEvent.RECORDING_RESTORE_EVENT*/
            },
            listType = KafkaEventListType.INCLUDE,
            message = "Email should not be empty!"
    )
    private String email;

    @NotEmptyUnderConditions(
            kafkaEventList = {
                    /*KafkaBusinessEvent.MEETINGINSTANCE_END_EVENT,
                    KafkaBusinessEvent.MEETINGINSTANCE_END_DATA_READY_EVENT,
                    KafkaBusinessEvent.RECORDING_CREATE_EVENT,
                    KafkaBusinessEvent.RECORDING_UPDATE_EVENT,
                    KafkaBusinessEvent.RECORDING_TRASH_EVENT,
                    KafkaBusinessEvent.RECORDING_RESTORE_EVENT*/
            },
            listType = KafkaEventListType.INCLUDE,
            message = "Username should not be empty!"
    )
    private String username;

    @NotEmptyUnderConditions(
            fromVersion = 2.0,
            message = "UserID should not be empty!"
    )
    private String userID;

    /**
     * Added for MeetingInstance Message v2.0 and Recording Message v2.0 (Feature SPARK-102523)
     * <p>
     * See Details:
     * - MeetingInstance Message v2.0: https://wiki.cisco.com/x/267UHw
     * - Recording Message v2.0: https://wiki.cisco.com/x/ibHUHw
     * - Meeting Message v3.0: https://wiki.cisco.com/display/HFWEB/Meeting+Message+v3.0
     */
    private String ciOrgID;

    /**
     * Added for MeetingInstance Message v2.0 and Recording Message v2.0 (Feature SPARK-102523)
     * <p>
     * See Details:
     * - MeetingInstance Message v2.0: https://wiki.cisco.com/x/267UHw
     * - Recording Message v2.0: https://wiki.cisco.com/x/ibHUHw
     * - Meeting Message v3.0: https://wiki.cisco.com/display/HFWEB/Meeting+Message+v3.0
     */
    private String ciUserUUID;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCiOrgID() {
        return ciOrgID;
    }

    public void setCiOrgID(String ciOrgID) {
        this.ciOrgID = ciOrgID;
    }

    public String getCiUserUUID() {
        return ciUserUUID;
    }

    public void setCiUserUUID(String ciUserUUID) {
        this.ciUserUUID = ciUserUUID;
    }


}
