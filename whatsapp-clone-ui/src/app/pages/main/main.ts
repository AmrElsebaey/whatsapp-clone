import { Component, OnInit } from '@angular/core';
import {ChatResponse, MessageRequest, MessageResponse} from '../../services/models';
import {ChatService, MessageService} from '../../services/services';
import { ChatList } from '../../components/chat-list/chat-list';
import {KeycloakService} from '../../utils/keycloak/keycloak';
import {DatePipe} from '@angular/common';
import {PickerComponent} from '@ctrl/ngx-emoji-mart';
import {FormsModule} from '@angular/forms';
import {EmojiData} from '@ctrl/ngx-emoji-mart/ngx-emoji';

@Component({
  selector: 'app-main',
  imports: [ChatList, DatePipe, PickerComponent, FormsModule],
  templateUrl: './main.html',
  styleUrl: './main.scss'
})
export class Main implements OnInit{

  chats: Array<ChatResponse> = [];
  selectedChat: ChatResponse = {};
  chatMessages: MessageResponse[] = [];
  showEmojis = false;
  messageContent = '';

  constructor(
    private chatService: ChatService,
    private keycloakService: KeycloakService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.getAllChats();
  }

  private getAllChats(): void {
    this.chatService.getChatsByRecipientId()
    .subscribe({
      next: (chats) => {
        this.chats = chats;
      }
    })
  }

  logout() {
    this.keycloakService.logout();
  }

  userProfile() {
    this.keycloakService.accountManagement();
  }

  chatSelected(chatResponse: ChatResponse) {
    this.selectedChat = chatResponse;
    this.getAllChatMessages(chatResponse.id as string);
    this.setMessagesToSeen();
    this.selectedChat.unreadMessages = 0;
  }

  private getAllChatMessages(chatId: string) {
    this.messageService.findChatMessages({
      'chat-id': chatId
    }).subscribe({
      next: (messages) => {
        this.chatMessages = messages;
      }
    })
  }

  private setMessagesToSeen() {
    this.messageService.setMessagesToSeen({
      'chat-id': this.selectedChat.id as string
    }).subscribe({
      next: () => {
      }
    })
  }

  isSelfMessage(message: MessageResponse) {
    return message.senderId === this.keycloakService.userId;

  }

  uploadMedia(target: EventTarget | null) {

  }

  onSelectEmojis(emojiSelected: any) {
    const emoji: EmojiData = emojiSelected.emoji;
    this.messageContent += emoji.native;
  }

  keyDown(event: KeyboardEvent) {
    if (event.key === 'Enter') {
      this.sendMessage()
    }
  }

  onClick() {
    this.setMessagesToSeen()
  }

  sendMessage() {
    if (this.messageContent) {
      const messageRequest: MessageRequest = {
        chatId: this.selectedChat.id,
        senderId: this.getSenderId(),
        recipientId: this.getRecipientId(),
        content: this.messageContent,
        type: 'TEXT'
      };
      this.messageService.saveMessage({
        body: messageRequest
      }).subscribe({
        next: () => {
          const message: MessageResponse = {
            senderId: this.getSenderId(),
            recipientId: this.getRecipientId(),
            content: this.messageContent,
            type: 'TEXT',
            state: 'SENT',
            createdAt: new Date().toString()
          };
          this.selectedChat.lastMessage = this.messageContent;
          this.chatMessages.push(message);
          this.messageContent = '';
          this.showEmojis = false;
        }
      })
    }
  }

  private getSenderId(): string {
    if (this.selectedChat.senderId === this.keycloakService.userId) {
      return this.selectedChat.senderId as string;
    }
    return this.selectedChat.recipientId as string;
  }

  private getRecipientId(): string {
    if (this.selectedChat.senderId === this.keycloakService.userId) {
      return this.selectedChat.recipientId as string;
    }
    return this.selectedChat.senderId as string;
  }
}
