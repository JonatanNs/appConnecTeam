import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { IUser } from '../../../shared/interfaces/user.interface';
import { ENVIRONMENT } from '../../../environments/environement';
import { IApiResponse } from '../../../shared/interfaces/api-response.interface';
import { IPage } from '../../../shared/interfaces/pages/page.interface';
import {IPageable} from '../../../shared/interfaces/pages/pageable.interface';

@Service()
export class UserService {
  private http = inject(HttpClient);
  private baseUrl = ENVIRONMENT.apiUrl;

  getAllUsers(pageable: IPageable): Observable<IApiResponse<IPage<IUser>>> {
    return this.http.get<IApiResponse<IPage<IUser>>>(`${this.baseUrl}/users?page=${pageable.page}&size=${pageable.size}`);
  }

  getUser(publicId: string): Observable<IApiResponse<IUser>> {
    return this.http.get<IApiResponse<IUser>>(`${this.baseUrl}/users/${publicId}`);
  }

  getUserByEmail(email : string) : Observable<IApiResponse<IUser>> {
    return this.http.get<IApiResponse<IUser>>(`${this.baseUrl}/users/email?=${email}`);
  }

  updateUser(publicId : string, user : IUser) : Observable<IApiResponse<void>> {
    return this.http.put<IApiResponse<void>>(`${this.baseUrl}/users/${publicId}`, user);
  }

  createUser(user : IUser) : Observable<IApiResponse<IUser>> {
    return this.http.post<IApiResponse<IUser>>(`${this.baseUrl}/users`, user);
  }

  deleteUser(publicId : string) : Observable<IApiResponse<void>> {
    return this.http.delete<IApiResponse<void>>(`${this.baseUrl}/users/${publicId}`);
  }

  deactivateUser(publicId : string) : Observable<IApiResponse<void>> {
    return this.http.put<IApiResponse<void>>(`${this.baseUrl}/users/deactivate/${publicId}`, {});
  }

  activateUser(publicId : string) : Observable<IApiResponse<void>> {
    return this.http.put<IApiResponse<void>>(`${this.baseUrl}/users/activate/${publicId}`, {});
  }
}
