package com.elsebaey.whatsappclone.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {

    private String id;
    private String chatName;
    private long unreadMessages;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private boolean isRecipientOnline;
    private String senderId;
    private String recipientId;
}
