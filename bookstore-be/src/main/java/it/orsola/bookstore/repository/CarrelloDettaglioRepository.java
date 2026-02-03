package it.orsola.bookstore.repository;

import it.orsola.bookstore.model.Carrello;
import it.orsola.bookstore.model.CarrelloDettaglio;
import it.orsola.bookstore.model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarrelloDettaglioRepository extends JpaRepository<CarrelloDettaglio, Long> {
    List<CarrelloDettaglio> findByCarrello(Carrello carrello);
    Optional<CarrelloDettaglio> findByCarrelloAndProdotto(Carrello carrello, Prodotto prodotto);
}
