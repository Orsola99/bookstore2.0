package it.orsola.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ordini")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idordine")
    private Long idOrdine;

    @ManyToOne
    @JoinColumn(name = "idutente", nullable = false)
    @JsonIgnoreProperties({"ordini", "indirizzi"})
    private Utente utente;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "dataordine")
    private Date dataOrdine;
        
    @Column(name = "statoordine", nullable = false)
    private int status = 0;
    
    @ManyToOne
    @JoinColumn(name = "idindirizzofatturazione", nullable = false)
    @JsonIgnoreProperties({"utente"})
    private Indirizzo indirizzoFatturazione;
    
    @ManyToOne
    @JoinColumn(name = "idindirizzospedizione", nullable = false)
    @JsonIgnoreProperties({"utente"})
    private Indirizzo indirizzoSpedizione;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "dataspedizione")
    private Date dataSpedizione;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"ordine"})
    private List<OrdineDettaglio> dettagli;
}
