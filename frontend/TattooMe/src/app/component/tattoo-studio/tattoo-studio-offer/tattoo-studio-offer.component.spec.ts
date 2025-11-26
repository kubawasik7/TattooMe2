import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TattooStudioOfferComponent } from './tattoo-studio-offer.component';

describe('TattooStudioOfferComponent', () => {
  let component: TattooStudioOfferComponent;
  let fixture: ComponentFixture<TattooStudioOfferComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TattooStudioOfferComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TattooStudioOfferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
