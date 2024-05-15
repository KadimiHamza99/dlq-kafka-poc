package io.kadev.kafkaconsumer1;

import io.kadev.kafkaconsumer1.exceptions.NonRetryableException;
import io.kadev.kafkaconsumer1.exceptions.RetryableException;
import io.kadev.kafkaconsumer1.models.InputModel;
import io.kadev.kafkaconsumer1.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.List;
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
            attempts = "2",
            autoCreateTopics = "true",
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
    @KafkaListener(topics = "#{'${spring.kafka.main.topic.name}'}",
            groupId = "#{'${spring.kafka.consumer.group-id}'}"
    )
    public void listener(
            @Payload InputModel message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) int offset,
            Acknowledgment acknowledgment
    ) {
        try{
            log.info("message partition {} offset {}", partition, offset);
            if(message.getBody().getAXAContextHeader().getAems_contextHeader().getAems_functionalID().contains("1IAJD98DZA9")){
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
//            @Header(KafkaHeaders.ORIGINAL_CONSUMER_GROUP) String failedService,
            @Header(KafkaHeaders.EXCEPTION_CAUSE_FQCN) String exception,
            @Header(KafkaHeaders.DLT_ORIGINAL_CONSUMER_GROUP) String failedService,
            @Header(Constants.NUMBER_OF_RETRIES) String retries,
            Acknowledgment acknowledgment
    ) {
        log.warn(exception);
        log.warn(sourceTopic);
        log.warn(failedService);
        log.warn(retries);
        List <org.apache.kafka.common.header.Header> headers = new ArrayList<>();
        headers.add(new RecordHeader(KafkaHeaders.EXCEPTION_CAUSE_FQCN, exception.getBytes()));
        headers.add(new RecordHeader(KafkaHeaders.ORIGINAL_TOPIC, sourceTopic.getBytes()));
        headers.add(new RecordHeader(KafkaHeaders.DLT_ORIGINAL_CONSUMER_GROUP, failedService.getBytes()));
        headers.add(new RecordHeader(Constants.NUMBER_OF_RETRIES, retries.getBytes()));
        if(RetryableException.class.getTypeName().equals(exception)){
            sendMessage(saveToLaterTopic, message, headers);
        } else if(NonRetryableException.class.getName().equals(exception)){
            sendMessage(zombieTopic, message, headers);
        }
        acknowledgment.acknowledge();
    }

    private void sendMessage(String topicName, Object message, List <org.apache.kafka.common.header.Header> headers){
        ProducerRecord<String, Object> record = new ProducerRecord <>
                (topicName, null, System.currentTimeMillis(), UUID.randomUUID().toString(), message, headers);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message sent to {}", topicName);
            } else {
                log.error("Failed to send message to {}", topicName);
            }
        });
    }

}
