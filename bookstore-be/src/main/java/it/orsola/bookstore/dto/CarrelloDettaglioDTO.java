package it.orsola.bookstore.dto;

import it.orsola.bookstore.model.CarrelloDettaglio;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarrelloDettaglioDTO {
    private Long idCarrelloDettagli;
    private Long idProdotto;
    private String titoloProdotto;
    private String sottotitoloProdotto;
    private String nomeFileProdotto;
    private BigDecimal quantita;
    private BigDecimal prezzo;
    private BigDecimal aliquota;
    private BigDecimal totaleRiga;

    public static CarrelloDettaglioDTO fromEntity(CarrelloDettaglio dettaglio) {
        CarrelloDettaglioDTO dto = new CarrelloDettaglioDTO();
        dto.setIdCarrelloDettagli(dettaglio.getIdCarrelloDettagli());
        dto.setIdProdotto(dettaglio.getProdotto().getIdProdotto());
        dto.setTitoloProdotto(dettaglio.getProdotto().getTitolo());
        dto.setSottotitoloProdotto(dettaglio.getProdotto().getSottotitolo());
        dto.setNomeFileProdotto(dettaglio.getProdotto().getNomeFile());
        dto.setQuantita(dettaglio.getQuantita());
        dto.setPrezzo(dettaglio.getPrezzo());
        dto.setAliquota(dettaglio.getAliquota());
        dto.setTotaleRiga(dettaglio.getPrezzo().multiply(dettaglio.getQuantita()));
        return dto;
    }
}
