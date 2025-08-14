package com.elsebaey.whatsappclone.chat;

import com.elsebaey.whatsappclone.user.User;
import com.elsebaey.whatsappclone.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByRecipientId(Authentication currentUser) {
        final String userId = currentUser.getName();
        return chatRepository.findChatBySenderId(userId)
                .stream()
                .map(chat -> chatMapper.toChatResponse(chat, userId))
                .toList();
    }

    public String createChat(String senderId, String recipientId) {
        Optional<Chat> existingChat = chatRepository.findChatBySenderAndRecipient(senderId, recipientId);
        if (existingChat.isPresent()) {
            return existingChat.get().getId();
        }

        User sender = userRepository.findByPublicId(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + senderId + " not found"));
        User recipient = userRepository.findByPublicId(recipientId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + recipientId + " not found"));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setRecipient(recipient);
        Chat savedChat = chatRepository.save(chat);
        return savedChat.getId();
    }
}
