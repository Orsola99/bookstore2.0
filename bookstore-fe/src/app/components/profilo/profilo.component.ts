import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

import { AuthService } from '../../services/auth.service';
import { IndirizzoService } from '../../services/indirizzo.service';
import { Utente } from '../../models/utente.model';
import { Indirizzo } from '../../models/indirizzo.model';

@Component({
  selector: 'app-profilo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profilo.component.html',
  styleUrl: './profilo.component.css'
})
export class ProfiloComponent implements OnInit, OnDestroy {
  currentUser: Utente | null = null;
  indirizzi: Indirizzo[] = [];
  loading = false;
  error: string | null = null;
  successMessage: string | null = null;

  mostraFormIndirizzo = false;
  indirizzoSelezionato: Indirizzo | null = null;
  nuovoIndirizzo: any = {
    tipo: 1,
    via: '',
    cap: '',
    citta: '',
    provincia: '',
    nazione: '',
    telefono: ''
  };
  azioneIndirizzo = 'aggiungi'; // 'aggiungi' o 'modifica'
  
  private subscriptions: Subscription[] = [];

  constructor(
    private authService: AuthService,
    private indirizzoService: IndirizzoService
  ) {}

  ngOnInit(): void {
    this.loading = true;
    
    // Ottieni l'utente corrente
    const userSub = this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      
      if (user && user.idUtente) {
        this.caricaIndirizzi(user.idUtente);
      } else {
        this.loading = false;
        this.error = 'Utente non autenticato';
      }
    });
    
    this.subscriptions.push(userSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  caricaIndirizzi(utenteId: number): void {
    this.loading = true;
    const indirizziSub = this.indirizzoService.getIndirizziByUtente(utenteId).subscribe({
      next: (indirizzi) => {
        console.log('Indirizzi caricati:', indirizzi);
        this.indirizzi = indirizzi || [];
        this.loading = false;
        
        
        if (this.indirizzi.length === 0) {
          this.successMessage = 'Nessun indirizzo trovato. Aggiungi il tuo primo indirizzo!';
          setTimeout(() => this.successMessage = null, 3000);
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento degli indirizzi:', err);
        this.error = 'Errore nel caricamento degli indirizzi. Riprova più tardi.';
        this.loading = false;
        this.indirizzi = [];
        setTimeout(() => this.error = null, 3000);
      }
    });
    
    this.subscriptions.push(indirizziSub);
  }

  getTipoIndirizzo(tipo: number): string {
    switch (tipo) {
      case 1: return 'Spedizione';
      case 2: return 'Fatturazione';
      case 3: return 'Entrambi';
      default: return 'Altro';
    }
  }

  mostraAggiungiIndirizzo(): void {
    this.azioneIndirizzo = 'aggiungi';
    this.indirizzoSelezionato = null;
    this.nuovoIndirizzo = {
      tipo: 1,
      via: '',
      cap: '',
      citta: '',
      provincia: '',
      nazione: '',
      telefono: ''
    };
    this.mostraFormIndirizzo = true;
  }

  mostraModificaIndirizzo(indirizzo: Indirizzo): void {
    this.azioneIndirizzo = 'modifica';
    this.indirizzoSelezionato = indirizzo;
    this.nuovoIndirizzo = {
      idIndirizzo: indirizzo.idIndirizzo,
      tipo: indirizzo.tipo,
      via: indirizzo.via,
      cap: indirizzo.cap,
      citta: indirizzo.citta,
      provincia: indirizzo.provincia,
      nazione: indirizzo.nazione,
      telefono: indirizzo.telefono
    };
    this.mostraFormIndirizzo = true;
  }

  annullaFormIndirizzo(): void {
    this.mostraFormIndirizzo = false;
    this.indirizzoSelezionato = null;
  }

  salvaIndirizzo(): void {
    if (!this.currentUser || !this.currentUser.idUtente) {
      this.error = 'Utente non autenticato';
      return;
    }

    if (!this.nuovoIndirizzo.via || !this.nuovoIndirizzo.cap || !this.nuovoIndirizzo.citta || 
        !this.nuovoIndirizzo.provincia || !this.nuovoIndirizzo.nazione || !this.nuovoIndirizzo.tipo) {
      this.error = 'Tutti i campi sono obbligatori';
      setTimeout(() => this.error = null, 3000);
      return;
    }

    this.loading = true;

    const indirizzo: any = {
      ...this.nuovoIndirizzo,
      utente: { idUtente: this.currentUser.idUtente },
      tipo: Number(this.nuovoIndirizzo.tipo)
    };
    
    console.log('Dati indirizzo da salvare:', indirizzo);
    
    let operazione;
    
    if (this.azioneIndirizzo === 'aggiungi') {
      operazione = this.indirizzoService.addIndirizzo(indirizzo);
    } else {
      operazione = this.indirizzoService.updateIndirizzo(indirizzo);
    }
    
    const sub = operazione.subscribe({
      next: (indirizzoSalvato) => {
        console.log('Indirizzo salvato:', indirizzoSalvato);
        this.loading = false;
        this.successMessage = this.azioneIndirizzo === 'aggiungi' 
          ? 'Indirizzo aggiunto con successo' 
          : 'Indirizzo modificato con successo';
        this.mostraFormIndirizzo = false;

        if (this.currentUser && this.currentUser.idUtente) {
          this.caricaIndirizzi(this.currentUser.idUtente);
        }

        setTimeout(() => this.successMessage = null, 3000);
      },
      error: (err) => {
        console.error('Errore durante il salvataggio dell\'indirizzo:', err);

        if (err.status === 400) {
          this.error = 'Dati indirizzo non validi. Verifica che tutti i campi siano compilati correttamente.';
        } else if (err.status === 500) {
          this.error = 'Errore del server durante il salvataggio dell\'indirizzo. Riprova più tardi.';
        } else {
          this.error = 'Errore durante il salvataggio dell\'indirizzo';
        }
        
        this.loading = false;

        setTimeout(() => this.error = null, 3000);
      }
    });
    
    this.subscriptions.push(sub);
  }

  eliminaIndirizzo(idIndirizzo: number): void {
    if (confirm('Sei sicuro di voler eliminare questo indirizzo?')) {
      this.loading = true;
      
      const sub = this.indirizzoService.deleteIndirizzo(idIndirizzo).subscribe({
        next: () => {
          this.loading = false;
          this.successMessage = 'Indirizzo eliminato con successo';

          if (this.currentUser && this.currentUser.idUtente) {
            this.caricaIndirizzi(this.currentUser.idUtente);
          }

          setTimeout(() => this.successMessage = null, 3000);
        },
        error: (err) => {
          console.error('Errore durante l\'eliminazione dell\'indirizzo:', err);
          
          if (err.status === 404) {
            this.error = 'Indirizzo non trovato. Potrebbe essere già stato eliminato.';
          } else if (err.status === 500) {
            this.error = 'Errore del server durante l\'eliminazione dell\'indirizzo. Riprova più tardi.';
          } else {
            this.error = 'Errore durante l\'eliminazione dell\'indirizzo';
          }
          
          this.loading = false;

          setTimeout(() => this.error = null, 3000);
        }
      });
      
      this.subscriptions.push(sub);
    }
  }
}
