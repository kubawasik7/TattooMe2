import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkHourMiniComponent } from './work-hour-mini.component';

describe('WorkHourMiniComponent', () => {
  let component: WorkHourMiniComponent;
  let fixture: ComponentFixture<WorkHourMiniComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WorkHourMiniComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkHourMiniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
