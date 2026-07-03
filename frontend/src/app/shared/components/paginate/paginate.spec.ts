import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Paginate } from './paginate';

describe('Paginate', () => {
  let component: Paginate;
  let fixture: ComponentFixture<Paginate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Paginate],
    }).compileComponents();

    fixture = TestBed.createComponent(Paginate);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
