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
import { UserProfil } from './components/user-profil/user-profil';
import { UserNotif } from './components/user-notif/user-notif';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, FaIconComponent, UserProfil, UserNotif],
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



  protected readonly faCircleQuestion = faCircleQuestion;


}
