import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Prodotto } from '../../../models/prodotto.model';
import { ProdottoService } from '../../../services/prodotto.service';
import { CarrelloService } from '../../../services/carrello.service';

@Component({
  selector: 'app-prodotto-dettaglio',
  templateUrl: './prodotto-dettaglio.component.html',
  styleUrls: ['./prodotto-dettaglio.component.css'],
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule]
})
export class ProdottoDettaglioComponent implements OnInit {
  prodotto: Prodotto | null = null;
  quantita: number = 1;
  loading: boolean = false;
  error: string | null = null;
  addingToCart: boolean = false;
  successMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private prodottoService: ProdottoService,
    private carrelloService: CarrelloService
  ) {}

  ngOnInit(): void {
    this.loading = true;
    const idProdotto = this.route.snapshot.paramMap.get('id');
    
    if (idProdotto) {
      this.prodottoService.getProdottoById(+idProdotto)
        .subscribe({
          next: (data) => {
            this.prodotto = data;
            this.loading = false;
          },
          error: (err) => {
            this.error = 'Errore nel caricamento del prodotto. Riprova più tardi.';
            this.loading = false;
            console.error('Errore nel caricamento del prodotto:', err);
          }
        });
    } else {
      this.error = 'ID prodotto non valido';
      this.loading = false;
    }
  }

  aggiungiAlCarrello(): void {
    if (!this.prodotto) return;
    
    this.addingToCart = true;
    this.successMessage = null;
    
    this.carrelloService.aggiungiProdotto(this.prodotto.idProdotto, this.quantita)
      .subscribe({
        next: (response) => {
          this.addingToCart = false;
          this.successMessage = 'Prodotto aggiunto al carrello con successo!';
          setTimeout(() => this.successMessage = null, 3000);
        },
        error: (err) => {
          this.error = 'Errore nell\'aggiunta al carrello. Riprova più tardi.';
          this.addingToCart = false;
          console.error('Errore nell\'aggiunta al carrello:', err);
        }
      });
  }

  incrementaQuantita(): void {
    this.quantita++;
  }

  decrementaQuantita(): void {
    if (this.quantita > 1) {
      this.quantita--;
    }
  }

  tornaAiProdotti(): void {
    this.router.navigate(['/prodotti']);
  }
}
