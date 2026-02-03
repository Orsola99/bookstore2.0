package it.orsola.bookstore.repository;

import it.orsola.bookstore.model.Ordine;
import it.orsola.bookstore.model.OrdineDettaglio;
import it.orsola.bookstore.model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrdineDettaglioRepository extends JpaRepository<OrdineDettaglio, Long> {
    List<OrdineDettaglio> findByOrdine(Ordine ordine);
    List<OrdineDettaglio> findByProdotto(Prodotto prodotto);
    
    @Query("SELECT SUM(od.quantita) FROM OrdineDettaglio od WHERE od.prodotto.idProdotto = ?1")
    BigDecimal getTotalQuantitaVendutaByProdottoId(Long prodottoId);
}
