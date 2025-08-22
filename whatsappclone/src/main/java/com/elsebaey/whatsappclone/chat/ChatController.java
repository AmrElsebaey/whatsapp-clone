package com.elsebaey.whatsappclone.chat;

import com.elsebaey.whatsappclone.common.StringResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
@Tag(name = "Chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<StringResponse> createChat(
            @RequestParam(name = "senderId") String senderId,
            @RequestParam(name = "recipientId") String recipientId
    ) {
        final String chatId = chatService.createChat(senderId, recipientId);
        StringResponse response = StringResponse.builder()
                .response(chatId)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatsByRecipientId(
            Authentication authentication
    ) {
        return ResponseEntity.ok(chatService.getChatsByRecipientId(authentication));
    }
}
