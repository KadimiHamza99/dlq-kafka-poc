package io.kadev.retrymanager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.event.KafkaEvent;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaveToLaterListener {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

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
                log.info("{} : {} : {}", failedService, topicName, exception);
                ack.acknowledge();
            } catch (RuntimeException e) {
                ack.nack(Duration.ZERO);
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

}
