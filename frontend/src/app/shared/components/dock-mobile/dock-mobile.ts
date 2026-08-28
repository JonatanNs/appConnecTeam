import { Component } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import {
  faAddressBook,
  faCalendarDays,
  faFileLines,
  faIdCard,
  faListCheck,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-dock-mobile',
  imports: [FaIconComponent],
  templateUrl: './dock-mobile.html',
  styleUrl: './dock-mobile.css',
})
export class DockMobile {
  protected readonly faListCheck = faListCheck;
  protected readonly faFileLines = faFileLines;
  protected readonly faIdCard = faIdCard;
  protected readonly faCalendarDays = faCalendarDays;
  protected readonly faAddressBook = faAddressBook;
}
