import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtistDateComponent } from './artist-date.component';

describe('ArtistDateComponent', () => {
  let component: ArtistDateComponent;
  let fixture: ComponentFixture<ArtistDateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArtistDateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArtistDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
