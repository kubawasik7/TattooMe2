import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudioScheduleComponent } from './studio-schedule.component';

describe('StudioScheduleComponent', () => {
  let component: StudioScheduleComponent;
  let fixture: ComponentFixture<StudioScheduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StudioScheduleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudioScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
