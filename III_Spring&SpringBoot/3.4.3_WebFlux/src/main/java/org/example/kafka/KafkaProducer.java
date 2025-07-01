package org.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.KafkaMessage;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.SenderResult;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final ReactiveKafkaProducerTemplate<String, KafkaMessage> reactiveKafkaProducerTemplate;

    public Flux<SenderResult<Void>> sendMessage(Integer count) {
        return Flux.range(0, count)
                .map(i -> {
                    String message = "Message N " + i + " for kafka";
                    var kafkaMessage = KafkaMessage.builder()
                            .id(Long.valueOf(i))
                            .message(message)
                            .sendTime(LocalDateTime.now())
                            .build();

                    return MessageBuilder.withPayload(kafkaMessage)
                            .setHeader(MessageHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();
                })
                .flatMap(kafkaMessage -> {
                    log.info("Sending the message to kafka: {}", kafkaMessage.getPayload().getId());
                    return reactiveKafkaProducerTemplate.send("message", kafkaMessage);
                });
    }
}
