import { Component, computed, inject, signal } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterLink } from '@angular/router';
import { faBell } from '@fortawesome/free-regular-svg-icons';
import { faCalendar, faUser } from '@fortawesome/free-solid-svg-icons';
import { NotificationService } from '../../../../../../features/notifications/services/notification/notification-service';
import { DatePipe } from '@angular/common';
import { INotification } from '../../../../../../features/notifications/interfaces/notification.interface';

@Component({
  selector: 'app-user-notif',
  imports: [FaIconComponent, RouterLink, DatePipe],
  templateUrl: './user-notif.html',
  styleUrl: './user-notif.css',
})
export class UserNotif {
  protected readonly faBell = faBell;
  protected readonly faCalendar = faCalendar;
  protected readonly faUser = faUser;

  private notificationService = inject(NotificationService);
  isNotificationMenuOpen = signal(false);

  notifications = this.notificationService.notifications;
  unreadCount = computed(() => this.notifications().filter((n) => !n.read).length);

  openNotificationMenu(): void {
    this.isNotificationMenuOpen.set(true);
  }

  closeNotificationMenu(): void {
    this.isNotificationMenuOpen.set(false);
  }

  markAsRead(notif: INotification): void {
    if (notif.read) return;
    this.notificationService.markAsRead(notif.publicId).subscribe();
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe();
  }
}
