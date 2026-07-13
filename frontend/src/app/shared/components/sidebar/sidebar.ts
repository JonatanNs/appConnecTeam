import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {
  faCalendarDays,
  faUsers,
  faFolderOpen,
  faListCheck,
  faCircleQuestion,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, FaIconComponent],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {

  protected readonly faCalendarDays = faCalendarDays;
  protected readonly faUsers = faUsers;
  protected readonly faFolderOpen = faFolderOpen;
  protected readonly faListCheck = faListCheck;
  protected readonly faCircleQuestion = faCircleQuestion;

}
