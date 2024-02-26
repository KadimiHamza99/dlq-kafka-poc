package io.kadev.kafkatest;

import io.kadev.kafkatest.models.InputModel;
import io.kadev.kafkatest.utils.ObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@Slf4j
public class KafkaTestApplication implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value(value = "${spring.kafka.topic.name}")
    private String mainTopic;

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(mainTopic, UUID.randomUUID().toString(), message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]" + result.getRecordMetadata());
            } else {
                log.error("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        InputModel input = ObjectFactory.request1();
        for(int i = 1; i>0; i--){
            sendMessage(String.valueOf(i));
        }
    }
}
