package cn.zero.cloud.platform.kafka.producer.resending.client;

import cn.zero.cloud.platform.kafka.common.exception.KafkaProducerException;
import cn.zero.cloud.platform.kafka.producer.resending.Message;
import cn.zero.cloud.platform.kafka.producer.resending.ResendingConstants;
import cn.zero.cloud.platform.kafka.utils.KafkaCommonUtil;
import cn.zero.cloud.platform.utils.PlatFormJsonUtil;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Base64;

public interface Resender {

    ResponseEntity<String> resendMessage(ProducerRecord producerRecord) throws KafkaProducerException;

    class Default implements Resender {
        private static final Logger logger = LoggerFactory.getLogger(Default.class);

        private String wnsVip;
        private String appName;

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        private final RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofMillis(10000))
                .setReadTimeout(Duration.ofMillis(10000)).build();

        public Default(String wnsVip, String appName) {
            this.wnsVip = wnsVip;
            this.appName = appName;
        }

        @Override
        public ResponseEntity<String> resendMessage(ProducerRecord producerRecord) throws KafkaProducerException {

            Message message = new Message();
            message.setMessageKey(producerRecord.key().toString());
            message.setMessageTopic(producerRecord.topic());
            Headers headers = producerRecord.headers();
            message.setRecordHeaders(KafkaCommonUtil.getKafkaHeaderString(headers));
            String messageBody;
            try {
                messageBody = PlatFormJsonUtil.serializeToJson(producerRecord.value());
            } catch (Exception e) {
                logger.error("message convert to json string error {}", e.getLocalizedMessage());
                throw new KafkaProducerException(e.getLocalizedMessage());
            }
            message.setMessageBody(messageBody);
            try {
                HttpEntity<String> requestEntity = buildRequestEntity(message);
                return restTemplate.postForEntity(wnsVip, requestEntity, String.class);
            } catch (Exception e) {
                logger.error("Resender.Default failure resend message + ", e.getLocalizedMessage());
                throw new KafkaProducerException(e.getLocalizedMessage());
            }
        }

        private HttpEntity<String> buildRequestEntity(Message message) throws /*WbxAppTokenException,*/ KafkaProducerException {
            return new HttpEntity<>(buildRequestBody(message), buildRequestHeaders(message));
        }

        private String buildRequestBody(Message message) throws KafkaProducerException {

            String result = "";
            try {
                result = PlatFormJsonUtil.serializeToJson(message);
            } catch (Exception e) {
                logger.error("covert failure: ", result);
                throw new KafkaProducerException(e.getLocalizedMessage());
            }
            return result;
        }


        private HttpHeaders buildRequestHeaders(Message message) /*throws WbxAppTokenException*/ {
            HttpHeaders headers = new HttpHeaders();
            String appName = this.appName;
            String appToken = null;
            /*try {
                appToken = AppTokenUtil.makeTicket2(appName);
            } catch (WbxAppTokenException e) {
                throw e;
            }*/

            headers.add(ResendingConstants.TRACKING_ID, message.getMessageKey());
            headers.add(ResendingConstants.Content_TYPE, "application/json");
            headers.add(ResendingConstants.APPNAME, appName);
            headers.add(ResendingConstants.APPTOKEN, appToken);
            String token = Base64.getEncoder().encodeToString((appName + ":" + appToken).getBytes());
            headers.add(ResendingConstants.HEADER_AUTHORIZATION, ResendingConstants.AUTH_PREFIX_APPTOKEN + " " + token);
            return headers;
        }
    }

}
