import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Prodotto } from '../../models/prodotto.model';
import { ProdottoService } from '../../services/prodotto.service';
import { CarrelloService } from '../../services/carrello.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-prodotto-dettaglio',
  templateUrl: './prodotto-dettaglio.component.html',
  styleUrls: ['./prodotto-dettaglio.component.css']
})
export class ProdottoDettaglioComponent implements OnInit {
  prodotto: Prodotto | null = null;
  quantita: number = 1;
  loading: boolean = false;
  error: string | null = null;
  successMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private prodottoService: ProdottoService,
    private carrelloService: CarrelloService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idProdotto = Number(params.get('id'));
      if (idProdotto) {
        this.loadProdotto(idProdotto);
      }
    });
  }

  loadProdotto(id: number): void {
    this.loading = true;
    this.prodottoService.getProdottoById(id)
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
  }

  aggiungiAlCarrello(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    if (!this.prodotto) return;

    const user = this.authService.getCurrentUser();
    if (!user) return;

    this.loading = true;
    this.carrelloService.getCarrelloByUtente(user.idUtente)
      .subscribe({
        next: (carrello) => {
          if (carrello && this.prodotto) {
            this.carrelloService.aggiungiProdotto(carrello.idCarrello, this.prodotto.idProdotto, this.quantita)
              .subscribe({
                next: () => {
                  this.successMessage = 'Prodotto aggiunto al carrello con successo!';
                  this.loading = false;
                  setTimeout(() => this.successMessage = null, 3000);
                },
                error: (err) => {
                  this.error = 'Errore nell\'aggiunta del prodotto al carrello. Riprova più tardi.';
                  this.loading = false;
                  console.error('Errore nell\'aggiunta del prodotto al carrello:', err);
                }
              });
          }
        },
        error: (err) => {
          this.error = 'Errore nel recupero del carrello. Riprova più tardi.';
          this.loading = false;
          console.error('Errore nel recupero del carrello:', err);
        }
      });
  }
}
