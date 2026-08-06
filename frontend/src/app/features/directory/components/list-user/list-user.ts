import { Component, input } from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {IApiResponse} from '../../../../shared/interfaces/api-response.interface';
import {IPage} from '../../../../shared/interfaces/pageable/page.interface';
import {IUser} from '../../../../shared/interfaces/user.interface';

@Component({
  selector: 'app-list-user',
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './list-user.html',
  styleUrl: './list-user.css',
})
export class ListUser {
  users = input.required<IApiResponse<IPage<IUser>>>();
}
