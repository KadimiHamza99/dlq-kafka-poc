package io.kadev.retrymanager;

import io.kadev.retrymanager.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.event.KafkaEvent;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaveToLaterListener {
    @Autowired
    private KafkaListenerEndpointRegistry registry;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.kafka.zombie.topic.name}")
    private String zombieTopic;

    @KafkaListener(id = "save-to-later-listener",topics = "#{'${spring.kafka.savetolater.topic.name}'}", autoStartup = "false")
    public void listener(
            ConsumerRecords<String, Object> records,
            Acknowledgment ack
    ) {
        log.info("Listening ...");
        for(ConsumerRecord<String, Object> record : records){
            try {
                log.warn("------------------------------ NEW RECORD ------------------------------");
                String failedService = new String(
                        record.headers().lastHeader(KafkaHeaders.DLT_ORIGINAL_CONSUMER_GROUP).value(),
                        StandardCharsets.UTF_8
                );
                String exception = new String(
                        record.headers().lastHeader(KafkaHeaders.EXCEPTION_CAUSE_FQCN).value(),
                        StandardCharsets.UTF_8
                );
                String topicName = new String(
                        record.headers().lastHeader(KafkaHeaders.ORIGINAL_TOPIC).value(),
                        StandardCharsets.UTF_8
                );

                int retries = Integer.parseInt
                        (new String(record.headers().lastHeader(Constants.NUMBER_OF_RETRIES).value(), StandardCharsets.UTF_8));
                log.info(String.valueOf(retries+1));
                record.headers().add(new RecordHeader(Constants.NUMBER_OF_RETRIES, String.valueOf(++retries).getBytes()));
                if(retries>3) {
                    sendMessage(zombieTopic, record.value(), Arrays.asList(record.headers().toArray()));
                    ack.acknowledge();
                } else {
                    log.info("{} : {} : {}", failedService, topicName, exception);
                    sendMessage(topicName, record.value(), Arrays.asList(record.headers().toArray()));
                    ack.acknowledge();
                }

            } catch (RuntimeException e) {
                log.warn("Error while retrying");
            }
        }
        registry.stop();
    }

    //To stop consumer from consuming if there is nothing to consume
    @EventListener(condition = "event.listenerId.startsWith('save-to-later-listener')")
    public void idleEventHandler(ListenerContainerIdleEvent event){
        log.info("Idle Event Handler will shutdown the save to later listener :: "+ LocalDateTime.now());
        registry.stop();
    }

    @Scheduled(cron = "0 * * * * *")
    public void scheduledMethod() {
        registry.start();
        log.info("Start listening");
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

    private void checkRetriesNumber(ConsumerRecord<String, Object> record, Acknowledgment ack) {

    }

}
