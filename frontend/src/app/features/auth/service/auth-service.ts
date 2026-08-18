import {inject, Service, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ENVIRONMENT} from '../../../environments/environement';
import {Observable, tap} from 'rxjs';
import {IApiResponse} from '../../../shared/interfaces/api-response.interface';
import {ILoginDTO} from '../../../shared/interfaces/login-dto.interface';
import {ICurrentUser} from '../../../shared/interfaces/current-user.interface';

@Service()
export class AuthService {
  private http = inject(HttpClient);
  private baseUrl = ENVIRONMENT.apiUrl;

  private _currentUser = signal<ICurrentUser | null>(null);
  readonly currentUser = this._currentUser.asReadonly();

  showLogin(formLogin: ILoginDTO): Observable<IApiResponse<ICurrentUser>> {
    return this.http.post<IApiResponse<ICurrentUser>>(`${this.baseUrl}/auth/login`, formLogin).pipe(
      tap((response) => {
        this._currentUser.set({
          publicId: response.data.publicId,
          email: response.data.email,
          firstname: response.data.firstname,
          lastname: response.data.lastname
        });
      })
    );
  }

  logout(): void {
    this._currentUser.set(null);
    // + appel au endpoint /auth/logout qu'on avait préparé côté backend, pour effacer le cookie
  }

  fetchCurrentUser(): Observable<IApiResponse<ICurrentUser>> {
    return this.http.get<IApiResponse<ICurrentUser>>(`${this.baseUrl}/auth/me`).pipe(
      tap((response) => this._currentUser.set(response.data))
    );
  }
}
