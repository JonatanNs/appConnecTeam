import { inject, Service } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ENVIRONMENT } from '../../../../environments/environement';
import { IPageable } from '../../../../shared/interfaces/pageable/pageable.interface';
import { Observable, Subject, tap } from 'rxjs';
import { IApiResponse } from '../../../../shared/interfaces/api-response.interface';
import { IPage } from '../../../../shared/interfaces/pageable/page.interface';
import { INotification } from '../../interfaces/notification.interface';

@Service()
export class NotificationService {
  private http = inject(HttpClient);
  private baseUrl = ENVIRONMENT.apiUrl;

  private notificationRead$ = new Subject<string>();
  readonly onNotificationRead = this.notificationRead$.asObservable();

  private allNotificationsRead$ = new Subject<void>();
  readonly onAllNotificationsRead = this.allNotificationsRead$.asObservable();

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
