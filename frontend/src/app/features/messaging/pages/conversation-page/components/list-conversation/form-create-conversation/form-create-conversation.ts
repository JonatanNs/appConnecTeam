import {Component, computed, debounced, inject, output, resource, signal} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faTimes} from '@fortawesome/free-solid-svg-icons';
import {UserService} from '../../../../../../../core/services/user/user-service';
import {debounce} from '@angular/forms/signals';
import {firstValueFrom, map} from 'rxjs';
import {toSignal} from '@angular/core/rxjs-interop';
import {FormsModule} from '@angular/forms';
import {IUser} from '../../../../../../../shared/interfaces/user.interface';
import {IPage} from '../../../../../../../shared/interfaces/pageable/page.interface';
import {ConversationService} from '../../../../../services/conversation/conversation-service';

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
      console.log('debounced query:', q);
      return q;
    },
    loader: async ({ params: q }) => {
      console.log('loader called with:', q);
      if (!q || !q.trim()) return [];
      try {
        const res = await firstValueFrom(this.userService.searchUser(q, {page: 0, size: 20}));
        console.log('search results:', res.data.content);
        return res.data.content;
      } catch (err) {
        console.error('search error:', err);
        throw err;
      }
    }
  });

  searchResults = computed(() => {
    const results = this.searchResource.value() ?? [];
    const selectedIds = new Set(this.selectedUsers().map((u) => u.publicId));
    return results.filter((u) => !selectedIds.has(u.publicId));
  });

  conversationName = signal('');

  onCreateConversation(): void {
    const usersIds = this.selectedUsers().map((u) => u.publicId);

    this.conversationService.createConversation({
      name: this.selectedUsers().length >= 2 ? this.conversationName() : 'void',
      usersIds,
    }).subscribe({
      next: () => {
        this.conversationName.set('');
        this.selectedUsers.set([]);
        (document.getElementById('my_modal_5') as HTMLDialogElement)?.close();
      },
      error: (err) => console.error('Erreur création conversation', err),
    });
  }
}
