package cn.zero.cloud.platform.controller;

import cn.zero.cloud.platform.common.pojo.dto.UpdateWorldDTO;
import cn.zero.cloud.platform.telemetry.Telemetry;
import cn.zero.cloud.platform.telemetry.factory.TelemetryManualLoggerFactory;
import cn.zero.cloud.platform.telemetry.logger.manual.TelemetryCommonTypeLoggerImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

import static cn.zero.cloud.platform.common.constants.TelemetryConstants.FeatureType.TEST_FEATURE;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.MetricType.TEST_METRIC;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ModuleType.TEST_API;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ObjectType.COMMON_OBJECT;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ObjectType.TEST_OBJECT;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.VerbType.SELECT;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.VerbType.UPDATE;

/**
 * @author Xisun Wang
 * @since 2024/3/6 12:19
 */
@RestController
@RequestMapping(value = "/world")
public class HelloController {
    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = SELECT, objectType = TEST_OBJECT)
    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String helloWorld() {
        return "hello, world!";
    }

    @Telemetry(moduleType = TEST_API, metricType = TEST_METRIC, featureType = TEST_FEATURE, verbType = UPDATE, objectType = TEST_OBJECT, objectID = "#updateWorldDTO.worldSerialNumber", parameters = {"#updateWorldDTO.worldName", "#updateWorldDTO.worldVision"})
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public String updateWorld(@RequestBody UpdateWorldDTO updateWorldDTO) {
        Assert.notNull(updateWorldDTO.getWorldSerialNumber(), "World serial number is required.");
        Assert.notNull(updateWorldDTO.getWorldName(), "World name is required.");
        Assert.notNull(updateWorldDTO.getWorldVision(), "World vision is required.");

        TelemetryCommonTypeLoggerImpl telemetryCommonTypeLoggerImpl = TelemetryManualLoggerFactory.getTelemetryCommonTypeLogger(COMMON_OBJECT, "1122").forUpdate();
        try {
            TimeUnit.SECONDS.sleep(3);
            System.out.println(1 / 0);
        } catch (Exception e) {
            telemetryCommonTypeLoggerImpl.createFailure(e.getMessage());
        }

        return StringUtils.join(updateWorldDTO.getWorldSerialNumber(), "-", updateWorldDTO.getWorldName(), ": ", updateWorldDTO.getWorldVision());
    }
}
