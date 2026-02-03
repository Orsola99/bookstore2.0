package it.orsola.bookstore.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "utenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idutente")
    private Long idUtente;

    private String cognome;

    private String nome;

    @Column(name = "codicefiscale")
    private String codiceFiscale;

    @Column(name = "partitaiva")
    private String partitaIva;

    private String mail;

    @Column(nullable = false)
    private String username;

    @Column(name = "userpass", nullable = false)
    private String userPass;

    @Column(nullable = false)
    private int privacy = 0;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("utente")
    private List<Indirizzo> indirizzi;
}
