import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faBell} from '@fortawesome/free-regular-svg-icons';
import {
  faCalendar,
  faChevronRight,
  faCircleQuestion,
  faGear, faMagnifyingGlass,
  faUser,
} from '@fortawesome/free-solid-svg-icons';
import { faRightFromBracket } from '@fortawesome/free-solid-svg-icons';
import { UserProfil } from './components/user-profil/user-profil';
import { UserNotif } from './components/user-notif/user-notif';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, FaIconComponent, UserProfil, UserNotif, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {

  protected readonly faMagnifyingGlass = faMagnifyingGlass;
}
