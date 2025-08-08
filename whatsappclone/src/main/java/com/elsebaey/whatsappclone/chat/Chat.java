package com.elsebaey.whatsappclone.chat;

import com.elsebaey.whatsappclone.common.BaseAuditingEntity;
import com.elsebaey.whatsappclone.message.Message;
import com.elsebaey.whatsappclone.message.MessageState;
import com.elsebaey.whatsappclone.message.MessageType;
import com.elsebaey.whatsappclone.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat")
public class Chat extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(final String senderId) {
        if (recipient.getId().equals(senderId)) {
            return sender.getFirstName() + " " + sender.getLastName();
        } else {
            return recipient.getFirstName() + " " + recipient.getLastName();
        }
    }

    @Transient
    public long getUnreadMessages(final String senderId) {
        return messages
                .stream()
                .filter(message -> message.getRecipientId().equals(senderId))
                .filter(message -> message.getState().equals(MessageState.SENT))
                .count();
    }

    @Transient
    public String getLastMessage() {
        if (messages != null && !messages.isEmpty()) {
            if (!messages.get(0).getType().equals(MessageType.TEXT)) {
                return "Attachment";
            } else {
                return messages.get(0).getContent();
            }
        }
        return null;
    }

    @Transient
    public LocalDateTime getLastMessageDate() {
        if (messages != null && !messages.isEmpty()) {
            return messages.get(0).getCreatedDate();
        }
        return null;
    }
}
