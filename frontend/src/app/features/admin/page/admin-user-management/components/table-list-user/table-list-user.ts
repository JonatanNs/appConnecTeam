import {
  Component,
  ElementRef,
  input,
  output,
  ViewChild
} from '@angular/core';
import {NgClass, NgOptimizedImage} from '@angular/common';
import {IUser} from '../../../../../../shared/interfaces/user.interface';
import {RouterLink} from '@angular/router';
import {IApiResponse} from '../../../../../../shared/interfaces/api-response.interface';
import {IPage} from '../../../../../../shared/interfaces/pageable/page.interface';

@Component({
  selector: 'app-table-list-user',
  imports: [
    NgOptimizedImage,
    RouterLink,
    NgClass
  ],
  templateUrl: './table-list-user.html',
  styleUrl: './table-list-user.css',
})
export class TableListUser {

  users = input.required<IApiResponse<IPage<IUser>>>();

  userAction = output<{
    action: 'activate' | 'deactivate';
    publicId: string;
  }>();

  @ViewChild('userModal')
  userModal!: ElementRef<HTMLDialogElement>;

  selectedUser: IUser | null = null;

  openUserModal(user: IUser): void {
    this.selectedUser = user;
    this.userModal.nativeElement.showModal();
  }

  closeUserModal(): void {
    this.userModal.nativeElement.close();
    this.selectedUser = null;
  }

  confirmAction(): void {
    if (!this.selectedUser) {
      return;
    }

    this.userAction.emit({
      action: this.selectedUser.active
        ? 'deactivate'
        : 'activate',
      publicId: this.selectedUser.publicId
    });

    this.closeUserModal();
  }
}
