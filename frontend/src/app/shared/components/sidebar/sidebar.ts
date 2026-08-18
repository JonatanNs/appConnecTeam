import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {
  faCalendarDays,
  faUsers,
  faFolderOpen,
  faListCheck,
  faCircleQuestion,
} from '@fortawesome/free-solid-svg-icons';
import { ListSidebar } from './components/list-sidebar/list-sidebar';
import {
  ListConversation
} from '../../../features/messaging/pages/conversation-page/components/list-conversation/list-conversation';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, FaIconComponent, ListSidebar, ListConversation],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  protected readonly faCircleQuestion = faCircleQuestion;

  // Indique si la sidebar est réduite
  isClosed = signal<boolean>(false);

  toggleSidebar(): void {
    this.isClosed.update((state) => !state);
  }
}
