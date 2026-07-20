import {Component, inject, signal} from '@angular/core';
import { UserService } from '../../../../core/services/user-service';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
import {combineLatest, switchMap} from 'rxjs';
import {Paginate} from '../../../../shared/components/paginate/paginate';
import {ListUser} from '../../../../shared/components/list-user/list-user';


@Component({
  selector: 'app-directory',
  imports: [ListUser, Paginate, ListUser],
  templateUrl: './directory.html',
  styleUrl: './directory.css',
})
export class Directory {

  private userService = inject(UserService);

  currentPage = signal(0);
  pageSize = signal(12);

  users = toSignal(
    combineLatest([
      toObservable(this.currentPage),
      toObservable(this.pageSize)
    ]).pipe(
      switchMap(([page, size]) => this.userService.getAllUsers({ page, size }))
    )
  );

  goToPage(page: number): void {
    this.currentPage.set(page);
  }

  changePageSize(size: number): void {
    this.pageSize.set(size);
    this.currentPage.set(0);
  }
}

