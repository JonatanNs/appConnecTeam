import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormAdduser } from './form-adduser';

describe('FormAdduser', () => {
  let component: FormAdduser;
  let fixture: ComponentFixture<FormAdduser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormAdduser],
    }).compileComponents();

    fixture = TestBed.createComponent(FormAdduser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
