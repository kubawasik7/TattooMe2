import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkHourComponent } from './work-hour.component';

describe('WorkHourComponent', () => {
  let component: WorkHourComponent;
  let fixture: ComponentFixture<WorkHourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WorkHourComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkHourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
