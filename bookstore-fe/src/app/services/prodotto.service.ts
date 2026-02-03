import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Prodotto } from '../models/prodotto.model';

@Injectable({
  providedIn: 'root'
})
export class ProdottoService {
  private apiUrl = 'http://localhost:8080/api/prodotti';

  constructor(private http: HttpClient) { }

  getAllProdotti(): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(this.apiUrl);
  }

  getProdottoById(id: number): Observable<Prodotto> {
    return this.http.get<Prodotto>(`${this.apiUrl}/${id}`);
  }

  getProdottiInEvidenza(limit: number = 4): Observable<Prodotto[]> {
    // Utilizziamo l'endpoint principale e limitiamo il numero di risultati
    return this.http.get<Prodotto[]>(`${this.apiUrl}?limit=${limit}`);
  }

  searchByTitolo(titolo: string): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/search/titolo?titolo=${titolo}`);
  }

  searchBySottotitolo(sottotitolo: string): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/search/sottotitolo?sottotitolo=${sottotitolo}`);
  }

  searchByPrezzoRange(min: number, max: number): Observable<Prodotto[]> {
    return this.http.get<Prodotto[]>(`${this.apiUrl}/search/prezzo?min=${min}&max=${max}`);
  }
}
