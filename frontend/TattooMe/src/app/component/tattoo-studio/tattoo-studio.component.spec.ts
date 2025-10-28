import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TattooStudioComponent } from './tattoo-studio.component';

describe('TattooStudioComponent', () => {
  let component: TattooStudioComponent;
  let fixture: ComponentFixture<TattooStudioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TattooStudioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TattooStudioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
