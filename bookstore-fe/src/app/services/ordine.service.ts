import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of, catchError, map } from 'rxjs';
import { Ordine } from '../models/ordine.model';

@Injectable({
  providedIn: 'root'
})
export class OrdineService {
  private apiUrl = 'http://localhost:8080/api/ordini';

  constructor(private http: HttpClient) { }

  getOrdiniByUtente(idUtente: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/utente/${idUtente}`, { observe: 'response' })
      .pipe(
        map((response: HttpResponse<any>) => {
          if (response.status === 200) {
            return { success: true, ordini: response.body?.ordini || [] };
          } else {
            return { success: false, ordini: [] };
          }
        }),
        catchError(error => {
          console.error('Errore nel recupero degli ordini:', error);
          return of({ success: false, ordini: [], error: error.message });
        })
      );
  }

  getOrdineById(id: number): Observable<Ordine> {
    return this.http.get<Ordine>(`${this.apiUrl}/${id}`);
  }

  createOrdine(utenteId: number, indirizzoSpedizioneId: number, indirizzoFatturazioneId: number): Observable<any> {
    // Utilizziamo l'opzione {observe: 'response'} per ottenere l'intera risposta HTTP
    // e non solo il corpo della risposta
    return this.http.post(`${this.apiUrl}/crea`, {
      utenteId,
      indirizzoSpedizioneId,
      indirizzoFatturazioneId
    }, { observe: 'response' }).pipe(
      map(response => {
        // Se lo status è 201 (Created), consideriamo l'operazione riuscita
        // anche se c'è un errore di parsing
        if (response.status === 201) {
          return { success: true, message: 'Ordine creato con successo' };
        }
        throw new Error('Errore durante la creazione dell\'ordine');
      }),
      catchError(error => {
        // Se lo status è 201 ma c'è un errore di parsing, consideriamo comunque l'operazione riuscita
        if (error.status === 201) {
          return of({ success: true, message: 'Ordine creato con successo' });
        }
        throw error;
      })
    );
  }

  updateStatus(idOrdine: number, status: string): Observable<Ordine> {
    return this.http.put<Ordine>(`${this.apiUrl}/${idOrdine}/status`, { status });
  }

  getOrdiniByStatus(status: string): Observable<Ordine[]> {
    return this.http.get<Ordine[]>(`${this.apiUrl}/status/${status}`);
  }

  getOrdiniByDate(startDate: string, endDate: string): Observable<Ordine[]> {
    return this.http.get<Ordine[]>(`${this.apiUrl}/date?startDate=${startDate}&endDate=${endDate}`);
  }
}
