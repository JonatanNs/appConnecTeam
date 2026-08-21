import { Component, inject, computed, signal, debounced, resource } from '@angular/core';
import { toSignal, toObservable } from '@angular/core/rxjs-interop';
import { switchMap, of, map, combineLatest, firstValueFrom } from 'rxjs';
import { ConversationService } from '../../../../services/conversation/conversation-service';
import { AuthService } from '../../../../../auth/service/auth-service';
import { IConversation } from '../../../../interfaces/conversation.interface';
import { IMessageSend } from '../../../../interfaces/message.interface';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import {
  faPlus,
  faEllipsisVertical,
  faMagnifyingGlass,
  faThumbtack,
  faTrash,
  faComments,
  faCommentSlash,
  faArrowRightFromBracket,
} from '@fortawesome/free-solid-svg-icons';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { FormCreateConversation } from './form-create-conversation/form-create-conversation';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-list-conversation',
  imports: [
    DatePipe,
    RouterLink,
    FaIconComponent,
    FormCreateConversation,
    ReactiveFormsModule,
    FormsModule,
  ],
  templateUrl: './list-conversation.html',
  styleUrl: './list-conversation.css',
})
export class ListConversation {

  private conversationService = inject(ConversationService);
  private router = inject(Router);
  private authService = inject(AuthService);
  protected currentUserId = computed(() => this.authService.currentUser()?.publicId);
  private defaultPageable = { size: 20, page: 0 };

  selectedConversationId = signal<string | null>(null);

  refreshTrigger = signal(0);

  conversations = toSignal(
    combineLatest([toObservable(this.currentUserId), toObservable(this.refreshTrigger)]).pipe(
      switchMap(([publicId]) =>
        publicId
          ? this.conversationService
              .getConversationByUserId(publicId, this.defaultPageable)
              .pipe(map((response) => response.data.content))
          : of([] as IConversation[]),
      ),
    ),
    { initialValue: [] as IConversation[] },
  );

  displayedConversations = computed(() => {
    return this.query().trim() ? this.searchResults() : this.conversations();
  });

  query = signal('');
  debouncedQuery = debounced(this.query, 300);

  constructor() {
    this.conversationService.onConversationCreated.subscribe(() => this.refresh());
    this.conversationService.onConversationCreated.subscribe(() => this.refresh());
    this.conversationService.onConversationUpdated.subscribe(() => this.refresh());
  }

  searchResource = resource({
    params: () => ({
      query: this.debouncedQuery.value(),
    }),

    loader: async ({ params }) => {
      const q = params.query?.trim();

      if (!q) {
        return [];
      }

      return firstValueFrom(
        this.conversationService.searchConversation(q, {
          page: 0,
          size: 20,
        }),
      ).then((res) => res.data.content);
    },
  });

  searchResults = computed(() => {
    return this.searchResource.value() ?? [];
  });

  refresh(): void {
    this.refreshTrigger.update((n) => n + 1);
  }

  lastMessage(conv: IConversation): IMessageSend | null {
    if (!conv.messages || conv.messages.length === 0) return null;
    return conv.messages[conv.messages.length - 1];
  }

  selectConversation(publicId: string): void {
    this.selectedConversationId.set(publicId);
  }

  onLeave(conversationId: string, event: Event): void {
    event.stopPropagation();
    this.conversationService.leaveConversation(conversationId).subscribe({
      next: () => {
        this.refresh();
      },
      error: (err) =>  err.error.message,
    });
  }

  onDelete(conversationId: string, event: Event): void {
    event.stopPropagation();
    this.conversationService.deleteConversation(conversationId).subscribe({
      next: () => {
        this.refresh();
      },
      error: (err) => err.error.message,
    });
  }

  protected readonly faTrash = faTrash;
  protected readonly faThumbtack = faThumbtack;
  protected readonly faEllipsisVertical = faEllipsisVertical;
  protected readonly faCommentSlash = faCommentSlash;
  protected readonly faMagnifyingGlass = faMagnifyingGlass;
  protected readonly faPlus = faPlus;
  protected readonly faComments = faComments;
  protected readonly faArrowRightFromBracket = faArrowRightFromBracket;
}
