import { inject, Service, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ENVIRONMENT } from '../../../environments/environement';
import { Observable, tap } from 'rxjs';
import { IApiResponse } from '../../../shared/interfaces/api-response.interface';
import { ICurrentUser } from '../../../shared/interfaces/current-user.interface';
import { WebSocketService } from '../../../core/websocket/services/websocket-service';
import { ILoginRequestDTO } from '../interfaces/login-request-dto.interface';
import { ILoginResponseDTO } from '../interfaces/login-response-dto.interface';

@Service()
export class AuthService {
  private http = inject(HttpClient);
  private wsService = inject(WebSocketService);
  private baseUrl = ENVIRONMENT.apiUrl;

  private readonly ACCESS_TOKEN_LIFETIME_MS = 900000; // 15 minutes

  private refreshTimer?: ReturnType<typeof setTimeout>;

  private _currentUser = signal<ICurrentUser | null>(null);
  readonly currentUser = this._currentUser.asReadonly();

  showLogin(formLogin: ILoginRequestDTO): Observable<IApiResponse<ICurrentUser>> {
    return this.http.post<IApiResponse<ICurrentUser>>(`${this.baseUrl}/auth/login`, formLogin).pipe(
      tap((response) => {
        this._currentUser.set({
          publicId: response.data.publicId,
          email: response.data.email,
          firstname: response.data.firstname,
          lastname: response.data.lastname,
          online: response.data.online,
        });
        this.wsService.connect();
        this.scheduleProactiveRefresh(this.ACCESS_TOKEN_LIFETIME_MS);
      }),
    );
  }

  logout(): Observable<IApiResponse<void>> {
    this._currentUser.set(null);
    this.wsService.disconnect();
    this.clearScheduledRefresh();
    return this.http.post<IApiResponse<void>>(`${this.baseUrl}/auth/logout`, null);
  }

  fetchCurrentUser(): Observable<IApiResponse<ICurrentUser>> {
    return this.http.get<IApiResponse<ICurrentUser>>(`${this.baseUrl}/auth/me`).pipe(
      tap((response) => {
        this._currentUser.set(response.data);
        this.wsService.connect();
        this.scheduleProactiveRefresh(this.ACCESS_TOKEN_LIFETIME_MS);
      }),
    );
  }

  refreshToken(): Observable<IApiResponse<ILoginResponseDTO>> {
    return this.http.post<IApiResponse<ILoginResponseDTO>>(`${this.baseUrl}/auth/refresh`, null).pipe(
      tap(() => {
        this.scheduleProactiveRefresh(this.ACCESS_TOKEN_LIFETIME_MS);
      }),
    );
  }

  private scheduleProactiveRefresh(expiresInMs: number): void {
    this.clearScheduledRefresh();

    const refreshBeforeExpiry = expiresInMs * 0.8;
    this.refreshTimer = setTimeout(() => {
      this.refreshToken().subscribe();
    }, refreshBeforeExpiry);
  }

  private clearScheduledRefresh(): void {
    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
      this.refreshTimer = undefined;
    }
  }
}
