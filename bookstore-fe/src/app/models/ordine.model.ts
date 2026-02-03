import { Utente } from './utente.model';
import { Indirizzo } from './indirizzo.model';
import { OrdineDettaglio } from './ordine-dettaglio.model';

export interface Ordine {
  idOrdine: number;
  utente: Utente;
  dataOrdine: Date;
  dataSpedizione?: Date;
  status: string;
  indirizzoFatturazione: Indirizzo;
  indirizzoSpedizione: Indirizzo;
  dettagli?: OrdineDettaglio[];
}
