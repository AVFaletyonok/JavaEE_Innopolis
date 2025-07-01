package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.NotificationKafkaMessage;
import org.example.mail.MailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private final MailSender mailSender;

    public void sendNotificationMessage(final NotificationKafkaMessage message) {

        mailSender.sendNotificationMessage(message.getEmail(), message.getMessage());
    }
}