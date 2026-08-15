import { Component, signal } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { RouterLink } from '@angular/router';
import {
  faArrowRight, faChevronDown,
  faCircleQuestion,
  faGear,
  faRightFromBracket, faShieldHalved,
  faUser,
} from '@fortawesome/free-solid-svg-icons';
import { faBell } from '@fortawesome/free-regular-svg-icons';

@Component({
  selector: 'app-user-profil',
  imports: [FaIconComponent, RouterLink],
  templateUrl: './user-profil.html',
  styleUrl: './user-profil.css',
})
export class UserProfil {
  protected readonly faRightFromBracket = faRightFromBracket;
  protected readonly faBell = faBell;
  protected readonly faUser = faUser;
  protected readonly faGear = faGear;
  protected readonly faCircleQuestion = faCircleQuestion;

  isProfileMenuOpen = signal(false);

  openProfileMenu(): void {
    this.isProfileMenuOpen.set(true);
  }

  closeProfileMenu(): void {
    this.isProfileMenuOpen.set(false);
  }

  protected readonly faArrowRight = faArrowRight;
  protected readonly faShieldHalved = faShieldHalved;
  protected readonly faChevronDown = faChevronDown;
}
