package cn.zero.cloud.platform.controller;

import cn.zero.cloud.platform.telemetry.Telemetry;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static cn.zero.cloud.platform.common.constants.TelemetryConstants.FeatureType.TEST_FEATURE;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.MetricType.TEST_METRIC;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ModuleType.TEST_API;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ObjectType.TEST_OBJECT;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.VerbType.UPDATE;

/**
 * @author Xisun Wang
 * @since 6/18/2024 13:42
 */
@RestController
public class AuthenticationController {

    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = UPDATE, objectType = TEST_OBJECT, objectID = "#updateWorldDTO.worldSerialNumber", parameters = {"#updateWorldDTO.worldName", "#updateWorldDTO.worldVision"})
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasCustomPermission('get')")
    public String updateWorld() {
        return "test success";
    }
}
