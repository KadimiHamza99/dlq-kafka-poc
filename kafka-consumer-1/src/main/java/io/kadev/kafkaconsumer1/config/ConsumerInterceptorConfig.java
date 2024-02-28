package io.kadev.kafkaconsumer1.config;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.RecordInterceptor;

import java.util.Map;

@NoArgsConstructor
@Slf4j
public class ConsumerInterceptorConfig<K,V> implements RecordInterceptor<K,V> {
    @Override
    public ConsumerRecord<K, V> intercept(ConsumerRecord<K, V> record, Consumer<K, V> consumer) {
        log.info("CONSUMER INTERCEPTOR");
        return record;
    }
}
