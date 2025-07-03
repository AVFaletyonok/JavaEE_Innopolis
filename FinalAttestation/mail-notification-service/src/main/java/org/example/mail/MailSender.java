package org.example.mail;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSender {

    public void sendNotificationMessage(final String email, final String message) {

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
                return new PasswordAuthentication("LLIurick@mail.ru", "574h8OpEzjJcH97JXpUl");
            }
        });

        try {
            // create a message
            Message msg = new MimeMessage(session);
            // set the from and to address
            InternetAddress addressFrom = new InternetAddress("LLIurick@mail.ru");
            msg.setFrom(addressFrom);

            InternetAddress addressTo = new InternetAddress(email);
            msg.setRecipient(Message.RecipientType.TO, addressTo);

            // Setting the Subject and Content Type
            msg.setSubject("Notification transaction message.");
            msg.setContent(message, "text/plain");

            log.info("Sending notification transaction email to: {}", addressTo.getAddress());
            Transport.send(msg);
            log.info("Notification transaction email to: {} was successfully send.", addressTo.getAddress());

        } catch (MessagingException e) {
            log.info("Notification email wasn't been sent to: {} due to an exception\n {}",
                    email, e.getMessage());
        }
    }
}
