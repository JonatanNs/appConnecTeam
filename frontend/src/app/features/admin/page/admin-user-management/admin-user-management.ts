import {Component, inject, signal} from '@angular/core';
import {FlashMessageService} from '../../../../core/services/flashMessage/flash-message-service';
import {UserService} from '../../../../core/services/user/user-service';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
import {catchError, combineLatest, of, switchMap} from 'rxjs';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {Paginate} from '../../../../shared/components/paginate/paginate';
import {NgClass} from '@angular/common';
import {RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {faPlus} from '@fortawesome/free-solid-svg-icons';
import {IPageable} from '../../../../shared/interfaces/pageable/pageable.interface';
import {TableListUser} from './components/table-list-user/table-list-user';

@Component({
  selector: 'app-admin-user-management',
  imports: [
    FontAwesomeModule,
    Paginate,
    RouterLink,
    FormsModule,
    NgClass,
    TableListUser
  ],
  templateUrl: './admin-user-management.html',
  styleUrl: './admin-user-management.css',
})
export class AdminUserManagement {

  private userService = inject(UserService);
  private flashMessage = inject(FlashMessageService);

  private refreshTrigger = signal(0);

  protected readonly faPlus = faPlus;

  readonly pageable = signal<IPageable>({
    page: 0,
    size: 12
  });

  users = toSignal(
    combineLatest([
      toObservable(this.pageable),
      toObservable(this.refreshTrigger)
    ]).pipe(
      switchMap(([pageable]) =>
        this.userService.getAllUsers(pageable).pipe(
          catchError((err) => {
            this.flashMessage.error(err.error.message);
            return of(undefined);
          })
        )
      )
    )
  );

  private refreshUsers(): void {
    this.refreshTrigger.update(v => v + 1);
  }

  goToPage(page: number): void {
    this.pageable.update(p => ({
      ...p,
      page
    }));
  }

  changePageSize(size: number): void {
    this.pageable.set({
      page: 0,
      size
    });
  }

  onUserAction(event: {
    action: 'activate' | 'deactivate';
    publicId: string;
  }): void {

    if (event.action === 'deactivate') {
      this.deactivateUser(event.publicId);
    } else {
      this.activateUser(event.publicId);
    }
  }

  private deactivateUser(publicId: string): void {
    this.userService.deactivateUser(publicId).subscribe({
      next: (res) => {
        this.flashMessage.success(res.message);
        this.refreshUsers();
      },
      error: (err) => {
        this.flashMessage.error(err.error.message);
      }
    });
  }

  private activateUser(publicId: string): void {
    this.userService.activateUser(publicId).subscribe({
      next: (res) => {
        this.flashMessage.success(res.message);
        this.refreshUsers();
      },
      error: (err) => {
        this.flashMessage.error(err.error.message);
      }
    });
  }
}
