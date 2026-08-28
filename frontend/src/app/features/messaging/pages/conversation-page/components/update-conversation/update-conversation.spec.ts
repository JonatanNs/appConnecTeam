import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateConversation } from './update-conversation';

describe('UpdateConversation', () => {
  let component: UpdateConversation;
  let fixture: ComponentFixture<UpdateConversation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateConversation],
    }).compileComponents();

    fixture = TestBed.createComponent(UpdateConversation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
