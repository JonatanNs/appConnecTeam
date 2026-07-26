import {Component, inject, signal} from '@angular/core';
import {FlashMessageService} from '../../../../core/services/flashMessage/flash-message-service';
import {UserService} from '../../../../core/services/user/user-service';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
import {catchError, combineLatest, of, switchMap} from 'rxjs';
import {FaIconComponent, FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {Paginate} from '../../../../shared/components/paginate/paginate';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {faPlus} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-admin-user-management',
  imports: [
    FontAwesomeModule,
    Paginate,
    NgOptimizedImage,
    RouterLink,
    FormsModule
  ],
  templateUrl: './admin-user-management.html',
  styleUrl: './admin-user-management.css',
})
export class AdminUserManagement {

  private userService = inject(UserService);
  private flashService = inject(FlashMessageService);

  currentPage = signal(0);
  pageSize = signal(12);
  private refreshTrigger = signal(0);

  users = toSignal(
    combineLatest([
      toObservable(this.currentPage),
      toObservable(this.pageSize),
      toObservable(this.refreshTrigger)
    ]).pipe(
      switchMap(([page, size]) =>
        this.userService.getAllUsers({page, size}).pipe(
          catchError(() => {
            this.flashService.error('Erreur lors du chargement des utilisateurs.');
            return of(undefined);
          })
        )
      )
    )
  );

  goToPage(page: number): void {
    this.currentPage.set(page);
  }

  changePageSize(size: number): void {
    this.pageSize.set(size);
    this.currentPage.set(0);
  }

  private refresh(): void {
    this.refreshTrigger.update(v => v + 1);
  }

  deactivateUser(publicId: string): void {
    this.userService.deactivateUser(publicId).subscribe({
      next: (res) => {
        this.flashService.success(res.message);
        this.refresh();
      },
      error: (err) => {
        this.flashService.error(err.error.message);
      }
    });
  }

  activateUser(publicId: string): void {
    this.userService.activateUser(publicId).subscribe({
      next: (res) => {
        this.flashService.success(res.message);
        this.refresh();
      },
      error: (err) => {
        this.flashService.error(err.error.message);
      }
    });
  }

  protected readonly faPlus = faPlus;
}
