import {Component, input, output} from '@angular/core';

@Component({
  selector: 'app-paginate',
  imports: [],
  templateUrl: './paginate.html',
  styleUrl: './paginate.css',
})
export class Paginate {
  totalPages = input.required<number>();
  number = input.required<number>();
  first = input.required<boolean>();
  last = input.required<boolean>();
  pageSize = input.required<number>();

  pageChange = output<number>();
  pageSizeChange = output<number>();

  goToPage(page: number): void {
    this.pageChange.emit(page);
  }

  onPageSizeChange(event: Event): void {
    const size = +(event.target as HTMLSelectElement).value;
    this.pageSizeChange.emit(size);
  }

  pagesArray(): number[] {
    return Array.from({ length: this.totalPages() }, (_, i) => i);
  }
}
