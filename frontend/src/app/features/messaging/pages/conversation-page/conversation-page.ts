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
import { ActivatedRoute, Router } from '@angular/router';
import { ConversationService } from '../../services/conversation/conversation-service';
import { IConversation } from '../../interfaces/conversation.interface';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPenToSquare, faTimes, faUsers } from '@fortawesome/free-solid-svg-icons';
import { IUser } from '../../../../shared/interfaces/user.interface';
import { UserService } from '../../../../core/services/user/user-service';
import {NotificationService} from '../../../notifications/services/notification/notification-service';

@Component({
  selector: 'app-conversation-page',
  imports: [FormsModule, DatePipe, FaIconComponent],
  templateUrl: './conversation-page.html',
  styleUrl: './conversation-page.css',
})
export class ConversationPage {
  private router = inject(Router);
  private wsService = inject(WebSocketService);
  private messageService = inject(MessageService);
  private notificationService = inject(NotificationService);
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private conversationService = inject(ConversationService);
  private typingTrigger$ = new Subject<void>();
  private typingTimeout?: ReturnType<typeof setTimeout>;

  isGroup = computed(() => (this.conversation()?.users?.length ?? 0) > 2);

  conversationId = input.required<string>();
  draft = signal('');
  typingUser = signal<string | null>(null);

  conversationRefreshTrigger = signal(0);

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

  messages = toSignal(
    toObservable(this.conversationId).pipe(
      switchMap((id) => {
        return this.messageService.getMessageHistory(id).pipe(
          catchError((err) => {
            if (err.status === 403 || err.status === 404) {
              this.router.navigate(['/messageries']);
            }
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
    this.typingTrigger$.pipe(debounceTime(300)).subscribe(() => {
      this.typing();
    });

    effect(() => {
      const id = this.conversationId();
      if (!id) return;
      this.notificationService.markConversationAsRead(id).subscribe();
    });

    // Scroll auto vers le bas à chaque nouveau message
    effect(() => {
      this.messages();
      setTimeout(() => this.scrollToBottom(), 50);
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
        this.typingTimeout = setTimeout(() => this.typingUser.set(null), 2000);
      });

      onCleanup(() => {
        sub.unsubscribe();
        clearTimeout(this.typingTimeout);
      });
    });

    effect(() => {
      const conv = this.conversation();
      if (conv) {
        this.editName.set(conv.name);
      }
    });
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

  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  private scrollToBottom(): void {
    if (this.messagesContainer) {
      this.messagesContainer.nativeElement.scrollTop =
        this.messagesContainer.nativeElement.scrollHeight;
    }
  }

  editTab = signal<'rename' | 'participants'>('rename');
  editName = signal('');

  selectedNewUsers = signal<IUser[]>([]);
  addUserQuery = signal('');
  debouncedAddUserQuery = debounced(this.addUserQuery, 300);

  addUserSearchResource = resource({
    params: () => this.debouncedAddUserQuery.value(),
    loader: async ({ params: q }) => {
      if (!q || !q.trim()) return [];
      const res = await firstValueFrom(this.userService.searchUser(q, { page: 0, size: 20 }));
      return res.data.content;
    },
  });

  addUserResults = computed(() => {
    const results = this.addUserSearchResource.value() ?? [];
    const existingIds = new Set(this.conversation()?.users?.map((u) => u.publicId) ?? []);
    const selectedIds = new Set(this.selectedNewUsers().map((u) => u.publicId));
    const currentUserId = this.authService.currentUser()?.publicId;
    return results.filter(
      (u) =>
        !existingIds.has(u.publicId) &&
        !selectedIds.has(u.publicId) &&
        u.publicId !== currentUserId,
    );
  });

  selectNewUser(user: IUser): void {
    this.selectedNewUsers.update((users) => [...users, user]);
    this.addUserQuery.set('');
  }

  removeNewUser(user: IUser): void {
    this.selectedNewUsers.update((users) => users.filter((u) => u.publicId !== user.publicId));
  }

  onRenameConversation(): void {
    const conversationId = this.conversationId();
    const name = this.editName().trim();
    if (!name) return;

    this.conversationService.updateConversation(conversationId, { name, usersIds: [] }).subscribe({
      next: () => {
        (document.getElementById('edit_conversation_modal') as HTMLDialogElement)?.close();
      },
      error: (err) => err,
    });
  }

  onAddParticipants(): void {
    const conversationId = this.conversationId();
    const usersIds = this.selectedNewUsers().map((u) => u.publicId);

    this.conversationService.updateConversation(conversationId, { name: '', usersIds }).subscribe({
      next: () => {
        this.selectedNewUsers.set([]);
        this.addUserQuery.set('');
        (document.getElementById('edit_conversation_modal') as HTMLDialogElement)?.close();
      },
      error: (err) => err,
    });
  }

  protected readonly faPenToSquare = faPenToSquare;
  protected readonly faArrowLeft = faArrowLeft;
  protected readonly faTimes = faTimes;
  protected readonly faUsers = faUsers;
}
