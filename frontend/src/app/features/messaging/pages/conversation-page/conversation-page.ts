import {Component, computed, debounced, effect, ElementRef, inject, input, resource, signal, ViewChild,} from '@angular/core';
import { takeUntilDestroyed, toObservable, toSignal } from '@angular/core/rxjs-interop';
import {catchError, combineLatest, debounceTime, firstValueFrom, map, merge, of, Subject, switchMap,} from 'rxjs';
import { FormsModule } from '@angular/forms';
import { WebSocketService } from '../../../../core/websocket/services/websocket-service';
import { MessageService } from '../../services/message/message-service';
import { AuthService } from '../../../auth/service/auth-service';
import { DatePipe } from '@angular/common';
import { scan } from 'rxjs';
import { IMessageSend } from '../../interfaces/message.interface';
import { ConversationService } from '../../services/conversation/conversation-service';
import { IConversation } from '../../interfaces/conversation.interface';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {
  faArrowDown,
  faArrowLeft,
  faComments,
  faPaperPlane,
  faPenToSquare,
  faTimes,
  faUsers,
} from '@fortawesome/free-solid-svg-icons';
import { IUser } from '../../../../shared/interfaces/user.interface';
import { UserService } from '../../../../core/services/user/user-service';
import {NotificationService} from '../../../notifications/services/notification/notification-service';
import { Router, RouterLink } from '@angular/router';
import { UpdateConversation } from './components/update-conversation/update-conversation';
import { SidePanel } from './components/side-panel/side-panel';
import { HeaderConversation } from './components/header-conversation/header-conversation';

@Component({
  selector: 'app-conversation-page',
  imports: [
    FormsModule,
    DatePipe,
    FaIconComponent,
    UpdateConversation,
    SidePanel,
    HeaderConversation,
  ],
  templateUrl: './conversation-page.html',
  styleUrl: './conversation-page.css',
})
export class ConversationPage {
  private router = inject(Router);
  private wsService = inject(WebSocketService);
  private messageService = inject(MessageService);
  private notificationService = inject(NotificationService);
  private authService = inject(AuthService);
  private conversationService = inject(ConversationService);
  private typingTrigger$ = new Subject<void>();
  private typingTimeout?: ReturnType<typeof setTimeout>;
  private isNearBottom = true; // tracké via le scroll

  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  private hasMoreHistory = signal(true);
  private isLoadingMore = signal(false);

  draft = signal('');
  messages = signal<IMessageSend[]>([]);
  newMessageCount = signal(0);
  typingUser = signal<string | null>(null);
  conversationRefreshTrigger = signal(0);

  conversationId = input.required<string>();

  isGroup = computed(() => (this.conversation()?.users?.length ?? 0) > 2);

  conversation = toSignal(
    combineLatest([
      toObservable(this.conversationId),
      toObservable(this.conversationRefreshTrigger),
    ]).pipe(
      switchMap(([id]) =>
        this.conversationService.getConversation(id).pipe(
          map((response) => response.data),
          catchError((err) => {
            if (err.status === 403 || err.status === 404) {
              this.router.navigate(['/messageries']);
            }
            return of(null);
          }),
        ),
      ),
    ),
    { initialValue: null as IConversation | null },
  );

