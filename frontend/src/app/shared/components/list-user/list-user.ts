import { Component, input } from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {IApiResponse} from '../../interfaces/api-response.interface';
import {IPage} from '../../interfaces/pages/page.interface';
import {IUser} from '../../interfaces/user.interface';

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
