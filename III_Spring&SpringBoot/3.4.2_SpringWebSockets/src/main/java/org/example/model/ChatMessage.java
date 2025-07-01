package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue
    private Long id;
//    private String senderId;
//    private String recipientId;
    private String senderName;
//    private String recipientName;
    private String content;
    private MessageType type;
//    private LocalTime timestamp;
//    private MessageStatus status;
}
