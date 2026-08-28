import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DockMobile } from './dock-mobile';

describe('DockMobile', () => {
  let component: DockMobile;
  let fixture: ComponentFixture<DockMobile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DockMobile],
    }).compileComponents();

    fixture = TestBed.createComponent(DockMobile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
