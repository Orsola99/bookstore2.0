import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Ordine } from '../../models/ordine.model';
import { OrdineService } from '../../services/ordine.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-ordine-dettaglio',
  templateUrl: './ordine-dettaglio.component.html',
  styleUrls: ['./ordine-dettaglio.component.css']
})
export class OrdineDettaglioComponent implements OnInit {
  ordine: Ordine | null = null;
  loading: boolean = false;
  error: string | null = null;
  totale: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private ordineService: OrdineService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.url } });
      return;
    }

    this.route.paramMap.subscribe(params => {
      const idOrdine = Number(params.get('id'));
      if (idOrdine) {
        this.loadOrdine(idOrdine);
      }
    });
  }

  loadOrdine(id: number): void {
    this.loading = true;
    this.ordineService.getOrdineById(id)
      .subscribe({
        next: (data) => {
          this.ordine = data;
          this.calcolaTotale();
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Errore nel caricamento dell\'ordine. Riprova più tardi.';
          this.loading = false;
          console.error('Errore nel caricamento dell\'ordine:', err);
        }
      });
  }

  calcolaTotale(): void {
    if (!this.ordine || !this.ordine.dettagli) return;
    
    this.totale = this.ordine.dettagli.reduce((sum, item) => {
      return sum + (item.prezzo * item.quantita);
    }, 0);
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'NUOVO':
        return 'Nuovo';
      case 'IN_LAVORAZIONE':
        return 'In lavorazione';
      case 'SPEDITO':
        return 'Spedito';
      case 'CONSEGNATO':
        return 'Consegnato';
      case 'ANNULLATO':
        return 'Annullato';
      default:
        return status;
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'NUOVO':
        return 'bg-info';
      case 'IN_LAVORAZIONE':
        return 'bg-primary';
      case 'SPEDITO':
        return 'bg-warning';
      case 'CONSEGNATO':
        return 'bg-success';
      case 'ANNULLATO':
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }
}
