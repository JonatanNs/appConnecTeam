import {Component, inject} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {
  faCalendar,
  faChevronRight,
  faCircleQuestion,
  faGear,
  faMagnifyingGlass,
  faRightFromBracket,
  faShieldHalved,
  faUser,
  faXmark,
  faBars,
  faHouse,
  faNewspaper,
  faComments,
  faAddressBook,
  faUsers,
} from '@fortawesome/free-solid-svg-icons';
import { UserProfil } from './components/user-profil/user-profil';
import { UserNotif } from './components/user-notif/user-notif';
import {AuthService} from '../../../../features/auth/service/auth-service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, FaIconComponent, UserProfil, UserNotif, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {

  protected readonly faMagnifyingGlass = faMagnifyingGlass;
  protected readonly faBars = faBars;
  protected readonly faXmark = faXmark;
  protected readonly faShieldHalved = faShieldHalved;
  protected readonly faRightFromBracket = faRightFromBracket;
  protected readonly faCircleQuestion = faCircleQuestion;
  protected readonly faGear = faGear;
  protected readonly faUser = faUser;

  private authService = inject(AuthService);
  private router = inject(Router);

  public isMobileMenuOpen = false;

  logout(): void {
    this.authService.logout()
    this.router.navigate(['/']);
  }

  protected readonly faHouse = faHouse;
  protected readonly faNewspaper = faNewspaper;
  protected readonly faComments = faComments;
  protected readonly faAddressBook = faAddressBook;
  protected readonly faUsers = faUsers;
}
