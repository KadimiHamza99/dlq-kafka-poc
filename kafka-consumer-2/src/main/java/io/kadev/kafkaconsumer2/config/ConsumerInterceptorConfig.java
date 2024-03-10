package io.kadev.kafkaconsumer2.config;

import io.kadev.kafkaconsumer2.utils.Constants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.KafkaHeaders;

import java.nio.charset.StandardCharsets;


@NoArgsConstructor
@Slf4j
public class ConsumerInterceptorConfig<K,V> implements RecordInterceptor<K,V> {
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value(value = "${spring.kafka.main.topic.name}")
    private String sourceTopic;

    @Override
    public ConsumerRecord<K, V> intercept(ConsumerRecord<K, V> record, Consumer<K, V> consumer) {
        try {
            String failedService = new String(
                    record.headers().lastHeader(KafkaHeaders.ORIGINAL_CONSUMER_GROUP).value(),
                    StandardCharsets.UTF_8
            );
            String mainTopic = new String(
                    record.headers().lastHeader(KafkaHeaders.ORIGINAL_TOPIC).value(),
                    StandardCharsets.UTF_8
            );
            if((groupId.equals(failedService) && sourceTopic.equals(mainTopic))
                    && !failedService.isEmpty()
                    && !mainTopic.isEmpty()
            ){
                log.info("Retrying a failed record");
                return record;
            }
            log.info("Already processed, skipped by interceptor");
            return null;
        } catch (Exception e){
            log.info("No original consumer/topic were found");
            return record;
        }
    }

    @Override
    public void failure(ConsumerRecord<K, V> record, Exception exception, Consumer<K, V> consumer) {
//        record.headers()
//                .add(Constants.FAILED_SERVICE, groupId.getBytes());
    }
}
