import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DirectoryDetail } from './directory-detail';

describe('DirectoryDetail', () => {
  let component: DirectoryDetail;
  let fixture: ComponentFixture<DirectoryDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DirectoryDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(DirectoryDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
