package io.kadev.kafkaconsumer1;

import io.kadev.kafkaconsumer1.exceptions.NonRetryableException;
import io.kadev.kafkaconsumer1.models.InputModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
@Slf4j
public class ListenerKafka {

    @RetryableTopic(kafkaTemplate = "kafkaTemplate",
            attempts = "4",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            exclude = {
                    NonRetryableException.class
            },
            backoff = @Backoff(delay = 3000, multiplier = 1.1, maxDelay = 15000)
    )
    @KafkaListener(topics = "io.kadev.kafka.poc")
    public void listen(
            @Payload InputModel message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) int offset,
            Acknowledgment acknowledgment
    ) {
        log.info("message partition {} offset {}", partition, offset);
        if(message.getBody().getAXAContextHeader().getAems_contextHeader().getAems_functionalID().contains("1BD5EI5I5YE4")){
            throw new NonRetryableException();
        }
        acknowledgment.acknowledge();
    }


    @org.springframework.kafka.annotation.DltHandler
    public void processMessage(
            InputModel message,
            @Header(KafkaHeaders.ORIGINAL_OFFSET) byte[] offset,
            @Header(KafkaHeaders.EXCEPTION_FQCN) String descException,
            @Header(KafkaHeaders.EXCEPTION_STACKTRACE) String stacktrace,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
            Acknowledgment acknowledgment
    ) {
        log.warn("DltHandler processMessage {}", message);
        log.warn(""+ ByteBuffer.wrap(offset).getLong());
        log.warn(descException);
        log.warn(errorMessage);
        acknowledgment.acknowledge();
    }

}
