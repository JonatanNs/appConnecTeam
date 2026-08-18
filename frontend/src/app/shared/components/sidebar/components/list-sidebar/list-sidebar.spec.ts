import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListSidebar } from './list-sidebar';

describe('ListSidebar', () => {
  let component: ListSidebar;
  let fixture: ComponentFixture<ListSidebar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListSidebar],
    }).compileComponents();

    fixture = TestBed.createComponent(ListSidebar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
