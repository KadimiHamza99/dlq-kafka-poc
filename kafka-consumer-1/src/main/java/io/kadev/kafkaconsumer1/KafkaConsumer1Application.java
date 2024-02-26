package io.kadev.kafkaconsumer1;

import io.kadev.kafkaconsumer1.models.InputModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class KafkaConsumer1Application implements CommandLineRunner{

    @Value(value = "${spring.kafka.topic.name}")
    private String mainTopic;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

//    @KafkaListener(
//            topicPartitions = @TopicPartition(topic = "io.kadev.kafka.poc",
//            partitionOffsets = {
//                    @PartitionOffset(partition = "0", initialOffset = "0"),
//                    @PartitionOffset(partition = "1", initialOffset = "0"),
//                    @PartitionOffset(partition = "2", initialOffset = "0"),
//                    @PartitionOffset(partition = "3", initialOffset = "0"),
//                    @PartitionOffset(partition = "4", initialOffset = "0")
//            })
//    )
    @KafkaListener(topics = "io.kadev.kafka.poc")
    public void listen(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) int offset,
            Acknowledgment acknowledgment
    ) {
        log.info("Received Message in group io.kadev.kafka.poc : " + message + " " + partition + " " + offset);
        acknowledgment.acknowledge();
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumer1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    }
}
