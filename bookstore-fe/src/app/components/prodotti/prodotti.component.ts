import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Prodotto } from '../../models/prodotto.model';
import { ProdottoService } from '../../services/prodotto.service';
import { CarrelloService } from '../../services/carrello.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-prodotti',
  templateUrl: './prodotti.component.html',
  styleUrl: './prodotti.component.css',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink]
})
export class ProdottiComponent implements OnInit {
  prodotti: Prodotto[] = [];
  searchTerm: string = '';
  loading: boolean = false;
  error: string | null = null;

  constructor(
    private prodottoService: ProdottoService,
    private carrelloService: CarrelloService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadProdotti();
  }

  loadProdotti(): void {
    this.loading = true;
    this.prodottoService.getAllProdotti()
      .subscribe({
        next: (data) => {
          this.prodotti = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Errore nel caricamento dei prodotti. Riprova più tardi.';
          this.loading = false;
          console.error('Errore nel caricamento dei prodotti:', err);
        }
      });
  }

  searchProdotti(): void {
    if (!this.searchTerm.trim()) {
      this.loadProdotti();
      return;
    }

    this.loading = true;
    this.prodottoService.searchByTitolo(this.searchTerm)
      .subscribe({
        next: (data) => {
          this.prodotti = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Errore nella ricerca dei prodotti. Riprova più tardi.';
          this.loading = false;
          console.error('Errore nella ricerca dei prodotti:', err);
        }
      });
  }

  aggiungiAlCarrello(idProdotto: number): void {
    // Verifica se l'utente è autenticato
    if (!this.authService.isLoggedIn()) {
      this.error = 'Devi effettuare il login per aggiungere prodotti al carrello';
      setTimeout(() => this.error = null, 3000); // Rimuovi il messaggio dopo 3 secondi
      return;
    }

    this.carrelloService.aggiungiProdotto(idProdotto)
      .subscribe({
        next: (response) => {
          // Mostra un messaggio di successo
          this.error = null;
          alert('Prodotto aggiunto al carrello!');
        },
        error: (err) => {
          console.error('Errore nell\'aggiunta del prodotto al carrello:', err);
          this.error = 'Errore nell\'aggiunta del prodotto al carrello. Riprova più tardi.';
          setTimeout(() => this.error = null, 3000); // Rimuovi il messaggio dopo 3 secondi
        }
      });
  }
}
