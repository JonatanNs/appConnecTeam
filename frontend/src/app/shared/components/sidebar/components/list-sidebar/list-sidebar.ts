import { Component, input, signal } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterLink, RouterLinkActive } from '@angular/router';
import {
  faCalendarDays,
  faFolderOpen,
  faListCheck,
  faUsers,
} from '@fortawesome/free-solid-svg-icons';
import { IApiResponse } from '../../../../interfaces/api-response.interface';
import { IPage } from '../../../../interfaces/pageable/page.interface';
import { IUser } from '../../../../interfaces/user.interface';

@Component({
  selector: 'app-list-sidebar',
  imports: [FaIconComponent, RouterLink, RouterLinkActive],
  templateUrl: './list-sidebar.html',
  styleUrl: './list-sidebar.css',
})
export class ListSidebar {
  protected readonly faListCheck = faListCheck;
  protected readonly faCalendarDays = faCalendarDays;
  protected readonly faFolderOpen = faFolderOpen;
  protected readonly faUsers = faUsers;

  // Indique si la sidebar est réduite
  closed = input.required<boolean>();

}
