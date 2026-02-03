import { Ordine } from './ordine.model';
import { Prodotto } from './prodotto.model';

export interface OrdineDettaglio {
  idOrdineDettaglio: number;
  ordine: Ordine;
  prodotto: Prodotto;
  quantita: number;
  prezzo: number;
  aliquota: number;
}
