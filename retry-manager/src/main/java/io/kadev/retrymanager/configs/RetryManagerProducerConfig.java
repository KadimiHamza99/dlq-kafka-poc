package io.kadev.retrymanager.configs;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RetryManagerProducerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /*
     * Configuration for the retry producer topic
     * */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        configProps.put(
                ProducerConfig.ACKS_CONFIG,
                "all");
//        configProps.put(ProducerConfig.RETRIES_CONFIG,Integer.toString(Integer.MAX_VALUE));
//        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,1000);
//        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,500);
        // the maximum number of bytes that will be included in a batch
        configProps.put(
                ProducerConfig.BATCH_SIZE_CONFIG,
                Integer.toString(32*1024));
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        return kafkaTemplate;
    }
}
