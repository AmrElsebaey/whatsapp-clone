package com.elsebaey.whatsappclone.message;

import com.elsebaey.whatsappclone.chat.Chat;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {


    public MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .type(message.getType())
                .state(message.getState())
                .senderId(message.getSenderId())
                .recipientId(message.getRecipientId())
                .createdAt(message.getCreatedDate())
                // todo handle media conversion if needed
                .build();
    }
}
