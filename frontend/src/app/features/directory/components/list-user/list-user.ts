import { Component, input } from '@angular/core';
import { IApiResponse } from '../../../../shared/interfaces/api-response.interface';
import { IUser } from '../../../../shared/interfaces/user.interface';
import { IPage } from '../../../../shared/interfaces/pages/page.interface';

@Component({
  selector: 'app-list-user',
  imports: [],
  templateUrl: './list-user.html',
  styleUrl: './list-user.css',
})
export class ListUser {
  users = input.required<IApiResponse<IPage<IUser>>>();
}
