package it.orsola.bookstore.dto;

import it.orsola.bookstore.model.Carrello;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CarrelloDTO {
    private Long idCarrello;
    private Long idUtente;
    private String nomeUtente;
    private Date created;
    private int status;
    private List<CarrelloDettaglioDTO> dettagli;
    private BigDecimal totale;

    public static CarrelloDTO fromEntity(Carrello carrello, BigDecimal totale) {
        CarrelloDTO dto = new CarrelloDTO();
        dto.setIdCarrello(carrello.getIdCarrello());
        dto.setIdUtente(carrello.getUtente().getIdUtente());
        dto.setNomeUtente(carrello.getUtente().getNome() + " " + carrello.getUtente().getCognome());
        dto.setCreated(carrello.getCreated());
        dto.setStatus(carrello.getStatus());
        
        if (carrello.getDettagli() != null) {
            dto.setDettagli(carrello.getDettagli().stream()
                    .map(CarrelloDettaglioDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        
        dto.setTotale(totale);
        
        return dto;
    }
}
