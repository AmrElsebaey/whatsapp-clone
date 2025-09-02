import { Component, OnInit } from '@angular/core';
import { ChatResponse } from '../../services/models';
import { ChatService } from '../../services/services';
import { ChatList } from '../../components/chat-list/chat-list';
import {KeycloakService} from '../../utils/keycloak/keycloak';

@Component({
  selector: 'app-main',
  imports: [ChatList],
  templateUrl: './main.html',
  styleUrl: './main.scss'
})
export class Main implements OnInit{

  chats: Array<ChatResponse> = [];

  constructor(
    private chatService: ChatService,
    private keycloakService: KeycloakService
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
}
