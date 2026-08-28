import { Component, input } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterLink } from '@angular/router';
import { faArrowLeft, faPenToSquare, faUsers } from '@fortawesome/free-solid-svg-icons';
import { IConversation } from '../../../../interfaces/conversation.interface';

@Component({
  selector: 'app-header-conversation',
  imports: [
    FaIconComponent,
    RouterLink
  ],
  templateUrl: './header-conversation.html',
  styleUrl: './header-conversation.css',
})
export class HeaderConversation {

  conversation =  input.required<IConversation | null>();
  isGroup =  input.required<boolean>();

  protected readonly faArrowLeft = faArrowLeft;
  protected readonly faPenToSquare = faPenToSquare;
  protected readonly faUsers = faUsers;
}
