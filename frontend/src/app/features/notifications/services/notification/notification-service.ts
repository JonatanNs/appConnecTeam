import { inject, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ENVIRONMENT } from '../../../../environments/environement';
import { IPageable } from '../../../../shared/interfaces/pageable/pageable.interface';
import { Observable, Subject, tap } from 'rxjs';
import { IApiResponse } from '../../../../shared/interfaces/api-response.interface';
import { IPage } from '../../../../shared/interfaces/pageable/page.interface';
import { INotification } from '../../interfaces/notification.interface';
import {WebSocketService} from '../../../../core/websocket/services/websocket-service';

@Service()
export class NotificationService {
  private http = inject(HttpClient);
  private baseUrl = ENVIRONMENT.apiUrl;

  private notificationRead$ = new Subject<string>();
  readonly onNotificationRead = this.notificationRead$.asObservable();

  private allNotificationsRead$ = new Subject<void>();
  readonly onAllNotificationsRead = this.allNotificationsRead$.asObservable();

  private myNewNotifications$ = new Subject<INotification>();
  readonly onMyNewNotifications$ = this.myNewNotifications$.asObservable();

  private wsService = inject(WebSocketService);

  constructor() {
    this.wsService.subscribeToNotifications().subscribe((notif) => {
      this.myNewNotifications$.next(notif);
    });
  }

  private conversationNotificationsRead$ = new Subject<string>();
  readonly onConversationNotificationsRead$ = this.conversationNotificationsRead$.asObservable();

  markConversationAsRead(conversationPublicId: string): Observable<IApiResponse<void>> {
    return this.http
      .patch<IApiResponse<void>>(`${this.baseUrl}/notifications/conversation/${conversationPublicId}/read`, null)
      .pipe(tap(() => this.conversationNotificationsRead$.next(conversationPublicId)));
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
}
