import {Component, computed, effect, inject, signal} from '@angular/core';
import { UserService } from '../../../../core/services/user/user-service';
import {toObservable, toSignal} from '@angular/core/rxjs-interop';
import {combineLatest, switchMap} from 'rxjs';
import {Paginate} from '../../../../shared/components/paginate/paginate';
import {ListUser} from '../../components/list-user/list-user';
import {httpResource} from '@angular/common/http';
import {IApiResponse} from '../../../../shared/interfaces/api-response.interface';
import {IPage} from '../../../../shared/interfaces/pageable/page.interface';
import {IUser} from '../../../../shared/interfaces/user.interface';
import {FlashMessageService} from '../../../../core/services/flashMessage/flash-message-service';
import {IPageable} from '../../../../shared/interfaces/pageable/pageable.interface';
import { WebSocketService } from '../../../../core/websocket/services/websocket-service';


@Component({
  selector: 'app-directory',
  imports: [ListUser, Paginate, ListUser],
  templateUrl: './directory.html',
  styleUrl: './directory.css',
})
export class Directory {
  private userService = inject(UserService);
  private wsService = inject(WebSocketService);

  readonly pageable = signal<IPageable>({ page: 0, size: 12 });

  users = signal<IApiResponse<IPage<IUser>> | undefined>(undefined);

  constructor() {
    combineLatest([toObservable(this.pageable)])
      .pipe(switchMap(([pageable]) => this.userService.getAllUsers(pageable)))
      .subscribe((response) => {
        this.users.set(response);
      });

    this.wsService.subscribeToPresence().subscribe((event) => {
      this.users.update((current) => {
        if (!current) return current;

        return {
          ...current,
          data: {
            ...current.data,
            content: current.data.content.map((u) =>
              u.publicId === event.userId ? { ...u, online: event.online } : u,
            ),
          },
        };
      });
    });
  }

  goToPage(page: number): void {
    this.pageable.update((p) => ({ ...p, page }));
  }

  changePageSize(size: number): void {
    this.pageable.set({ page: 0, size });
  }
}

