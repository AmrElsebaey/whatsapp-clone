import {Component, input, InputSignal, output} from '@angular/core';
import {ChatResponse, UserResponse} from '../../services/models';
import {DatePipe} from '@angular/common';
import {UserService} from '../../services/services/user.service';

@Component({
  selector: 'app-chat-list',
  imports: [
    DatePipe
  ],
  templateUrl: './chat-list.html',
  styleUrl: './chat-list.scss'
})
export class ChatList {

  chats: InputSignal<ChatResponse[]> = input<ChatResponse[]>([]);
  searchNewContact = false;
  contacts: Array<UserResponse> = [];
  chatSelected = output<ChatResponse>();

  constructor(
    private userService: UserService
  ) {
  }

  searchContact() {
    this.userService.getAllUsers()
      .subscribe( {
        next: (users) => {
          this.contacts = users;
          this.searchNewContact = true;
        }
      })
  }

  chatClicked(chat: ChatResponse) {
    this.chatSelected.emit(chat);
  }

  wrapMessage(lastMessage: string | undefined):string {
    if (lastMessage && lastMessage.length <= 20) {
      return lastMessage;
    }
    return lastMessage?.substring(0, 17) + '...';
  }

  selectContact(contact: UserResponse) {
  }

  protected readonly console = console;
}
