import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProdottoDettaglioComponent } from './prodotto-dettaglio.component';

describe('ProdottoDettaglioComponent', () => {
  let component: ProdottoDettaglioComponent;
  let fixture: ComponentFixture<ProdottoDettaglioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProdottoDettaglioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProdottoDettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
