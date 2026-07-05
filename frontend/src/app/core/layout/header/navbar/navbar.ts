import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faBell} from '@fortawesome/free-regular-svg-icons';
import {faGear, faUser} from '@fortawesome/free-solid-svg-icons';

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
}
