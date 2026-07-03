import { Component, effect, inject, Signal } from '@angular/core';
import { UserService } from '../../../../core/services/user-service';
import { toSignal } from '@angular/core/rxjs-interop';
import { ListUser } from '../../components/list-user/list-user';

@Component({
  selector: 'app-directory',
  imports: [ListUser],
  templateUrl: './directory.html',
  styleUrl: './directory.css',
})
export class Directory {
  private userService = inject(UserService);

  users  = toSignal(this.userService.getAllUsers());

}

