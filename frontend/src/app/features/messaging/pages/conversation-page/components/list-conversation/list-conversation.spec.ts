import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListConversation } from './list-conversation';

describe('ListConversation', () => {
  let component: ListConversation;
  let fixture: ComponentFixture<ListConversation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListConversation],
    }).compileComponents();

    fixture = TestBed.createComponent(ListConversation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
