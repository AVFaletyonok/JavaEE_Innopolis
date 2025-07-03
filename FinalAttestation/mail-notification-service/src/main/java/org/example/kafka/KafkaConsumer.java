package org.example.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.NotificationKafkaMessage;
import org.example.service.MonitorService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MonitorService monitorService;

    @KafkaListener(topics = "topic-1", groupId = "group1")
    void listener(NotificationKafkaMessage data) {
        log.info("Received message [{}] in group1", data);
        monitorService.sendNotificationMessage(data);
    }
}
