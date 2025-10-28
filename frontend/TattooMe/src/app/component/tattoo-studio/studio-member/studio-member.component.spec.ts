import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudioMemberComponent } from './studio-member.component';

describe('StudioMemberComponent', () => {
  let component: StudioMemberComponent;
  let fixture: ComponentFixture<StudioMemberComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StudioMemberComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudioMemberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
