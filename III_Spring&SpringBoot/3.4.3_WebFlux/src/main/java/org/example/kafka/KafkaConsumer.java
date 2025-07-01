package org.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.KafkaMessage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ReactiveKafkaConsumerTemplate<String, KafkaMessage> reactiveKafkaConsumerTemplate;

    @KafkaListener(topics = "message", groupId = "group_id")
    public void listen(KafkaMessage message) {
        log.info("message: {}", message);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void readMessage() {
//        reactiveKafkaConsumerTemplate
//                .receiveAutoAck()
//                .doOnNext(r -> {
//                    log.info("The message from kafka {} {} has been read", r.key(), r.value().getMessage());
//                })
//                .doOnComplete(() -> log.info("All messages have been read"))
//                .subscribe();
//    }
}
