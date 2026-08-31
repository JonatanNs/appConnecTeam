import { inject, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { toSignal } from '@angular/core/rxjs-interop';
import { ENVIRONMENT } from '../../../../../environments/environment';
import { IPageable } from '../../../../shared/interfaces/pageable/pageable.interface';
import {
  catchError,
  map,
  merge,
  Observable,
  of,
  scan,
  startWith,
  Subject,
  switchMap,
  tap,
} from 'rxjs';
import { IApiResponse } from '../../../../shared/interfaces/api-response.interface';
import { IPage } from '../../../../shared/interfaces/pageable/page.interface';
import { INotification } from '../../interfaces/notification.interface';
import { WebsocketService } from '../../../../core/websocket/services/websocket.service';

type NotificationEvent =
  | { kind: 'new'; notification: INotification }
  | { kind: 'read'; publicId: string }
  | { kind: 'read-all' }
  | { kind: 'read-conversation'; conversationPublicId: string };

@Service()
export class NotificationService {
  private http = inject(HttpClient);
  private wsService = inject(WebsocketService);
  private baseUrl = ENVIRONMENT.apiUrl;

  private defaultPageable = { page: 0, size: 20 };

  private notificationRead$ = new Subject<string>();
  readonly onNotificationRead = this.notificationRead$.asObservable();

  private allNotificationsRead$ = new Subject<void>();
  readonly onAllNotificationsRead = this.allNotificationsRead$.asObservable();

  private conversationNotificationsRead$ = new Subject<string>();
  readonly onConversationNotificationsRead$ = this.conversationNotificationsRead$.asObservable();

  private myNewNotifications$ = new Subject<INotification>();
  readonly onMyNewNotifications$ = this.myNewNotifications$.asObservable();

  private history$ = this.getMyNotifications(this.defaultPageable).pipe(
    map((res) => res.data.content),
    catchError((err) => {
      console.error('Erreur chargement notification', err);
      return of([] as INotification[]);
    }),
  );

  private events$ = merge(
    this.wsService
      .subscribeToNotifications()
      .pipe(map((notification): NotificationEvent => ({ kind: 'new', notification }))),
    this.onNotificationRead.pipe(
      map((publicId): NotificationEvent => ({ kind: 'read', publicId })),
    ),
    this.onAllNotificationsRead.pipe(map((): NotificationEvent => ({ kind: 'read-all' }))),
    this.onConversationNotificationsRead$.pipe(
      map((conversationPublicId): NotificationEvent => ({
        kind: 'read-conversation',
        conversationPublicId,
      })),
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
          startWith(history),
        ),
      ),
    ),
    { initialValue: [] as INotification[] },
  );

  constructor() {
    this.wsService.subscribeToNotifications().subscribe((notif) => {
      this.myNewNotifications$.next(notif);
    });
  }

  getMyNotifications(pageable: IPageable): Observable<IApiResponse<IPage<INotification>>> {
    return this.http.get<IApiResponse<IPage<INotification>>>(
      `${this.baseUrl}/notifications?page=${pageable.page}&size=${pageable.size}`,
    );
  }

  getUnreadCount(): Observable<IApiResponse<number>> {
    return this.http.get<IApiResponse<number>>(`${this.baseUrl}/notifications/unread-count`);
  }

  markAsRead(publicId: string): Observable<IApiResponse<number>> {
    return this.http
      .patch<IApiResponse<number>>(`${this.baseUrl}/notifications/${publicId}/read`, null)
      .pipe(tap(() => this.notificationRead$.next(publicId)));
  }

  markAllAsRead(): Observable<IApiResponse<number>> {
    return this.http
      .patch<IApiResponse<number>>(`${this.baseUrl}/notifications/read-all`, null)
      .pipe(tap(() => this.allNotificationsRead$.next()));
  }

  markConversationAsRead(conversationPublicId: string): Observable<IApiResponse<void>> {
    return this.http
      .patch<IApiResponse<void>>(
        `${this.baseUrl}/notifications/conversation/${conversationPublicId}/read`,
        null,
      )
      .pipe(tap(() => this.conversationNotificationsRead$.next(conversationPublicId)));
  }
}
