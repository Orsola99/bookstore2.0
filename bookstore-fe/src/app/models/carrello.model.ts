import { CarrelloDettaglio } from './carrello-dettaglio.model';

export interface Carrello {
  idCarrello: number;
  idUtente: number;
  nomeUtente: string;
  dettagli?: CarrelloDettaglio[];
  created: Date;
  status: number;
  totale: number;
}
