package com.elsebaey.whatsappclone.chat;

import org.springframework.stereotype.Service;

@Service
public class ChatMapper {
    public ChatResponse toChatResponse(Chat chat, String senderId) {
        return ChatResponse.builder()
                .id(chat.getId())
                .chatName(chat.getChatName(senderId))
                .unreadMessages(chat.getUnreadMessages(senderId))
                .lastMessage(chat.getLastMessage())
                .lastMessageTime(chat.getLastMessageDate())
                .isRecipientOnline(chat.getRecipient().isUserOnline())
                .senderId(chat.getSender().getId())
                .recipientId(chat.getRecipient().getId())
                .build();
    }
}
