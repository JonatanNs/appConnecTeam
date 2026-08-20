import { Component, effect, ElementRef, inject, input, signal, ViewChild } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, map, merge, of, switchMap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { WebSocketService } from '../../../../core/websocket/services/websocket-service';
import { MessageService } from '../../services/message/message-service';
import { AuthService } from '../../../auth/service/auth-service';
import { DatePipe } from '@angular/common';
import { scan } from 'rxjs';
import { IMessageSend } from '../../interfaces/message.interface';
import { ActivatedRoute } from '@angular/router';
import { ConversationService } from '../../services/conversation/conversation-service';
import { IConversation } from '../../interfaces/conversation.interface';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {
  faArrowLeft,
  faCircleInfo,
  faPenToSquare,
  faVideo,
} from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-conversation-page',
  imports: [FormsModule, DatePipe, FaIconComponent],
  templateUrl: './conversation-page.html',
  styleUrl: './conversation-page.css',
})
export class ConversationPage {
  private wsService = inject(WebSocketService);
  private messageService = inject(MessageService);
  private authService = inject(AuthService);
  private conversationService = inject(ConversationService);

  conversationId = input.required<string>();
  draft = signal('');

  conversation = toSignal(
    toObservable(this.conversationId).pipe(
      switchMap((id) =>
        this.conversationService.getConversation(id).pipe(
          map((response) => response.data),
          catchError((err) => {
            console.error('Erreur chargement conversation', err);
            return of(null as IConversation | null);
          }),
        ),
      ),
    ),
    { initialValue: null as IConversation | null },
  );

  messages = toSignal(
    toObservable(this.conversationId).pipe(
      switchMap((id) => {
        return this.messageService.getMessageHistory(id).pipe(
          catchError((err) => {
            console.error('Erreur chargement historique', err);
            return of([] as IMessageSend[]);
          }),
          switchMap((history) =>
            merge(
              of(history),
              this.messageService
                .subscribeToConversation(id)
                .pipe(
                  scan((acc: IMessageSend[], message: IMessageSend) => [...acc, message], history),
                ),
            ).pipe(
              catchError((err) => {
                console.error('Erreur flux WebSocket', err);
                return of(history);
              }),
            ),
          ),
        );
      }),
    ),
    { initialValue: [] as IMessageSend[] },
  );

  constructor() {
    const route = inject(ActivatedRoute);
    route.paramMap.subscribe((params) => {
      console.log('paramMap direct:', params.get('conversationId'));
    });

    effect(() => {
      this.messages(); // souscription aux changements
      setTimeout(() => this.scrollToBottom(), 50);
    });

    effect((onCleanup) => {
      const id = this.conversationId();
      console.log('effect join, id =', id);
      if (!id) return;
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

  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  private scrollToBottom(): void {
    if (this.messagesContainer) {
      this.messagesContainer.nativeElement.scrollTop =
        this.messagesContainer.nativeElement.scrollHeight;
    }
  }

  protected readonly faCircleInfo = faCircleInfo;
  protected readonly faVideo = faVideo;
  protected readonly faPenToSquare = faPenToSquare;
  protected readonly faArrowLeft = faArrowLeft;
}

