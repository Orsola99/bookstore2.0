package it.orsola.bookstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "carrellodettagli")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrelloDettaglio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcarrellodettagli")
    private Long idCarrelloDettagli;

    @ManyToOne
    @JoinColumn(name = "idcarrello", nullable = false)
    @JsonBackReference
    private Carrello carrello;

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
