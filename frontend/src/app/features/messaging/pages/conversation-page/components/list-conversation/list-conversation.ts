import { Component, inject, computed, signal } from '@angular/core';
import { toSignal, toObservable } from '@angular/core/rxjs-interop';
import { switchMap, of, map, combineLatest } from 'rxjs';
import { ConversationService } from '../../../../services/conversation/conversation-service';
import { AuthService } from '../../../../../auth/service/auth-service';
import { IConversation } from '../../../../interfaces/conversation.interface';
import { IMessageSend } from '../../../../interfaces/message.interface';
import { DatePipe, JsonPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { WebSocketService } from '../../../../../../core/websocket/services/websocket-service';
import {
  faPlus,
  faEllipsisVertical,
  faCheckDouble,
  faGear,
  faMagnifyingGlass,
  faThumbtack,
  faCheck,
  faTrash,
  faComments,
  faCommentSlash,
  faArrowRightFromBracket,
} from '@fortawesome/free-solid-svg-icons';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-list-conversation',
  imports: [DatePipe, RouterLink, JsonPipe, FaIconComponent],
  templateUrl: './list-conversation.html',
  styleUrl: './list-conversation.css',
})
export class ListConversation {
  private conversationService = inject(ConversationService);
  private authService = inject(AuthService);
  private userId = computed(() => this.authService.currentUser()?.publicId);
  private defaultPageable = { size: 20, page: 0 };

  selectedConversationId = signal<string | null>(null);

  refreshTrigger = signal(0);

  conversations = toSignal(
    combineLatest([toObservable(this.userId), toObservable(this.refreshTrigger)]).pipe(
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

  lastMessage(conv: IConversation): IMessageSend | null {
    if (!conv.messages || conv.messages.length === 0) return null;
    return conv.messages[conv.messages.length - 1];
  }

  selectConversation(publicId: string): void {
    this.selectedConversationId.set(publicId);
  }

  protected readonly faCheck = faCheck;
  protected readonly faTrash = faTrash;
  protected readonly faThumbtack = faThumbtack;
  protected readonly faEllipsisVertical = faEllipsisVertical;
  protected readonly faCommentSlash = faCommentSlash;
  protected readonly faMagnifyingGlass = faMagnifyingGlass;
  protected readonly faPlus = faPlus;
  protected readonly faGear = faGear;
  protected readonly faCheckDouble = faCheckDouble;
  protected readonly faComments = faComments;
  protected readonly faArrowRightFromBracket = faArrowRightFromBracket;
}
