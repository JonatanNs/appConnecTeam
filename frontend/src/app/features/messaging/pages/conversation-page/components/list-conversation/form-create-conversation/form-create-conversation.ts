import {Component, computed, debounced, inject, resource, signal} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {UserService} from '../../../../../../../core/services/user/user-service';
import {firstValueFrom} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {IUser} from '../../../../../../../shared/interfaces/user.interface';
import {ConversationService} from '../../../../../services/conversation/conversation-service';
import {AuthService} from '../../../../../../auth/service/auth-service';

@Component({
  selector: 'app-form-create-conversation',
  imports: [
    FaIconComponent,
    FormsModule
  ],
  templateUrl: './form-create-conversation.html',
  styleUrl: './form-create-conversation.css',
})
export class FormCreateConversation {
  protected readonly faTimes = faTimes;
  private userService = inject(UserService);
  private conversationService = inject(ConversationService);
  private authService = inject(AuthService);

  selectedUsers = signal<IUser[]>([]);
  removeUser(user: IUser): void {
    this.selectedUsers.update((users) => users.filter((u) => u.publicId !== user.publicId));
  }

  selectUser(user: IUser): void {
    this.selectedUsers.update((users) => [...users, user]);
    this.query.set('');
  }

  query = signal('');
  debouncedQuery = debounced(this.query, 300);

  searchResource = resource({
    params: () => {
      const q = this.debouncedQuery.value();
      return q;
    },
    loader: async ({ params: q }) => {
      if (!q || !q.trim()) return [];
      try {
        const res = await firstValueFrom(this.userService.searchUser(q, {page: 0, size: 20}));
        return res.data.content;
      } catch (err) {
        throw err;
      }
    }
  });

  searchResults = computed(() => {
    const results = this.searchResource.value() ?? [];
    const currentUserId = this.authService.currentUser()?.publicId;
    const selectedIds = new Set(this.selectedUsers().map((u) => u.publicId));
    return results.filter((u) => !selectedIds.has(u.publicId) && u.publicId !== currentUserId);
  });


  conversationName = signal('');
  onCreateConversation(): void {
    const usersIds = this.selectedUsers().map((u) => u.publicId);

    this.conversationService.createConversation({
      name: this.selectedUsers().length >= 2 ? this.conversationName() : '',
      usersIds,
    }).subscribe({
      next: () => {
        this.conversationName.set('');
        this.selectedUsers.set([]);
        (document.getElementById('my_modal_5') as HTMLDialogElement)?.close();
      },
      error: (err) =>  err,
    });
  }
}
