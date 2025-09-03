import { Component, OnInit } from '@angular/core';
import {ChatResponse, MessageResponse} from '../../services/models';
import {ChatService, MessageService} from '../../services/services';
import { ChatList } from '../../components/chat-list/chat-list';
import {KeycloakService} from '../../utils/keycloak/keycloak';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-main',
  imports: [ChatList, DatePipe],
  templateUrl: './main.html',
  styleUrl: './main.scss'
})
export class Main implements OnInit{

  chats: Array<ChatResponse> = [];
  selectedChat: ChatResponse = {};
  chatMessages: MessageResponse[] = [];

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
      next: (res) => {
        this.chats = res;
      },
      error: (err) => {
        console.error('Error fetching chats:', err);
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
    // this.selectedChat.unreadMessages = 0;
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

  }

  isSelfMessage(message: MessageResponse) {
    return message.senderId === this.keycloakService.userId;

  }
}
