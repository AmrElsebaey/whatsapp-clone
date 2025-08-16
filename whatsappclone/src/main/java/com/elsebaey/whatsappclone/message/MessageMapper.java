package com.elsebaey.whatsappclone.message;

import com.elsebaey.whatsappclone.chat.Chat;
import com.elsebaey.whatsappclone.file.FileUtils;
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
                .media(FileUtils.readFileFromLocation(message.getMediaFilePath()))
                .build();
    }
}
