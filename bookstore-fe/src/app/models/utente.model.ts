export interface Utente {
  idUtente?: number; // Opzionale, sarà assegnato dal backend
  nome: string;
  cognome: string;
  codiceFiscale?: string;
  partitaIva?: string;
  mail: string; // Nel backend è 'mail', non 'email'
  username: string;
  userPass?: string; // Nel backend è 'userPass', non 'password'
  privacy?: number;
}
