import { Component, inject, computed, signal } from '@angular/core';
import { NotificationService } from '../../services/notification/notification-service';
import { WebSocketService } from '../../../../core/websocket/services/websocket-service';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, map, merge, of, scan, switchMap } from 'rxjs';
import { INotification } from '../../interfaces/notification.interface';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {
  faUserPlus,
  faThumbsUp,
  faComment,
  faBriefcase,
  faEllipsisVertical,
} from '@fortawesome/free-solid-svg-icons';

type NotificationEvent =
  | { kind: 'new'; notification: INotification }
  | { kind: 'read'; publicId: string }
  | { kind: 'read-all' }
  | { kind: 'read-conversation'; conversationPublicId: string };

type NotificationFilter = 'all' | 'unread';

@Component({
  selector: 'app-notifications',
  imports: [DatePipe, RouterLink, FaIconComponent],
  templateUrl: './notifications.html',
  styleUrl: './notifications.css',
})
export class Notifications {
  private notificationService = inject(NotificationService);
  private wsService = inject(WebSocketService);

  private defaultPageable = { page: 0, size: 20 };

  faUserPlus = faUserPlus;
  faThumbsUp = faThumbsUp;
  faComment = faComment;
  faBriefcase = faBriefcase;
  faEllipsisVertical = faEllipsisVertical;

  filter = signal<NotificationFilter>('all');

  private history$ = this.notificationService.getMyNotifications(this.defaultPageable).pipe(
    map((res) => res.data.content),
    catchError((err) => {
      console.error('Erreur chargement notifications', err);
      return of([] as INotification[]);
    }),
  );

  private events$ = merge(
    this.wsService.subscribeToNotifications().pipe(
      map((notification): NotificationEvent => ({ kind: 'new', notification })),
    ),
    this.notificationService.onNotificationRead.pipe(
      map((publicId): NotificationEvent => ({ kind: 'read', publicId })),
    ),
    this.notificationService.onAllNotificationsRead.pipe(
      map((): NotificationEvent => ({ kind: 'read-all' })),
    ),
    this.notificationService.onConversationNotificationsRead$.pipe(
      map((conversationPublicId): NotificationEvent => ({ kind: 'read-conversation', conversationPublicId })),
    ),
  );

  notifications = toSignal(
    this.history$.pipe(
      switchMap((history) =>
        this.events$.pipe(
          scan((acc: INotification[], event: NotificationEvent) => {
            switch (event.kind) {
              case 'new':
                return [event.notification, ...acc];
              case 'read':
                return acc.map((n) => (n.publicId === event.publicId ? { ...n, read: true } : n));
              case 'read-all':
                return acc.map((n) => ({ ...n, read: true }));
              case 'read-conversation':
                return acc.map((n) =>
                  n.conversationPublicId === event.conversationPublicId ? { ...n, read: true } : n,
                );
            }
          }, history),
        ),
      ),
    ),
    { initialValue: [] as INotification[] },
  );

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
