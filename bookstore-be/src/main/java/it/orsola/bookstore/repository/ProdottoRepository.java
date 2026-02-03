package it.orsola.bookstore.repository;

import it.orsola.bookstore.model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    List<Prodotto> findByTitoloContainingIgnoreCase(String titolo);
    List<Prodotto> findBySottotitoloContainingIgnoreCase(String sottotitolo);
    List<Prodotto> findByTitoloContainingIgnoreCaseOrSottotitoloContainingIgnoreCase(String titolo, String sottotitolo);
    
    @Query("SELECT p FROM Prodotto p WHERE p.prezzo BETWEEN ?1 AND ?2")
    List<Prodotto> findByPrezzoBetween(BigDecimal minPrezzo, BigDecimal maxPrezzo);
}
