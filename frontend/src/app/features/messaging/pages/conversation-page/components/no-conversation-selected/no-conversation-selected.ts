import { Component } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { ListConversation } from '../list-conversation/list-conversation';

@Component({
  selector: 'app-no-conversation-selected',
  imports: [FaIconComponent, ListConversation],
  templateUrl: './no-conversation-selected.html',
  styleUrl: './no-conversation-selected.css',
})
export class NoConversationSelected {
  protected readonly faComments = faComments;

}
