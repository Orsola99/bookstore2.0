import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';
import { CarrelloService } from '../../../services/carrello.service';
import { Utente } from '../../../models/utente.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule]
})
export class HeaderComponent implements OnInit {
  currentUser: Utente | null = null;
  isLoggedIn = false;
  numeroProdottiCarrello: number = 0;
  private subscriptions: Subscription[] = [];

  constructor(
    private authService: AuthService,
    private carrelloService: CarrelloService
  ) {}

  ngOnInit(): void {
    // Sottoscrizione all'observable dell'utente corrente
    const userSub = this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      this.isLoggedIn = !!user;
      
      // Se l'utente è autenticato, carica il carrello
      if (user && user.idUtente) {
        this.caricaCarrello(user.idUtente);
      } else {
        this.numeroProdottiCarrello = 0;
      }
    });
    
    // Sottoscrizione alle notifiche di aggiornamento del carrello
    const carrelloSub = this.carrelloService.carrelloAggiornato$.subscribe(() => {
      if (this.currentUser && this.currentUser.idUtente) {
        this.caricaCarrello(this.currentUser.idUtente);
      }
    });
    
    this.subscriptions.push(userSub);
    this.subscriptions.push(carrelloSub);
  }
  
  ngOnDestroy(): void {
    // Pulizia delle sottoscrizioni per evitare memory leak
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
  
  private caricaCarrello(idUtente: number): void {
    const carrelloSub = this.carrelloService.getCarrelloByUtente(idUtente)
      .subscribe({
        next: (response) => {
          if (response && response.success && response.carrello && response.carrello.dettagli) {
            this.numeroProdottiCarrello = response.carrello.dettagli.length;
          } else {
            this.numeroProdottiCarrello = 0;
          }
        },
        error: (err) => {
          console.error('Errore nel caricamento del carrello:', err);
          this.numeroProdottiCarrello = 0;
        }
      });
    
    this.subscriptions.push(carrelloSub);
  }

  logout(): void {
    this.authService.logout();
  }
}
