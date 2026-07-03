import { Component, effect, inject } from '@angular/core';
import { UserService } from '../../../../core/services/user-service';
import { toSignal } from '@angular/core/rxjs-interop';
@Component({
  selector: 'app-directory',
  imports: [],
  templateUrl: './directory.html',
  styleUrl: './directory.css',
})
export class Directory {
  private userService = inject(UserService);

  users = toSignal(this.userService.getAllUsers());

  constructor() {
    effect(() => {
      console.log(this.users());
    });
  }
}

