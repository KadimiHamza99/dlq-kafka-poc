package io.kadev.kafkatest.configs;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class InfrastructureConfiguration {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.topic.name}")
    private String mainTopic;
    @Value(value = "${spring.kafka.replication.factor}")
    private short replicationFactor;
    @Value(value = "${spring.kafka.partition.number}")
    private int partitionNumber;
    @Value(value = "${spring.kafka.zombie.topic.name}")
    private String zombieTopic;
    @Value(value = "${spring.kafka.savetolater.topic.name}")
    private String saveToLaterTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic mainTopic() {
        return new NewTopic(mainTopic, partitionNumber, replicationFactor);
    }

    @Bean
    public NewTopic zombieTopic() {
        return new NewTopic(zombieTopic, partitionNumber, replicationFactor);
    }

    @Bean
    public NewTopic saveToLaterTopic() {
        return new NewTopic(saveToLaterTopic, partitionNumber, replicationFactor);
    }

}
