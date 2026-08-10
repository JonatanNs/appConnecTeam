import {Component, computed, effect, ElementRef, inject, signal, ViewChild} from '@angular/core';
import {FlashMessageService} from '../../../../core/services/flashMessage/flash-message-service';
import {UserService} from '../../../../core/services/user/user-service';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
import {catchError, combineLatest, of, switchMap} from 'rxjs';
import {FaIconComponent, FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {Paginate} from '../../../../shared/components/paginate/paginate';
import {NgClass, NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {faPlus} from '@fortawesome/free-solid-svg-icons';
import {IUser} from '../../../../shared/interfaces/user.interface';
import {httpResource} from '@angular/common/http';
import {IApiResponse} from '../../../../shared/interfaces/api-response.interface';
import {IPage} from '../../../../shared/interfaces/pageable/page.interface';
import {IPageable} from '../../../../shared/interfaces/pageable/pageable.interface';

@Component({
  selector: 'app-admin-user-management',
  imports: [
    FontAwesomeModule,
    Paginate,
    NgOptimizedImage,
    RouterLink,
    FormsModule,
    NgClass
  ],
  templateUrl: './admin-user-management.html',
  styleUrl: './admin-user-management.css',
})
export class AdminUserManagement {

  private userService = inject(UserService);
  private flashMessage = inject(FlashMessageService);
  private refreshTrigger = signal(0);

  private refreshUsers(): void {
    this.refreshTrigger.update(v => v + 1);
  }

  protected readonly faPlus = faPlus;
  readonly pageable = signal<IPageable>({ page: 0, size: 12 });

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

  goToPage(page: number): void {
    this.pageable.update(p => ({ ...p, page }));
  }

  changePageSize(size: number): void {
    this.pageable.set({ page: 0, size });
  }

  @ViewChild('userModal')
  userModal!: ElementRef<HTMLDialogElement>;
  selectedUser: IUser | null = null;

  openUserModal(user: IUser) {
    this.selectedUser = user;
    this.userModal.nativeElement.showModal();
  }

  closeUserModal() {
    this.userModal.nativeElement.close();
  }

  confirmAction() {
    if (!this.selectedUser) {
      return;
    }

    if (this.selectedUser.active) {
      this.deactivateUser(this.selectedUser.publicId);
    } else {
      this.activateUser(this.selectedUser.publicId);
    }

    this.closeUserModal();
  }

  deactivateUser(publicId: string): void {
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

  activateUser(publicId: string): void {
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
