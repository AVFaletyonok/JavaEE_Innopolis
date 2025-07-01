package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.ChatMessage;
import org.example.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    final private ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage = chatMessageRepository.saveAndFlush(chatMessage);
        return chatMessage;
    }

//    public Long countNewMessages(String recipientId, String senderId) {
//        return chatMessageRepository
//                .countByRecipientIdAndSenderIdAndStatus(recipientId,
//                                                        senderId,
//                                                        MessageStatus.RECEIVED);
//    }
//
//    public List<ChatMessage> findChatMessages(String recipientId, String senderId) {
//        Optional<String> chatId = chatRoomService.getChatId(recipientId, senderId, false);
//        if (chatId.isEmpty()) {
//            return  new ArrayList<>();
//        }
//
//        List<ChatMessage> messages = chatMessageRepository.findByChatId(chatId.get());
//        if (!messages.isEmpty()) {
//            messages.stream().map(chatMessage -> {
////                chatMessage.setStatus(MessageStatus.DELIVERED);
//                return chatMessageRepository.saveAndFlush(chatMessage);
//            });
//        }
//        return messages;
//    }
//
//    public ChatMessage findById(Long id) {
//        return chatMessageRepository
//                .findById(id)
//                .map(chatMessage -> {
////                    chatMessage.setStatus(MessageStatus.DELIVERED);
//                    return chatMessageRepository.saveAndFlush(chatMessage);
//                })
//                .orElseThrow(() -> new ResourceNotFoundException("Can't find the message (" + id + ")"));
//    }
}
