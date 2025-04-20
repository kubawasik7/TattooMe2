import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TattooArtistListComponent } from './tattoo-artist-list.component';

describe('TattooArtistListComponent', () => {
  let component: TattooArtistListComponent;
  let fixture: ComponentFixture<TattooArtistListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TattooArtistListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TattooArtistListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
