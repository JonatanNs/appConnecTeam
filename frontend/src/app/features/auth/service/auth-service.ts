import {inject, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ENVIRONMENT} from '../../../environments/environement';
import {Observable} from 'rxjs';
import {IApiResponse} from '../../../shared/interfaces/api-response.interface';
import {IUser} from '../../../shared/interfaces/user.interface';
import {LoginDTO} from '../../../shared/interfaces/LoginDTO';

@Service()
export class AuthService {
  private http = inject(HttpClient);
  private baseUrl = ENVIRONMENT.apiUrl;

  showLogin(formLogin : LoginDTO) : Observable<IApiResponse<LoginDTO>>{
    return this.http.post<IApiResponse<LoginDTO>>(`${this.baseUrl}/auth/login`, formLogin);
  }


}
