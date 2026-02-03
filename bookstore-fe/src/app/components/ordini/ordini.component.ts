import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { Ordine } from '../../models/ordine.model';
import { OrdineService } from '../../services/ordine.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-ordini',
  templateUrl: './ordini.component.html',
  styles: [],
  standalone: true,
  imports: [CommonModule, RouterLink]
})
export class OrdiniComponent implements OnInit {
  ordini: Ordine[] = [];
  loading: boolean = false;
  error: string | null = null;

  constructor(
    private router: Router,
    private ordineService: OrdineService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/ordini' } });
      return;
    }
    
    this.loadOrdini();
  }

  loadOrdini(): void {
    const user = this.authService.getCurrentUser();
    if (!user || !user.idUtente) return;

    this.loading = true;
    this.ordineService.getOrdiniByUtente(user.idUtente)
      .subscribe({
        next: (response) => {
          if (response && response.success && response.ordini) {
            this.ordini = response.ordini;
          } else {
            this.ordini = [];
          }
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Errore nel caricamento degli ordini. Riprova più tardi.';
          this.loading = false;
          console.error('Errore nel caricamento degli ordini:', err);
        }
      });
  }

  getStatusLabel(status: number): string {
    switch (status) {
      case 0:
        return 'Nuovo';
      case 1:
        return 'Elaborazione';
      case 2:
        return 'In Consegna';
      case 3:
        return 'Consegnato';
      case 4:
        return 'Annullato';
      default:
        return 'Sconosciuto';
    }
  }

  getStatusClass(status: number): string {
    switch (status) {
      case 0:
        return 'bg-info';
      case 1:
        return 'bg-primary';
      case 2:
        return 'bg-warning';
      case 3:
        return 'bg-success';
      case 4:
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }
}
