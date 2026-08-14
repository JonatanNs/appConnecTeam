import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserNotif } from './user-notif';

describe('UserNotif', () => {
  let component: UserNotif;
  let fixture: ComponentFixture<UserNotif>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserNotif],
    }).compileComponents();

    fixture = TestBed.createComponent(UserNotif);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
