import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Carrello } from '../../models/carrello.model';
import { CarrelloDettaglio } from '../../models/carrello-dettaglio.model';
import { Indirizzo } from '../../models/indirizzo.model';
import { CarrelloService } from '../../services/carrello.service';
import { AuthService } from '../../services/auth.service';
import { IndirizzoService } from '../../services/indirizzo.service';
import { OrdineService } from '../../services/ordine.service';

@Component({
  selector: 'app-carrello',
  templateUrl: './carrello.component.html',
  styleUrl: './carrello.component.css',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule]
})
export class CarrelloComponent implements OnInit, OnDestroy {
  carrello: Carrello | null = null;
  dettagli: CarrelloDettaglio[] = [];
  totale: number = 0;
  loading: boolean = false;
  error: string | null = null;
  successMessage: string | null = null;
  
  // Proprietà per la creazione dell'ordine
  indirizzi: Indirizzo[] = [];
  mostraModalOrdine: boolean = false;
  indirizzoSpedizioneId: number | null = null;
  indirizzoFatturazioneId: number | null = null;
  creazioneOrdineInCorso: boolean = false;
  
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private carrelloService: CarrelloService,
    private authService: AuthService,
    private indirizzoService: IndirizzoService,
    private ordineService: OrdineService
  ) { }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/carrello' } });
      return;
    }
    
    this.loadCarrello();
  }

  ngOnDestroy(): void {
    // Pulizia delle sottoscrizioni per evitare memory leak
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadCarrello(): void {
    const user = this.authService.getCurrentUser();
    if (!user || user.idUtente === undefined) return;

    this.loading = true;
    const carrelloSub = this.carrelloService.getCarrelloByUtente(user.idUtente)
      .subscribe({
        next: (response) => {
          if (response && response.success && response.carrello) {
            this.carrello = response.carrello;
            this.dettagli = response.carrello.dettagli || [];
            this.totale = response.carrello.totale;
            this.loading = false;
          } else {
            this.error = 'Dati del carrello non validi';
            this.loading = false;
          }
        },
        error: (err) => {
          this.error = 'Errore nel caricamento del carrello. Riprova più tardi.';
          this.loading = false;
          console.error('Errore nel caricamento del carrello:', err);
        }
      });
    
    this.subscriptions.push(carrelloSub);
  }

  aggiornaQuantita(dettaglio: CarrelloDettaglio, quantita: number): void {
    if (!this.carrello) return;
    
    this.loading = true;
    this.carrelloService.aggiornaQuantita(this.carrello.idUtente, dettaglio.idCarrelloDettagli, quantita)
      .subscribe({
        next: (response) => {
          if (response && response.success && response.carrello) {
            this.carrello = response.carrello;
            this.dettagli = response.carrello.dettagli || [];
            this.totale = response.carrello.totale;
            this.loading = false;
            this.successMessage = 'Quantità aggiornata con successo!';
            setTimeout(() => this.successMessage = null, 3000);
          } else {
            this.error = 'Errore nell\'aggiornamento della quantità.';
            this.loading = false;
          }
        },
        error: (err) => {
          this.error = 'Errore nell\'aggiornamento della quantità. Riprova più tardi.';
          this.loading = false;
          console.error('Errore nell\'aggiornamento della quantità:', err);
        }
      });
  }

  rimuoviProdotto(dettaglio: CarrelloDettaglio): void {
    if (!this.carrello) return;
    
    this.loading = true;
    this.carrelloService.rimuoviProdotto(this.carrello.idUtente, dettaglio.idProdotto)
      .subscribe({
        next: (response) => {
          if (response && response.success && response.carrello) {
            this.carrello = response.carrello;
            this.dettagli = response.carrello.dettagli || [];
            this.totale = response.carrello.totale;
            this.loading = false;
            this.successMessage = 'Prodotto rimosso dal carrello con successo!';
            setTimeout(() => this.successMessage = null, 3000);
          } else {
            this.dettagli = this.dettagli.filter(d => d.idCarrelloDettagli !== dettaglio.idCarrelloDettagli);
            this.loading = false;
            this.successMessage = 'Prodotto rimosso dal carrello con successo!';
            setTimeout(() => this.successMessage = null, 3000);
          }
        },
        error: (err) => {
          this.error = 'Errore nella rimozione del prodotto. Riprova più tardi.';
          this.loading = false;
          console.error('Errore nella rimozione del prodotto:', err);
        }
      });
  }

  svuotaCarrello(): void {
    if (!this.carrello) return;
    
    if (confirm('Sei sicuro di voler svuotare il carrello?')) {
      this.loading = true;
      this.carrelloService.svuotaCarrello(this.carrello.idUtente)
        .subscribe({
          next: (response) => {
            if (response && response.success) {
              this.dettagli = [];
              this.totale = 0;
              this.loading = false;
              this.successMessage = 'Carrello svuotato con successo!';
              setTimeout(() => this.successMessage = null, 3000);
            } else {
              this.error = 'Errore nello svuotamento del carrello.';
              this.loading = false;
            }
          },
          error: (err) => {
            this.error = 'Errore nello svuotamento del carrello. Riprova più tardi.';
            this.loading = false;
            console.error('Errore nello svuotamento del carrello:', err);
          }
        });
    }
  }

  calcolaTotale(): void {
    // Non è più necessario calcolare il totale manualmente, viene fornito dal backend
    // Questo metodo è mantenuto per compatibilità con il codice esistente
    if (this.carrello && this.carrello.totale) {
      this.totale = this.carrello.totale;
    } else {
      this.totale = this.dettagli.reduce((sum, item) => {
        return sum + (item.prezzo * item.quantita);
      }, 0);
    }
  }

  procediAllOrdine(): void {
    if (!this.carrello || this.dettagli.length === 0) return;
    
    const user = this.authService.getCurrentUser();
    if (!user || !user.idUtente) {
      this.error = 'Utente non autenticato';
      setTimeout(() => this.error = null, 3000);
      return;
    }
    
    // Carica gli indirizzi dell'utente
    this.loading = true;
    this.indirizzoService.getIndirizziByUtente(user.idUtente).subscribe({
      next: (indirizzi) => {
        this.indirizzi = indirizzi;
        this.loading = false;
        
        if (this.indirizzi.length === 0) {
          this.error = 'Non hai indirizzi salvati. Aggiungi almeno un indirizzo nel tuo profilo.';
          setTimeout(() => this.error = null, 5000);
          return;
        }
        
        // Imposta gli indirizzi predefiniti se disponibili
        const indirizzoSpedizione = this.indirizzi.find(i => i.tipo === 1 || i.tipo === 3);
        const indirizzoFatturazione = this.indirizzi.find(i => i.tipo === 2 || i.tipo === 3);
        
        if (indirizzoSpedizione) {
          this.indirizzoSpedizioneId = indirizzoSpedizione.idIndirizzo;
        } else {
          this.indirizzoSpedizioneId = this.indirizzi[0].idIndirizzo;
        }
        
        if (indirizzoFatturazione) {
          this.indirizzoFatturazioneId = indirizzoFatturazione.idIndirizzo;
        } else {
          this.indirizzoFatturazioneId = this.indirizzi[0].idIndirizzo;
        }
        
        // Mostra il modale per la selezione degli indirizzi
        this.mostraModalOrdine = true;
      },
      error: (err) => {
        console.error('Errore nel caricamento degli indirizzi:', err);
        this.error = 'Errore nel caricamento degli indirizzi. Riprova più tardi.';
        this.loading = false;
        setTimeout(() => this.error = null, 3000);
      }
    });
  }
  
  confermaOrdine(): void {
    if (!this.carrello || !this.indirizzoSpedizioneId || !this.indirizzoFatturazioneId) {
      this.error = 'Seleziona gli indirizzi per procedere';
      setTimeout(() => this.error = null, 3000);
      return;
    }
    
    const user = this.authService.getCurrentUser();
    if (!user || !user.idUtente) {
      this.error = 'Utente non autenticato';
      setTimeout(() => this.error = null, 3000);
      return;
    }
    
    this.creazioneOrdineInCorso = true;
    this.loading = true;
    
    this.ordineService.createOrdine(
      user.idUtente,
      this.indirizzoSpedizioneId,
      this.indirizzoFatturazioneId
    ).subscribe({
      next: (response) => {
        if (response && response.success) {
          // Svuota il carrello dopo aver effettuato l'ordine
          const user = this.authService.getCurrentUser();
          if (user && user.idUtente) {
            this.carrelloService.svuotaCarrello(user.idUtente).subscribe({
              next: () => {
                console.log('Carrello svuotato con successo dopo la creazione dell\'ordine');
                this.dettagli = [];
                this.totale = 0;
                this.carrello = null;
              },
              error: (err) => {
                console.error('Errore nello svuotamento del carrello dopo la creazione dell\'ordine:', err);
              }
            });
          }
          
          this.successMessage = 'Ordine creato con successo! Grazie per il tuo acquisto.';
          this.mostraModalOrdine = false;
          this.creazioneOrdineInCorso = false;
          this.loading = false;
          
          // Reindirizza alla pagina degli ordini dopo 3 secondi
          setTimeout(() => {
            this.router.navigate(['/ordini']);
          }, 3000);
        } else {
          this.error = 'Errore nella creazione dell\'ordine';
          this.creazioneOrdineInCorso = false;
          this.loading = false;
          setTimeout(() => this.error = null, 3000);
        }
      },
      error: (err) => {
        console.error('Errore nella creazione dell\'ordine:', err);
        this.error = 'Errore nella creazione dell\'ordine. Riprova più tardi.';
        this.creazioneOrdineInCorso = false;
        this.loading = false;
        this.mostraModalOrdine = false;
        setTimeout(() => this.error = null, 3000);
      }
    });
  }
  
  annullaOrdine(): void {
    this.mostraModalOrdine = false;
    this.indirizzoSpedizioneId = null;
    this.indirizzoFatturazioneId = null;
  }
  
  getTipoIndirizzo(tipo: number): string {
    switch (tipo) {
      case 1: return 'Spedizione';
      case 2: return 'Fatturazione';
      case 3: return 'Entrambi';
      default: return 'Altro';
    }
  }
}
