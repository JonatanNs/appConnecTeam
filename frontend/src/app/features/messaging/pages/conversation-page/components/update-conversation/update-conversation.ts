import {
  Component,
  computed,
  debounced,
  effect,
  inject,
  input,
  resource,
  signal,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { ConversationService } from '../../../../services/conversation/conversation-service';
import { UserService } from '../../../../../../core/services/user/user-service';
import { AuthService } from '../../../../../auth/service/auth-service';
import { IConversation } from '../../../../interfaces/conversation.interface';
import { IUser } from '../../../../../../shared/interfaces/user.interface';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faTimes, faUsers } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-update-conversation',
  imports: [FormsModule, FaIconComponent],
  templateUrl: './update-conversation.html',
  styleUrl: './update-conversation.css',
})
export class UpdateConversation {
  private conversationService = inject(ConversationService);
  private userService = inject(UserService);
  private authService = inject(AuthService);

  conversationId = input.required<string>();
  conversation = input.required<IConversation | null>();

  isGroup = computed(() => (this.conversation()?.users?.length ?? 0) > 2);

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

  constructor() {
    effect(() => {
      const conv = this.conversation();
      if (conv) {
        this.editName.set(conv.name);
      }
    });
  }

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

  protected readonly faTimes = faTimes;
  protected readonly faUsers = faUsers;
}
