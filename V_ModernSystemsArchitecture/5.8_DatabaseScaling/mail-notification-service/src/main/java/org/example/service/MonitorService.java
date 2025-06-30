package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.client.StudentsRestClient;
import org.example.dto.student.StudentResponse;
import org.example.mail.MailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private final StudentsRestClient studentsClient;
    private final MailSender mailSender;

    public void sendMessagesAll(final String message) {
        List<StudentResponse> students = studentsClient.getStudents();

        mailSender.sendNotificationMessage(students, message);
    }
}