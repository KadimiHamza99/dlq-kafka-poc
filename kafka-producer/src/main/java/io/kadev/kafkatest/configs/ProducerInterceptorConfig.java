package io.kadev.kafkatest.configs;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;

import java.util.Map;

@NoArgsConstructor
@Slf4j
public class ProducerInterceptorConfig<K,V> implements ProducerInterceptor<K,V> {

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> producerRecord) {
        //Pour tester est ce que le consommateur va re-traiter les messages echoués
//        producerRecord.headers()
//                .add(KafkaHeaders.ORIGINAL_CONSUMER_GROUP, "kadev1".getBytes());
//        producerRecord.headers()
//                .add(KafkaHeaders.ORIGINAL_TOPIC, "io.kadev.kafka.poc".getBytes());
        return producerRecord;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
