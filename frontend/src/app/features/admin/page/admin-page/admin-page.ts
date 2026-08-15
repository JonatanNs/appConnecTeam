import { Component } from '@angular/core';
import {RouterLink} from '@angular/router';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {
  faArrowRight,
  faBriefcase, faCalendarDays,
  faGear, faListCheck,
  faNewspaper,
  faShieldHalved,
  faUsers
} from '@fortawesome/free-solid-svg-icons';
import {faBell} from '@fortawesome/free-regular-svg-icons';

@Component({
  selector: 'app-admin-page',
  imports: [
    RouterLink,
    FaIconComponent
  ],
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css',
})
export class AdminPage {
  protected readonly faShieldHalved = faShieldHalved;
  protected readonly faArrowRight = faArrowRight;
  protected readonly faNewspaper = faNewspaper;
  protected readonly faUsers = faUsers;
  protected readonly faBriefcase = faBriefcase;
  protected readonly faGear = faGear;
  protected readonly faListCheck = faListCheck;
  protected readonly faBell = faBell;
  protected readonly faCalendarDays = faCalendarDays;
}
