package org.example.repository;

import org.example.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//    long countByRecipientIdAndSenderIdAndStatus(String recipientId,
//                                                String senderId,
//                                                MessageStatus status);
//
//    List<ChatMessage> findByChatId(String chatId);
}
