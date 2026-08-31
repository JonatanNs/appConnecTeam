import {Component, computed, inject, input} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {IApiResponse} from '../../../interfaces/api-response.interface';
import {IPage} from '../../../interfaces/pageable/page.interface';
import {IUser} from '../../../interfaces/user.interface';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faEnvelope} from '@fortawesome/free-regular-svg-icons';
import {RouterLink} from '@angular/router';
import {faCommentDots, faPhone, faUser} from '@fortawesome/free-solid-svg-icons';
import {AuthService} from '../../../../features/auth/service/auth.service';
import {SearchFilter} from '../../search-filter/search-filter';

@Component({
  selector: 'app-list-user',
  imports: [NgOptimizedImage, FaIconComponent, RouterLink, SearchFilter],
  templateUrl: './list-user.html',
  styleUrl: './list-user.css',
})
export class ListUser {
  private authService = inject(AuthService);

  currentUser = this.authService.currentUser
  users = input.required<IApiResponse<IPage<IUser>>>();


  activeUsers = computed(() => {
    return this.users()?.data.content.filter((u) => u.active) ?? [];
  });

  protected readonly faEnvelope = faEnvelope;
  protected readonly faUser = faUser;
  protected readonly faPhone = faPhone;
  protected readonly faCommentDots = faCommentDots;
}
