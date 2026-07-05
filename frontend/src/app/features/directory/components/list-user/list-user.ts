import { Component, input } from '@angular/core';
import { IApiResponse } from '../../../../shared/interfaces/api-response.interface';
import { IUser } from '../../../../shared/interfaces/user.interface';
import { IPage } from '../../../../shared/interfaces/pages/page.interface';
import {NgOptimizedImage} from '@angular/common';

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
