import { Component, OnInit } from '@angular/core';
import { ChatResponse } from '../../services/models';
import { ChatService } from '../../services/services';
import { ChatList } from '../../components/chat-list/chat-list';

@Component({
  selector: 'app-main',
  imports: [ChatList],
  templateUrl: './main.html',
  styleUrl: './main.scss'
})
export class Main implements OnInit{

  chats: Array<ChatResponse> = [];

  constructor(
    private chatService: ChatService
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
}
