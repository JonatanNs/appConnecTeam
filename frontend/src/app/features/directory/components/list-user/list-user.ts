import { Component, Input, input, InputSignal } from '@angular/core';
import { IApiResponse } from '../../../../shared/interfaces/api-response.interface';
import { IUser } from '../../../../shared/interfaces/user.interface';
import { IPage } from '../../../../shared/interfaces/pageable.interface';
import { Paginate } from '../../../../shared/components/paginate/paginate';

@Component({
  selector: 'app-list-user',
  imports: [Paginate],
  templateUrl: './list-user.html',
  styleUrl: './list-user.css',
})
export class ListUser {
  users = input.required<IApiResponse<IPage<IUser>>>();
}
