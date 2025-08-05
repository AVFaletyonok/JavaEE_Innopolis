package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.ChatMessage;
import org.example.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage processMessage(@Payload ChatMessage chatMessage) {
//        Optional<String> chatId =
//                chatRoomService.getChatId(chatMessage.getRecipientId(), chatMessage.getSenderId(), true);
//        chatMessage.setChatId(chatId.get());
//        ChatMessage savedMessage = chatMessageService.save(chatMessage);

//        simpMessagingTemplate
//                .convertAndSendToUser(
//                        chatMessage.getRecipientId(), "/message",
//                        new ChatNotification(savedMessage.getId(),
//                                savedMessage.getSenderId(),
//                                savedMessage.getRecipientId())
//                );

        chatMessageService.save(chatMessage);

        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                                SimpMessageHeaderAccessor headerAccessor) {
        // Add username in WebSocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderName());

        chatMessageService.save(chatMessage);

        return chatMessage;
    }

    /*
    @GetMapping("/messages/{recipientId}/{senderId}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable String recipientId,
                                                 @PathVariable String senderId) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(recipientId, senderId));
    }

    @GetMapping("/messages/{recipientId}/{senderId}")
    public ResponseEntity<?> findChatMessages(@PathVariable String recipientId,
                                              @PathVariable String senderId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(recipientId, senderId));
    }

    @GetMapping("/message/{id}")
    public ResponseEntity<?> findMessage(@PathVariable String id) {
        return ResponseEntity
                .ok(chatMessageService.findById(Long.parseLong(id)));
    }
     */
}
