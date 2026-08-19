import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormCreateConversation } from './form-create-conversation';

describe('FormCreateConversation', () => {
  let component: FormCreateConversation;
  let fixture: ComponentFixture<FormCreateConversation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormCreateConversation],
    }).compileComponents();

    fixture = TestBed.createComponent(FormCreateConversation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
