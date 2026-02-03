package it.orsola.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "prodotti")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idprodotto")
    private Long idProdotto;

    private String titolo;
    
    private String sottotitolo;
    
    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false)
    private BigDecimal prezzo = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private BigDecimal aliquota = BigDecimal.ZERO;
    
    @Column(name = "nomefile")
    private String nomeFile;
}
