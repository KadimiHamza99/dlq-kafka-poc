package io.kadev.kafkatest.configs;

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


/*
* Initiate Kafka Idempotent Producer (Ensures that duplicates are not introduced due to unexpected retries)
* */
@Configuration
public class KafkaProducer {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

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
        // the maximum number of bytes that will be included in a batch
        configProps.put(
                ProducerConfig.BATCH_SIZE_CONFIG,
                Integer.toString(32*1024));
//        // the number of milliseconds a producer is willing to wait before sending a batch out.
//        configProps.put(
//                ProducerConfig.LINGER_MS_CONFIG,
//                0);
//        configProps.put(
//                ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
//                1);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
//
}
