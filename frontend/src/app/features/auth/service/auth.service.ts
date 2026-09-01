import { inject, Service, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ENVIRONMENT } from '../../../../environments/environment';
import { Observable, tap } from 'rxjs';
import { IApiResponse } from '../../../shared/interfaces/api-response.interface';
import { ICurrentUser } from '../../../shared/interfaces/current-user.interface';
import { WebsocketService } from '../../../core/websocket/services/websocket.service';
import { ILoginRequestDTO } from '../interfaces/login-request-dto.interface';
import { ILoginResponseDTO } from '../interfaces/login-response-dto.interface';

@Service()
export class AuthService {
  private http = inject(HttpClient);
  private wsService = inject(WebsocketService);
  private baseUrl = ENVIRONMENT.apiUrl;

  private refreshTimer?: ReturnType<typeof setTimeout>;

  private _currentUser = signal<ICurrentUser | null>(null);
  readonly currentUser = this._currentUser.asReadonly();

  hasRole(roleName: string): boolean {
    return this._currentUser()?.roles?.some((r) => r.name === roleName) ?? false;
  }

  showLogin(formLogin: ILoginRequestDTO): Observable<IApiResponse<ICurrentUser>> {
    return this.http.post<IApiResponse<ILoginResponseDTO>>(`${this.baseUrl}/auth/login`, formLogin).pipe(
      tap((response) => {
        this._currentUser.set({
          publicId: response.data.publicId,
          email: response.data.email,
          firstname: response.data.firstname,
          lastname: response.data.lastname,
          online: response.data.online,
          roles: response.data.roles
        });
        this.wsService.connect();
        this.scheduleProactiveRefresh(response.data.tokenExpiresIn);
      }),
    ) as unknown as Observable<IApiResponse<ICurrentUser>>;
  }

  logout(): Observable<IApiResponse<void>> {
    this._currentUser.set(null);
    this.wsService.disconnect();
    this.clearScheduledRefresh();
    return this.http.post<IApiResponse<void>>(`${this.baseUrl}/auth/logout`, null);
  }

  fetchCurrentUser(): Observable<IApiResponse<ICurrentUser>> {
    return this.http.get<IApiResponse<ILoginResponseDTO>>(`${this.baseUrl}/auth/me`).pipe(
      tap((response) => {
        this._currentUser.set(response.data);
        this.wsService.connect();
        this.scheduleProactiveRefresh(response.data.tokenExpiresIn);
      }),
    );
  }

  refreshToken(): Observable<IApiResponse<ILoginResponseDTO>> {
    return this.http.post<IApiResponse<ILoginResponseDTO>>(`${this.baseUrl}/auth/refresh`, null).pipe(
      tap((response) => {
        this.scheduleProactiveRefresh(response.data.tokenExpiresIn);
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
