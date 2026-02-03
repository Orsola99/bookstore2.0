import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, switchMap, catchError, Subject } from 'rxjs';
import { Carrello } from '../models/carrello.model';
import { CarrelloDettaglio } from '../models/carrello-dettaglio.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CarrelloService {
  private apiUrl = 'http://localhost:8080/api/carrello';
  
  // Subject per notificare i cambiamenti al carrello
  private carrelloAggiornato = new Subject<void>();
  
  // Observable pubblico che i componenti possono sottoscrivere
  carrelloAggiornato$ = this.carrelloAggiornato.asObservable();

  constructor(private http: HttpClient, private authService: AuthService) { }

  getCarrelloByUtente(idUtente: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${idUtente}`);
  }

  aggiungiProdotto(idProdotto: number, quantita: number = 1): Observable<any> {
    // Ottieni l'utente corrente
    const currentUser = this.authService.getCurrentUser();
    
    if (!currentUser) {
      return of(null).pipe(
        switchMap(() => {
          throw new Error('Utente non autenticato');
        })
      ) as Observable<any>;
    }
    
    // Verifica che idUtente sia definito
    if (currentUser.idUtente === undefined) {
      return of(null).pipe(
        switchMap(() => {
          throw new Error('ID utente non disponibile');
        })
      ) as Observable<any>;
    }
    
    // Aggiungi direttamente il prodotto al carrello dell'utente
    return this.http.post<any>(`${this.apiUrl}/${currentUser.idUtente}/aggiungi`, {
      prodottoId: idProdotto,
      quantita
    }).pipe(
      catchError(error => {
        console.error('Errore nell\'aggiunta del prodotto al carrello:', error);
        throw error;
      }),
      switchMap(response => {
        // Notifica che il carrello è stato aggiornato
        this.carrelloAggiornato.next();
        return of(response);
      })
    );
  }
  
  // Metodo per rimuovere un prodotto dal carrello
  rimuoviProdotto(idUtente: number, idProdotto: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${idUtente}/rimuovi/${idProdotto}`).pipe(
      switchMap(response => {
        // Notifica che il carrello è stato aggiornato
        this.carrelloAggiornato.next();
        return of(response);
      })
    );
  }

  // Metodo per aggiornare la quantità di un prodotto nel carrello
  aggiornaQuantita(idUtente: number, dettaglioId: number, quantita: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${idUtente}/aggiorna`, {
      dettaglioId,
      quantita
    }).pipe(
      switchMap(response => {
        // Notifica che il carrello è stato aggiornato
        this.carrelloAggiornato.next();
        return of(response);
      })
    );
  }

  // Metodo per svuotare il carrello
  svuotaCarrello(idUtente: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${idUtente}/svuota`).pipe(
      switchMap(response => {
        // Notifica che il carrello è stato aggiornato
        this.carrelloAggiornato.next();
        return of(response);
      })
    );
  }
}
