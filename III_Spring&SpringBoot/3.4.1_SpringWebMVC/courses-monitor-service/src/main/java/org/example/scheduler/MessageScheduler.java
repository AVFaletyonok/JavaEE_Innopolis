package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mail.MailSender;
import org.example.service.CourseMonitorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageScheduler {
    private final MailSender defaultMailSender;
    private final CourseMonitorService courseMonitorService;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void postScheduledNotificationMails() {

        courseMonitorService.checkCourses();
    }
}
