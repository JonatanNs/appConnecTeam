import {Component, signal} from '@angular/core';
import { RouterLink } from '@angular/router';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faBell} from '@fortawesome/free-regular-svg-icons';
import {
  faCalendar,
  faChevronRight,
  faCircleQuestion,
  faGear,
  faUser,
} from '@fortawesome/free-solid-svg-icons';
import { faRightFromBracket } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, FaIconComponent],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  protected readonly faBell = faBell;
  protected readonly faGear = faGear;
  protected readonly faUser = faUser;
  protected readonly faCalendar = faCalendar;
  protected readonly faChevronRight = faChevronRight;
  protected faRightFromBracket = faRightFromBracket;

  isProfileMenuOpen = signal(false);

  openProfileMenu(): void {
    this.isProfileMenuOpen.set(true);
  }

  closeProfileMenu(): void {
    this.isProfileMenuOpen.set(false);
  }

  protected readonly faCircleQuestion = faCircleQuestion;

  isNotificationMenuOpen = signal(false);

  openNotificationMenu(): void {
    this.isNotificationMenuOpen.set(true);
  }

  closeNotificationMenu(): void {
    this.isNotificationMenuOpen.set(false);
  }
}
