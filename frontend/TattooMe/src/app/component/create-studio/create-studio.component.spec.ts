import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateStudioComponent } from './create-studio.component';

describe('CreateStudioComponent', () => {
  let component: CreateStudioComponent;
  let fixture: ComponentFixture<CreateStudioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateStudioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateStudioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
