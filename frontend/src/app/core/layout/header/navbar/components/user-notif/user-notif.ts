import { Component, signal } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterLink } from '@angular/router';
import { faBell } from '@fortawesome/free-regular-svg-icons';
import { faCalendar, faUser } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-user-notif',
  imports: [FaIconComponent, RouterLink],
  templateUrl: './user-notif.html',
  styleUrl: './user-notif.css',
})
export class UserNotif {
  protected readonly faBell = faBell;
  protected readonly faCalendar = faCalendar;
  protected readonly faUser = faUser;

  isNotificationMenuOpen = signal(false);

  openNotificationMenu(): void {
    this.isNotificationMenuOpen.set(true);
  }

  closeNotificationMenu(): void {
    this.isNotificationMenuOpen.set(false);
  }

}
