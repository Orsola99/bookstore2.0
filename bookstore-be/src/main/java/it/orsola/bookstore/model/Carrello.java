package it.orsola.bookstore.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "carrelli")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcarrello")
    private Long idCarrello;

    @ManyToOne
    @JoinColumn(name = "idutente", nullable = false)
    private Utente utente;
    
    @Temporal(TemporalType.DATE)
    private Date created;
    
    @Column(nullable = false)
    private int status = 0;

    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CarrelloDettaglio> dettagli;
}
