package io.kadev.kafkaconsumer1.config;

import io.kadev.kafkaconsumer1.utils.Constants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;


import java.util.Map;

@NoArgsConstructor
@Slf4j
public class ProducerInterceptorConfig<K, V> implements ProducerInterceptor<K, V> {

    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value(value = "${spring.kafka.topic.name}")
    private String mainTopic;

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> producerRecord) {
        producerRecord.headers()
                .add(Constants.FAILED_SERVICE, groupId.getBytes());
        producerRecord.headers()
                .add(Constants.SOURCE_TOPIC, mainTopic.getBytes());
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<java.lang.String, ?> map) {

    }
}
