import { Component, computed, inject, signal } from '@angular/core';
import { NotificationService } from '../../services/notification/notification-service';
import { INotification } from '../../interfaces/notification.interface';
import { DatePipe } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {
  faUserPlus,
  faThumbsUp,
  faComment,
  faBriefcase,
  faEllipsisVertical,
} from '@fortawesome/free-solid-svg-icons';

type NotificationFilter = 'all' | 'unread';

@Component({
  selector: 'app-notifications',
  imports: [DatePipe, FaIconComponent],
  templateUrl: './notifications.html',
  styleUrl: './notifications.css',
})
export class Notifications {
  private notificationService = inject(NotificationService);

  faUserPlus = faUserPlus;
  faThumbsUp = faThumbsUp;
  faComment = faComment;
  faBriefcase = faBriefcase;
  faEllipsisVertical = faEllipsisVertical;

  filter = signal<NotificationFilter>('all');

  notifications = this.notificationService.notifications;
  unreadCount = computed(() => this.notifications().filter((n) => !n.read).length);

  displayedNotifications = computed(() => {
    const list = this.notifications();
    return this.filter() === 'unread' ? list.filter((n) => !n.read) : list;
  });

  setFilter(filter: NotificationFilter): void {
    this.filter.set(filter);
  }

  iconFor(type: INotification['type']) {
    switch (type) {
      case 'NEW_MESSAGE':
        return this.faComment;
      case 'ADDED_TO_CONVERSATION':
        return this.faUserPlus;
      case 'REMOVED_FROM_CONVERSATION':
        return this.faUserPlus;
      default:
        return this.faBriefcase;
    }
  }

  markAsRead(notif: INotification): void {
    if (notif.read) return;
    this.notificationService.markAsRead(notif.publicId).subscribe();
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe();
  }
}
