package it.orsola.bookstore.service;

import it.orsola.bookstore.model.Ordine;
import it.orsola.bookstore.model.OrdineDettaglio;
import it.orsola.bookstore.model.Prodotto;
import it.orsola.bookstore.repository.OrdineDettaglioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrdineDettaglioService {

    private final OrdineDettaglioRepository ordineDettaglioRepository;

    @Autowired
    public OrdineDettaglioService(OrdineDettaglioRepository ordineDettaglioRepository) {
        this.ordineDettaglioRepository = ordineDettaglioRepository;
    }

    public List<OrdineDettaglio> findAll() {
        return ordineDettaglioRepository.findAll();
    }

    public Optional<OrdineDettaglio> findById(Long id) {
        return ordineDettaglioRepository.findById(id);
    }

    public List<OrdineDettaglio> findByOrdine(Ordine ordine) {
        return ordineDettaglioRepository.findByOrdine(ordine);
    }
    
    public List<OrdineDettaglio> findByProdotto(Prodotto prodotto) {
        return ordineDettaglioRepository.findByProdotto(prodotto);
    }
    
    public BigDecimal getTotalQuantitaVendutaByProdottoId(Long prodottoId) {
        BigDecimal result = ordineDettaglioRepository.getTotalQuantitaVendutaByProdottoId(prodottoId);
        return result != null ? result : BigDecimal.ZERO;
    }

    @Transactional
    public OrdineDettaglio save(OrdineDettaglio ordineDettaglio) {
        return ordineDettaglioRepository.save(ordineDettaglio);
    }

    @Transactional
    public void delete(Long id) {
        ordineDettaglioRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteByOrdine(Ordine ordine) {
        List<OrdineDettaglio> dettagli = ordineDettaglioRepository.findByOrdine(ordine);
        ordineDettaglioRepository.deleteAll(dettagli);
    }
}
