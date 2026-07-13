import { Component } from '@angular/core';
import {RouterLink} from '@angular/router';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faArrowRight, faBriefcase, faNewspaper, faShieldHalved, faUsers} from '@fortawesome/free-solid-svg-icons';

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
}
