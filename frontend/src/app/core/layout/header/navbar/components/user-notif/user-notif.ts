import { Component, computed, inject, signal } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterLink } from '@angular/router';
import { faBell } from '@fortawesome/free-regular-svg-icons';
import { faCalendar, faUser } from '@fortawesome/free-solid-svg-icons';
import { NotificationService } from '../../../../../../features/notifications/services/notification/notification-service';
import { WebSocketService } from '../../../../../websocket/services/websocket-service';
import { toSignal } from '@angular/core/rxjs-interop';
import { DatePipe } from '@angular/common';
import { catchError, map, merge, of, scan, switchMap } from 'rxjs';
import { INotification } from '../../../../../../features/notifications/interfaces/notification.interface';

type NotificationEvent =
  | { kind: 'new'; notification: INotification }
  | { kind: 'read'; publicId: string }
  | { kind: 'read-all' };

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
  private wsService = inject(WebSocketService);

  isNotificationMenuOpen = signal(false);

  private defaultPageable = { page: 0, size: 10 };

  private history$ = this.notificationService.getMyNotifications(this.defaultPageable).pipe(
    map((res) => res.data.content),
    catchError((err) => {
      console.error('Erreur chargement notifications', err);
      return of([] as INotification[]);
    }),
  );

  private events$ = merge(
    this.wsService
      .subscribeToNotifications()
      .pipe(map((notification): NotificationEvent => ({ kind: 'new', notification }))),
    this.notificationService.onNotificationRead.pipe(
      map((publicId): NotificationEvent => ({ kind: 'read', publicId })),
    ),
    this.notificationService.onAllNotificationsRead.pipe(
      map((): NotificationEvent => ({ kind: 'read-all' })),
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
            }
          }, history),
        ),
      ),
    ),
    { initialValue: [] as INotification[] },
  );

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
