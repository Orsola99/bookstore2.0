import { Utente } from './utente.model';

export interface Indirizzo {
  idIndirizzo: number;
  utente: Utente;
  tipo: number;
  via: string;
  cap: string;
  citta: string;
  provincia: string;
  nazione: string;
  telefono: string;
}
