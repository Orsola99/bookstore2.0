package it.orsola.bookstore.repository;

import it.orsola.bookstore.model.Ordine;
import it.orsola.bookstore.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByUtente(Utente utente);
    List<Ordine> findByUtenteOrderByDataOrdineDesc(Utente utente);
    List<Ordine> findByStatus(int status);
    List<Ordine> findByDataOrdineAfter(Date data);
    List<Ordine> findByDataOrdineAfterAndStatus(Date data, int status);
}
