import { AfterViewChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Chat } from '../../model/chat';
import { Message } from '../../model/message';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ChatService } from '../../service/chat.service';
import { MessageService } from '../../service/message.service';
import { NewMessage } from '../../model/new-message';
import { AuthService } from '../../service/auth.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-chat-list',
  standalone: false,
  templateUrl: './chat-list.component.html',
  styleUrl: './chat-list.component.css'
})
export class ChatListComponent implements OnInit, AfterViewChecked  {
  chats: Chat[] = [];
  activeChatId?: string;
  activeChatName = '';
  messages: Message[] = [];
  currentUserId: string = '';
  form!: FormGroup;
  attachmentB64: string = '';
  sending = false;

   @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  constructor(
    private chatService: ChatService,
    private messageService: MessageService,
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    this.currentUserId = this.authService.getUserId()!;
    this.form = this.fb.group({ content: [''] });

    this.route.queryParamMap.subscribe(params => {
      const receiverId = params.get('receiver');
      this.loadChats(receiverId ?? undefined);
    });
  }

    ngAfterViewChecked(): void {
    this.scrollToBottom();
  }
  
   private scrollToBottom(): void {
    if (this.messagesContainer) {
      try {
        this.messagesContainer.nativeElement.scrollTop =
          this.messagesContainer.nativeElement.scrollHeight;
      } catch (err) {
        console.error('Scroll error:', err);
      }
    }
  }

  loadChats(receiverId?: string): void {
    this.chatService.getUserChats().subscribe(chats => {
      this.chats = chats;
      if (receiverId) {
        this.openOrCreateChat(receiverId);
      } else if (!this.activeChatId && chats.length) {
        this.openChat(chats[0].id);
      }
    });
  }

  openChat(chatId: string): void {
    this.activeChatId = chatId;
    const c = this.chats.find(x => x.id === chatId);
    this.activeChatName = c?.receiverName || 'Rozmowa';
    this.fetchMessages();
  }
  openOrCreateChat(receiverId: string): void {
    let chat = this.chats.find(c => c.receiverId === receiverId);

    if (chat) {
      this.openChat(chat.id);
    } else {
      this.chatService.startChat(receiverId).subscribe(newChat => {
        this.chats = [...this.chats, newChat];
        this.openChat(newChat.id);
        this.chatService.getUserChats().subscribe(allChats => {
          this.chats = allChats;
        });
      });
    }
  }

  fetchMessages(): void {
    if (!this.activeChatId) return;
    this.messageService.getMessages(this.activeChatId).subscribe(list => {
      this.messages = list ?? [];
    });
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
        this.fetchMessages();
      },
      error: () => (this.sending = false)
    });
  }
}
