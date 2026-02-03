package it.orsola.bookstore.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "indirizzi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indirizzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idindirizzo")
    private Long idIndirizzo;

    @ManyToOne
    @JoinColumn(name = "idutente", nullable = false)
    @JsonIgnoreProperties("indirizzi")
    private Utente utente;
    
    @Column(name = "tipo", nullable = false)
    private int tipo = 0;
    
    @Column(name = "via")
    private String via;

    private String cap;

    private String citta;

    @Column(length = 2)
    private String provincia;

    private String nazione;
    
    private String telefono;
}
