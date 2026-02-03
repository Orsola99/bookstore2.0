import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, catchError, of } from 'rxjs';
import { Indirizzo } from '../models/indirizzo.model';

@Injectable({
  providedIn: 'root'
})
export class IndirizzoService {
  private apiUrl = 'http://localhost:8080/api/indirizzi';

  constructor(private http: HttpClient) { }

  getIndirizziByUtente(utenteId: number): Observable<Indirizzo[]> {
    console.log('Caricamento indirizzi per utente:', utenteId);
    
    // Il backend ora restituisce direttamente la lista di indirizzi
    return this.http.get<Indirizzo[]>(`${this.apiUrl}/utente/${utenteId}`)
      .pipe(
        map(indirizzi => {
          console.log('Indirizzi caricati dal backend:', indirizzi);
          return indirizzi || [];
        }),
        catchError(error => {
          console.error('Errore nella richiesta HTTP:', error);
          // In caso di errore, ritorniamo un array vuoto
          return of([]);
        })
      );
  }

  // Metodo privato per ottenere gli indirizzi dal backend in caso di errore di parsing
  private getIndirizziFromBackend(utenteId: number): Observable<Indirizzo[]> {
    console.log('Utilizzo metodo alternativo per recuperare gli indirizzi');
    
    // Per simulare il recupero degli indirizzi, creiamo alcuni indirizzi di esempio
    // Questo è solo temporaneo finché non risolviamo il problema nel backend
    const indirizziEsempio: Indirizzo[] = [
      {
        idIndirizzo: 1,
        utente: { idUtente: utenteId } as any,
        tipo: 1,
        via: 'Via Roma 123',
        cap: '00100',
        citta: 'Roma',
        provincia: 'RM',
        nazione: 'Italia',
        telefono: '0612345678'
      },
      {
        idIndirizzo: 2,
        utente: { idUtente: utenteId } as any,
        tipo: 2,
        via: 'Via Milano 456',
        cap: '20100',
        citta: 'Milano',
        provincia: 'MI',
        nazione: 'Italia',
        telefono: '0287654321'
      }
    ];
    
    // In un'implementazione reale, dovresti fare una richiesta al backend
    // per recuperare gli indirizzi dell'utente
    return of(indirizziEsempio);
  }
  
  getIndirizzo(id: number): Observable<Indirizzo> {
    // Il backend ora restituisce direttamente l'indirizzo
    return this.http.get<Indirizzo>(`${this.apiUrl}/${id}`)
      .pipe(
        map(indirizzo => {
          console.log('Indirizzo recuperato:', indirizzo);
          return indirizzo;
        }),
        catchError(error => {
          console.error('Errore nel recupero dell\'indirizzo:', error);
          throw error;
        })
      );
  }

  addIndirizzo(indirizzo: Indirizzo): Observable<Indirizzo> {
    console.log('Invio richiesta di creazione indirizzo:', indirizzo);
    
    // Creiamo un oggetto con la struttura corretta per il backend
    const requestBody = {
      utenteId: indirizzo.utente.idUtente,
      via: indirizzo.via,
      cap: indirizzo.cap,
      citta: indirizzo.citta,
      provincia: indirizzo.provincia,
      nazione: indirizzo.nazione,
      telefono: indirizzo.telefono,
      tipo: indirizzo.tipo
    };
    
    // Il backend ora restituisce direttamente l'indirizzo creato
    return this.http.post<Indirizzo>(`${this.apiUrl}`, requestBody)
      .pipe(
        map(indirizzoCreato => {
          console.log('Indirizzo creato:', indirizzoCreato);
          return indirizzoCreato;
        }),
        catchError(error => {
          console.error('Errore nella richiesta HTTP:', error);
          throw error;
        })
      );
  }

  updateIndirizzo(indirizzo: Indirizzo): Observable<Indirizzo> {
    console.log('Aggiornamento indirizzo:', indirizzo);
    
    return this.http.put<Indirizzo>(`${this.apiUrl}/${indirizzo.idIndirizzo}`, indirizzo)
      .pipe(
        map(indirizzoAggiornato => {
          console.log('Indirizzo aggiornato:', indirizzoAggiornato);
          return indirizzoAggiornato;
        }),
        catchError(error => {
          console.error('Errore nella richiesta HTTP:', error);
          throw error;
        })
      );
  }

  deleteIndirizzo(id: number): Observable<void> {
    console.log('Eliminazione indirizzo con id:', id);
    
    // Il backend ora restituisce una risposta vuota con status 200 in caso di successo
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        map(() => {
          console.log('Indirizzo eliminato con successo');
          return;
        }),
        catchError(error => {
          console.error('Errore nella richiesta HTTP:', error);
          throw error;
        })
      );
  }
}
