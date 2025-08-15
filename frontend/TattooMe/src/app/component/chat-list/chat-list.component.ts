import { Component, OnInit } from '@angular/core';
import { Chat } from '../../model/chat';
import { Message } from '../../model/message';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ChatService } from '../../service/chat.service';
import { MessageService } from '../../service/message.service';
import { NewMessage } from '../../model/new-message';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-chat-list',
  standalone: false,
  templateUrl: './chat-list.component.html',
  styleUrl: './chat-list.component.css'
})
export class ChatListComponent implements OnInit {
  chats: Chat[] = [];
  activeChatId?: string;
  activeChatName = '';
  messages: Message[] = [];

  currentUserId: string = '';

  form!: FormGroup;
  attachmentB64: string = '';
  sending = false;

  constructor(
    private chatService: ChatService,
    private messageService: MessageService,
    private fb: FormBuilder,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.currentUserId = this.authService.getUserId()!;
    this.form = this.fb.group({ content: [''] });
    this.loadChats();
  }

  loadChats(): void {
    this.chatService.getUserChats().subscribe(chats => {
      this.chats = chats;
      if (!this.activeChatId && chats.length) {
        this.openChat(chats[0].senderId);
      }
    });
  }

  openChat(chatId: string): void {
    this.activeChatId = chatId;
    const c = this.chats.find(x => x.senderId === chatId);
    this.activeChatName = c?.receiverName || 'Rozmowa';
  }
  
  send(): void {
    if (!this.activeChatId || this.sending) return;
    const newMessage: NewMessage = {
      chatId: this.activeChatId,
      content: (this.form.value.content || '').trim(),
      base64Attachment: this.attachmentB64 || ''
    };
    if (!newMessage.content && !newMessage.base64Attachment) return;

    this.sending = true;
    this.messageService.sendMessage(newMessage).subscribe({
      next: () => {
        this.form.reset();
        this.attachmentB64 = '';
        this.sending = false;
      },
      error: () => this.sending = false
    });
  }
}
