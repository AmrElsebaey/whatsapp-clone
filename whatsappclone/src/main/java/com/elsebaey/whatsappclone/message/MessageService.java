package com.elsebaey.whatsappclone.message;

import com.elsebaey.whatsappclone.chat.Chat;
import com.elsebaey.whatsappclone.chat.ChatRepository;
import com.elsebaey.whatsappclone.file.FileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final FileService fileService;

   public void saveMessage(MessageRequest messageRequest) {
       Chat chat = chatRepository.findById(messageRequest.getChatId())
               .orElseThrow(() -> new EntityNotFoundException("Chat with id " + messageRequest.getChatId() + " not found"));

       Message message = new Message();
       message.setContent(messageRequest.getContent());
       message.setChat(chat);
       message.setSenderId(messageRequest.getSenderId());
       message.setRecipientId(messageRequest.getRecipientId());
       message.setType(messageRequest.getType());
       message.setState(MessageState.SENT);

       messageRepository.save(message);

       // todo notify the recipient via WebSocket or other means
    }

    public List<MessageResponse> findChatMessages(String chatId) {
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    @Transactional
    public void setMessagesToSeem(String chatId, Authentication authentication) {
       Chat chat = chatRepository.findById(chatId)
               .orElseThrow(() -> new EntityNotFoundException("Chat with id " + chatId + " not found"));

//       final String recipientId = getRecipientId(chat, authentication);

       messageRepository.setMessagesToSeenByChatId(chatId, MessageState.SEEN);

         // todo notify the recipient via WebSocket or other means
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat with id " + chatId + " not found"));

        final String senderId = getSenderId(chat, authentication);
        final String recipientId = getRecipientId(chat, authentication);

        final String filePath = fileService.saveFile(file, senderId);
        
        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setChat(chat);
        message.setType(MessageType.IMAGE);
        message.setState(MessageState.SENT);
        message.setMediaFilePath(filePath);

        messageRepository.save(message);

        // todo notify the recipient via WebSocket or other means
    }

    private String getSenderId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getSender().getId();
        }
        return chat.getRecipient().getId();
    }

    private String getRecipientId(Chat chat, Authentication authentication) {
    if (chat.getSender().getId().equals(authentication.getName())) {
        return chat.getRecipient().getId();
    }
       return chat.getSender().getId();
    }
}
