package io.kadev.kafkatest;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

@SpringBootApplication
@Slf4j
public class KafkaTestApplication implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value(value = "${spring.kafka.topic.name}")
    private String mainTopic;

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(mainTopic, message);
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
        for(int i = 100000; i>0; i--){
            sendMessage("HAMZA KADIMI");
        }
    }
}
