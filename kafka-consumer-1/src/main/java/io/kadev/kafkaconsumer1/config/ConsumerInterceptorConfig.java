package io.kadev.kafkaconsumer1.config;

import io.kadev.kafkaconsumer1.utils.Constants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.KafkaHeaders;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


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
            String retries = new String(
                    record.headers().lastHeader(Constants.NUMBER_OF_RETRIES).value(),
                    StandardCharsets.UTF_8
            );
            //Le cas où le service de batch à renvoyer un ancien record pour le rejouer
            if((groupId.equals(failedService) && sourceTopic.equals(mainTopic))
                    && !failedService.isEmpty()
                    && !mainTopic.isEmpty()
            ){
                log.info("Retrying a failed record, retry number : {}", retries);
                return record;
            }
            //Si le record a été déjà bien consommer par ce service et qui est republié pour un autre consommateur qui ne l'a pas bien traité
            log.info("Already processed, skipped by interceptor");
            return null;
        } catch (Exception e){
            //Le record est traité pour la première fois par ce consommateur
            log.info("No original consumer/topic were found");
            return record;
        }
    }

    @Override
    public void failure(ConsumerRecord<K, V> record, Exception exception, Consumer<K, V> consumer) {
        try {
            new String(
                    record.headers().lastHeader(Constants.NUMBER_OF_RETRIES).value(),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            log.warn("Number of retries have been set");
            record.headers()
                    .add(Constants.NUMBER_OF_RETRIES, "0".getBytes());
        }

    }
}
