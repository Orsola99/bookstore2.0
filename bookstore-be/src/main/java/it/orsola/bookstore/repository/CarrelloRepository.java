package it.orsola.bookstore.repository;

import it.orsola.bookstore.model.Carrello;
import it.orsola.bookstore.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
    Optional<Carrello> findByUtente(Utente utente);
}
