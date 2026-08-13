import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableListUser } from './table-list-user';

describe('TableListUser', () => {
  let component: TableListUser;
  let fixture: ComponentFixture<TableListUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TableListUser],
    }).compileComponents();

    fixture = TestBed.createComponent(TableListUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
