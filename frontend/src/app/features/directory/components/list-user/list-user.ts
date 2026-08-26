import { Component, computed, input } from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {IApiResponse} from '../../../../shared/interfaces/api-response.interface';
import {IPage} from '../../../../shared/interfaces/pageable/page.interface';
import {IUser} from '../../../../shared/interfaces/user.interface';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faEnvelope } from '@fortawesome/free-regular-svg-icons';
import { RouterLink } from '@angular/router';
import { faUser } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-list-user',
  imports: [NgOptimizedImage, FaIconComponent, RouterLink],
  templateUrl: './list-user.html',
  styleUrl: './list-user.css',
})
export class ListUser {
  users = input.required<IApiResponse<IPage<IUser>>>();

  //activeUsers = computed(() => this.users()?.data.content.filter((u) => u.active) ?? []);

  activeUsers = computed(() => {
    const users = this.users()?.data.content.filter((u) => u.active) ?? [];

    console.log('ACTIVE USERS:', users);

    return users;
  });

  protected readonly faEnvelope = faEnvelope;
  protected readonly faUser = faUser;
}
