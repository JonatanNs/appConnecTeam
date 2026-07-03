import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { IUser } from '../../shared/interfaces/user.interface';
import { ENVIRONMENT } from '../../environments/environement';
import { IApiResponse } from '../../shared/interfaces/api-response.interface';
import { IPage } from '../../shared/interfaces/pageable.interface';

@Service()
export class UserService {
    private http = inject(HttpClient);
    private baseUrl = ENVIRONMENT.apiUrl;

    getAllUsers() : Observable<IApiResponse<IPage<IUser[]>>> {
        return this.http.get<IApiResponse<IPage<IUser[]>>>(`${this.baseUrl}/users`);
    }

    getUser(){

    }

    getUserByEmail(){

    }

    updateUser(){

    }

    createUser(){

    }

    deleteUser(){

    }

}
