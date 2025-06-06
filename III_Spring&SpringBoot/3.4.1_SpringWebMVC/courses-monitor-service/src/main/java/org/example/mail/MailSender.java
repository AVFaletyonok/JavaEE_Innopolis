package org.example.mail;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.student.StudentResponse;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSender {

    public void sendNotificationMessage(final StudentResponse student, final String courseName) {

        // Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // create some properties and get the Session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("LLIurick@mail.ru", "C7brbJ7j8ahh9jOQyAHk");
            }
        });

        try {
            // create a message
            Message msg = new MimeMessage(session);
            // set the from and to address
            InternetAddress addressFrom = new InternetAddress("LLIurick@mail.ru");
            msg.setFrom(addressFrom);
            InternetAddress addressTo = new InternetAddress(student.getEmail());
            msg.setRecipient(Message.RecipientType.TO, addressTo);

            // Setting the Subject and Content Type
            msg.setSubject("Notification message: The course '" + courseName + "' is started!");
            String text = MessageFormat.format("Hi, {0} {1} {2}!/nWe are pleased to inform you:/n" +
                            "The course '{3}' that you registered for has been started today.",
                    student.getFirstName(), student.getPatronymic(), student.getLastName(),
                    courseName);
            msg.setContent(text, "text/plain");

            log.info("Sending notification email to: {}", addressTo.getAddress());
            Transport.send(msg);
            log.info("Notification email to: {} was successfully send.", addressTo.getAddress());
        } catch (MessagingException e) {
            log.info("Notification email wasn't been sent to: {} due to an exception\n {}",
                    student.getEmail(), e.getMessage());
        }
    }
}
