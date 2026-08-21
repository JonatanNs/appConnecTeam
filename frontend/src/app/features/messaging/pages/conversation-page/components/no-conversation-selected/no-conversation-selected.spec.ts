import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoConversationSelected } from './no-conversation-selected';

describe('NoConversationSelected', () => {
  let component: NoConversationSelected;
  let fixture: ComponentFixture<NoConversationSelected>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NoConversationSelected],
    }).compileComponents();

    fixture = TestBed.createComponent(NoConversationSelected);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
