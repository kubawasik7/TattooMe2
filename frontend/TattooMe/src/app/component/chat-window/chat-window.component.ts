import { Component, OnInit } from '@angular/core';
import { NewMessage } from '../../model/new-message';
import { MessageService } from '../../service/message.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Message } from '../../model/message';

@Component({
  selector: 'app-chat-window',
  standalone: false,
  templateUrl: './chat-window.component.html',
  styleUrl: './chat-window.component.css'
})
export class ChatWindowComponent implements OnInit {
  chatId!: string;
  messages: Message[] = [];
  messageForm!: FormGroup;
  attachmentFile?: File;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.chatId = this.route.snapshot.paramMap.get('id')!;
    this.messageForm = this.fb.group({
      content: ['']
    });
    this.loadMessages();
  }

  loadMessages(): void {
    this.messageService.getMessages(this.chatId).subscribe({
      next: data => this.messages = data
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.attachmentFile = input.files[0];
    }
  }

  sendMessage(): void {
    const dto: NewMessage = {
      chatId: this.chatId,
      content: this.messageForm.get('content')?.value || '',
      base64Attachment: ''
    };

    if (this.attachmentFile) {
      const reader = new FileReader();
      reader.onload = () => {
        dto.base64Attachment = (reader.result as string).split(',')[1];
        this.send(dto);
      };
      reader.readAsDataURL(this.attachmentFile);
    } else {
      this.send(dto);
    }
  }

  private send(newMessage: NewMessage): void {
    this.messageService.sendMessage(newMessage).subscribe(() => {
      this.messageForm.reset();
      this.attachmentFile = undefined;
      this.loadMessages();
    });
  }
}
