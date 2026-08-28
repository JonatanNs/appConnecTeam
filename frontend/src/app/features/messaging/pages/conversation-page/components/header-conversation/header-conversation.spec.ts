import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderConversation } from './header-conversation';

describe('HeaderConversation', () => {
  let component: HeaderConversation;
  let fixture: ComponentFixture<HeaderConversation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderConversation],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderConversation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
