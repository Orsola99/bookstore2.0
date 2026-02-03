package it.orsola.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "ordinidettagli")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrdineDettaglio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddettaglio")
    private Long idDettaglio;

    @ManyToOne
    @JoinColumn(name = "idordine", nullable = false)
    @JsonIgnoreProperties({"dettagli", "utente", "indirizzoFatturazione", "indirizzoSpedizione"})
    private Ordine ordine;

    @ManyToOne
    @JoinColumn(name = "idprodotto", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private BigDecimal quantita;

    @Column(nullable = false)
    private BigDecimal prezzo = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private BigDecimal aliquota = BigDecimal.ZERO;
}
