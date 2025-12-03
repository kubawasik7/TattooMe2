import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TattooStudioVisitComponent } from './tattoo-studio-visit.component';

describe('TattooStudioVisitComponent', () => {
  let component: TattooStudioVisitComponent;
  let fixture: ComponentFixture<TattooStudioVisitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TattooStudioVisitComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TattooStudioVisitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
