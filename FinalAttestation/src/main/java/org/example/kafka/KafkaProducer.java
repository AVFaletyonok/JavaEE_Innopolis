package org.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.NotificationKafkaMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, NotificationKafkaMessage> kafkaTemplate;

    public void sendMessage(NotificationKafkaMessage message, String topicName) {
        log.info("Sending : {} to {}", message.getMessage(), message.getEmail());
        log.info("-------------");

        kafkaTemplate.send(topicName, message);

        // for setting created topic
//        kafkaTemplate.getKafkaAdmin().setCreateOrModifyTopic(topic -> topic.);
    }
}
