package cn.zero.cloud.platform.common.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xisun Wang
 * @since 2024/3/21 9:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateWorldDTO {
    private String worldSerialNumber;

    private String worldName;

    private String worldVision;
}
