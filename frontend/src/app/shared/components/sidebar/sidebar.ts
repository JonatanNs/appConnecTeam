import { Component, signal } from '@angular/core';
import {RouterLink, RouterLinkActive } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {faCircleQuestion} from '@fortawesome/free-solid-svg-icons';
import { ListSidebar } from './components/list-sidebar/list-sidebar';


@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, FaIconComponent, ListSidebar],
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
