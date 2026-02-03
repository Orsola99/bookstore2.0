package it.orsola.bookstore.repository;

import it.orsola.bookstore.model.Indirizzo;
import it.orsola.bookstore.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndirizzoRepository extends JpaRepository<Indirizzo, Long> {
    List<Indirizzo> findByUtente(Utente utente);
}
