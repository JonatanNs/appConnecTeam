import {Component, inject, signal} from '@angular/core';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faPlus} from '@fortawesome/free-solid-svg-icons';
import {Paginate} from '../../../../shared/components/paginate/paginate';
import {UserService} from '../../../../core/services/user-service';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
import {combineLatest, switchMap} from 'rxjs';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-admin-user-management',
  imports: [
    FaIconComponent,
    Paginate,
    NgOptimizedImage,
    RouterLink,
    FormsModule
  ],
  templateUrl: './admin-user-management.html',
  styleUrl: './admin-user-management.css',
})
export class AdminUserManagement {
  protected readonly faPlus = faPlus;
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
