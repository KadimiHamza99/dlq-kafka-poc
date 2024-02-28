package io.kadev.kafkaconsumer1;

import io.kadev.kafkaconsumer1.exceptions.NonRetryableException;
import io.kadev.kafkaconsumer1.exceptions.RetryableException;
import io.kadev.kafkaconsumer1.models.InputModel;
import io.kadev.kafkaconsumer1.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;


@Component
@Slf4j
@RequiredArgsConstructor
public class ListenerKafka {

    @RetryableTopic(kafkaTemplate = "kafkaTemplate",
            attempts = "4",
            traversingCauses = "true",
            retryTopicSuffix = ".retry",
            dltTopicSuffix = ".dlt",
            sameIntervalTopicReuseStrategy = SameIntervalTopicReuseStrategy.SINGLE_TOPIC,
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            exclude = {
                NonRetryableException.class,
            },
            backoff = @Backoff(delay = 3000)
    )
    @KafkaListener(topics = Constants.MAIN_TOPIC)
    public void listen(
            @Payload InputModel message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) int offset,
            Acknowledgment acknowledgment
    ) {
        log.info("message partition {} offset {}", partition, offset);

        if(message.getBody().getAXAContextHeader().getAems_contextHeader().getAems_functionalID().contains("1BD5EI5I5YE4")){
            throw new RetryableException();
        }

        acknowledgment.acknowledge();
    }


    @DltHandler
    public void processMessage(
            InputModel message,
            @Header(KafkaHeaders.ORIGINAL_OFFSET) byte[] offset,
            @Header("SOURCE_TOPIC") String sourceTopic,
            @Header("CONSUMER_GROUP") String failedService,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
            Acknowledgment acknowledgment
    ) {
        log.warn("DltHandler processMessage {}", message);
        log.warn(sourceTopic);
        log.warn(failedService);
//        log.warn(""+ ByteBuffer.wrap(offset).getLong());
//        log.warn(descException);
//        log.warn(errorMessage);
        acknowledgment.acknowledge();
    }

}
