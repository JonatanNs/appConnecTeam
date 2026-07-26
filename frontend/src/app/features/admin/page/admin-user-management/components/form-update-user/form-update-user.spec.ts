import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormUpdateUser } from './form-update-user';

describe('FormUpdateUser', () => {
  let component: FormUpdateUser;
  let fixture: ComponentFixture<FormUpdateUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormUpdateUser],
    }).compileComponents();

    fixture = TestBed.createComponent(FormUpdateUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
