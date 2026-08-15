import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {FaIconComponent} from '@fortawesome/angular-fontawesome';
import {faMagnifyingGlass} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-search-filter',
  imports: [FormsModule, FaIconComponent],
  templateUrl: './search-filter.html',
  styleUrl: './search-filter.css',
})
export class SearchFilter {
  protected readonly faMagnifyingGlass = faMagnifyingGlass;
}
