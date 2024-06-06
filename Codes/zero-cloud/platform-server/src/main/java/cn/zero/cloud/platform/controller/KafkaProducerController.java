package cn.zero.cloud.platform.controller;

import cn.zero.cloud.platform.service.SummaryApiService;
import cn.zero.cloud.platform.telemetry.Telemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static cn.zero.cloud.platform.common.constants.TelemetryConstants.FeatureType.KAFKA_FEATURE;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.FeatureType.TEST_FEATURE;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.MetricType.KAFKA_METRIC;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.MetricType.TEST_METRIC;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ModuleType.KAFKA_API;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ModuleType.TEST_API;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ObjectType.KAFKA_OBJECT;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.ObjectType.TEST_OBJECT;
import static cn.zero.cloud.platform.common.constants.TelemetryConstants.VerbType.SELECT;

/**
 * @author Xisun Wang
 * @since 2024/4/12 15:53
 */
@RestController
@RequestMapping(value = "/kafka/producer")
public class KafkaProducerController {
    private final SummaryApiService summaryApiServiceImpl;

    @Autowired
    public KafkaProducerController(SummaryApiService summaryApiServiceImpl) {
        this.summaryApiServiceImpl = summaryApiServiceImpl;

    }

    @Telemetry(moduleType = KAFKA_API, metricType = KAFKA_METRIC, featureType = KAFKA_FEATURE, objectType = KAFKA_OBJECT)
    @GetMapping(value = "/message/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void sendSummaryMessage() {
        summaryApiServiceImpl.sendSummaryMessage();
    }
}
