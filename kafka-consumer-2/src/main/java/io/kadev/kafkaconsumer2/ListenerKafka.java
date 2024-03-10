package io.kadev.kafkaconsumer2;


import io.kadev.kafkaconsumer2.exceptions.NonRetryableException;
import io.kadev.kafkaconsumer2.exceptions.RetryableException;
import io.kadev.kafkaconsumer2.models.InputModel;
import io.kadev.kafkaconsumer2.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Component
@Slf4j
@RequiredArgsConstructor
public class ListenerKafka {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value(value = "${spring.kafka.zombie.topic.name}")
    private String zombieTopic;
    @Value(value = "${spring.kafka.savetolater.topic.name}")
    private String saveToLaterTopic;

    @RetryableTopic(kafkaTemplate = "kafkaTemplate",
            attempts = "4",
            traversingCauses = "true",
            retryTopicSuffix = "#{'.retry.'.concat('${spring.kafka.consumer.group-id}')}",
            dltTopicSuffix = "#{'.dlt.'.concat('${spring.kafka.consumer.group-id}')}",
            sameIntervalTopicReuseStrategy = SameIntervalTopicReuseStrategy.SINGLE_TOPIC,
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            exclude = {
                NonRetryableException.class,
            },
            backoff = @Backoff(delay = 3000)
    )
    @KafkaListener(topics = "#{'${spring.kafka.main.topic.name}'}")
    public void listener(
            @Payload InputModel message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) int offset,
            Acknowledgment acknowledgment
    ) {
        try{
            log.info("message partition {} offset {}", partition, offset);
            if(message.getBody().getAXAContextHeader().getAems_contextHeader().getAems_functionalID().contains("1BD5EI5I5YE4")){
                throw new RetryableException();
            }
        } finally {
            log.warn("Message committed");
            acknowledgment.acknowledge();
        }

    }


    @DltHandler
    public void dltHandler(
            InputModel message,
            @Header(KafkaHeaders.ORIGINAL_OFFSET) byte[] offset,
            @Header(KafkaHeaders.ORIGINAL_TOPIC) String sourceTopic,
            @Header(KafkaHeaders.ORIGINAL_CONSUMER_GROUP) String failedService,
            @Header(KafkaHeaders.EXCEPTION_CAUSE_FQCN) String exception,
            Acknowledgment acknowledgment
    ) {
        log.warn(exception);
        log.warn(sourceTopic);
        log.warn(failedService);

        if(RetryableException.class.getTypeName().equals(exception)){
            sendMessage(saveToLaterTopic, message);
        } else {
            sendMessage(zombieTopic, message);
        }


    }

    private void sendMessage(String topicName, Object message){
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, UUID.randomUUID().toString(), message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent to {}", topicName);
            } else {
                log.error("Failed to send message to {}", topicName);
            }
        });
    }

}
