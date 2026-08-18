import {Component, effect, inject, input, signal} from '@angular/core';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
import {switchMap} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {WebSocketService} from '../../../../core/websocket/services/websocket-service';
import {MessageService} from '../../services/message/message-service';
import {AuthService} from '../../../auth/service/auth-service';
import {DatePipe} from '@angular/common';
import { scan } from 'rxjs';
import {IMessage} from '@stomp/stompjs';

@Component({
  selector: 'app-conversation-page',
  imports: [FormsModule, DatePipe],
  templateUrl: './conversation-page.html',
  styleUrl: './conversation-page.css',
})
export class ConversationPage {
  private wsService = inject(WebSocketService);
  private messageService = inject(MessageService);
  private authService = inject(AuthService);


  conversationId = input.required<string>();
  draft = signal('');

  messages = toSignal(
    toObservable(this.conversationId).pipe(
      switchMap((id) =>
        this.messageService.subscribeToConversation(id).pipe(
          scan((acc: IMessage[], message: IMessage) => [...acc, message], [] as IMessage[])
        )
      )
    ),
    { initialValue: [] as IMessage[] }
  );

  constructor() {
    effect((onCleanup) => {
      const id = this.conversationId();
      this.wsService.joinConversation(id);
      onCleanup(() => this.wsService.leaveConversation(id));
    });
  }

  onSend(): void {
    const content = this.draft().trim();
    if (!content) return;

    this.messageService.sendMessage(this.conversationId(), content);
    this.draft.set('');
  }

  isMine(message: any): boolean {
    return message.senderPublicId === this.authService.currentUser()?.publicId;
  }

  onEnter(event: Event): void {
    const keyboardEvent = event as KeyboardEvent;
    if (!keyboardEvent.shiftKey) {
      keyboardEvent.preventDefault();
      this.onSend();
    }
  }
}