  constructor() {
    this.conversationService.onConversationUpdated.subscribe(() => {
      this.conversationRefreshTrigger.update((n) => n + 1);
    });

    this.conversationService.onConversationLeftOrDeleted
      .pipe(takeUntilDestroyed())
      .subscribe((deletedId) => {
        if (deletedId === this.conversationId()) {
          this.router.navigate(['/messageries']);
        }
      });

    // Envoi de l'événement typing (debounce pour ne pas spammer le websocket)
    this.typingTrigger$.pipe(debounceTime(500)).subscribe(() => {
      this.typing();
    });

    effect(() => {
      const id = this.conversationId();
      if (!id) return;
      this.notificationService.markConversationAsRead(id).subscribe();
    });

    // Join / leave de la conversation
    effect((onCleanup) => {
      const id = this.conversationId();
      if (!id) return;
      this.wsService.joinConversation(id);
      onCleanup(() => this.wsService.leaveConversation(id));
    });

    // Réception des événements "typing" des autres utilisateurs
    effect((onCleanup) => {
      const id = this.conversationId();
      if (!id) return;

      const sub = this.wsService.subscribeToTyping(id).subscribe((event: any) => {
        if (event.userPublicId === this.authService.currentUser()?.publicId) return;
        this.typingUser.set(event.userName ?? "Quelqu'un");

        clearTimeout(this.typingTimeout);
        this.typingTimeout = setTimeout(() => this.typingUser.set(null), 1000);
      });

      onCleanup(() => {
        sub.unsubscribe();
        clearTimeout(this.typingTimeout);
      });
    });

    // Chargement initial + flux temps réel, à chaque changement de conversationId
    effect((onCleanup) => {
      const id = this.conversationId();
      if (!id) return;

      this.messages.set([]);
      this.hasMoreHistory.set(true);
      this.newMessageCount.set(0);

      const sub = this.messageService
        .getMessageHistory(id)
        .pipe(
          catchError((err) => {
            if (err.status === 403 || err.status === 404) {
              this.router.navigate(['/messageries']);
            }
            return of([] as IMessageSend[]);
          }),
          switchMap((history) => {
            this.messages.set(history);
            setTimeout(() => this.scrollToBottom(), 50); // scroll initial uniquement
            return this.messageService.subscribeToConversation(id);
          }),
        )
        .subscribe((message) => {
          this.messages.update((list) => [...list, message]);

          if (this.isNearBottom) {
            // L'utilisateur est déjà en bas → on scrolle avec lui
            setTimeout(() => this.scrollToBottom(), 50);
          } else {
            // L'utilisateur lit plus haut → juste incrémenter le compteur
            this.newMessageCount.update((n) => n + 1);
          }
        });

      onCleanup(() => sub.unsubscribe());
    });
  }

  loadMoreMessages(): void {
    const id = this.conversationId();
    const currentMessages = this.messages();
    if (!id || this.isLoadingMore() || !this.hasMoreHistory() || currentMessages.length === 0)
      return;

    const oldestMessage = currentMessages[0];
    this.isLoadingMore.set(true);

    this.messageService.getMessageHistory(id, oldestMessage.createdAt, 20).subscribe({
      next: (olderMessages) => {
        if (olderMessages.length === 0) {
          this.hasMoreHistory.set(false);
        } else {
          this.messages.update((list) => [...olderMessages, ...list]);
        }
        this.isLoadingMore.set(false);
      },
      error: () => this.isLoadingMore.set(false),
    });
  }

  onScroll(event: Event): void {
    const el = event.target as HTMLElement;

    if (el.scrollTop < 100) {
      this.loadMoreMessages();
    }

    const distanceFromBottom = el.scrollHeight - el.scrollTop - el.clientHeight;
    this.isNearBottom = distanceFromBottom < 100;

    if (this.isNearBottom) {
      this.newMessageCount.set(0); // il vient de rejoindre le bas, on efface le badge
    }
  }

  scrollToBottomManually(): void {
    this.scrollToBottom();
    this.newMessageCount.set(0);
  }

  onTyping(): void {
    this.typingTrigger$.next();
  }

  typing(): void {
    this.wsService.sendTyping(this.conversationId(), {
      conversationId: this.conversationId(),
      userId: this.authService.currentUser()?.publicId ?? '',
      userName: this.authService.currentUser()?.firstname ?? '',
      isTyping: true,
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

  private scrollToBottom(): void {
    if (this.messagesContainer) {
      this.messagesContainer.nativeElement.scrollTop =
        this.messagesContainer.nativeElement.scrollHeight;
    }
  }

  protected readonly faArrowLeft = faArrowLeft;
  protected readonly faPaperPlane = faPaperPlane;
  protected readonly faArrowDown = faArrowDown;
  protected readonly faComments = faComments;
}
