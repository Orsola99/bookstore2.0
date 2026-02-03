import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Utente } from '../models/utente.model';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/utenti';
  private currentUserSubject = new BehaviorSubject<Utente | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    
    // Controlla se c'è un utente salvato nel localStorage (solo nel browser)
    if (this.isBrowser) {
      const storedUser = localStorage.getItem('currentUser');
      if (storedUser) {
        this.currentUserSubject.next(JSON.parse(storedUser));
      }
    }
  }

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap(response => {
          if (response && response.utente) {
            if (this.isBrowser) {
              localStorage.setItem('currentUser', JSON.stringify(response.utente));
              // Se il backend non fornisce un token, possiamo usare un valore di default o l'ID utente
              localStorage.setItem('token', 'authenticated-' + response.utente.idUtente);
            }
            this.currentUserSubject.next(response.utente);
          }
        })
      );
  }

  register(utente: Utente): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/registrazione`, utente);
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem('currentUser');
      localStorage.removeItem('token');
    }
    this.currentUserSubject.next(null);
  }

  getCurrentUser(): Utente | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return !!this.currentUserSubject.value;
  }

  getToken(): string | null {
    if (this.isBrowser) {
      return localStorage.getItem('token');
    }
    return null;
  }
}
