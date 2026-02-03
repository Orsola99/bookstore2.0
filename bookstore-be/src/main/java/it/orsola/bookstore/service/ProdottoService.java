package it.orsola.bookstore.service;

import it.orsola.bookstore.model.Prodotto;
import it.orsola.bookstore.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;

    @Autowired
    public ProdottoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    public List<Prodotto> findAll() {
        return prodottoRepository.findAll();
    }

    public Optional<Prodotto> findById(Long id) {
        return prodottoRepository.findById(id);
    }

    public List<Prodotto> findByTitolo(String titolo) {
        return prodottoRepository.findByTitoloContainingIgnoreCase(titolo);
    }
    
    public List<Prodotto> findBySottotitolo(String sottotitolo) {
        return prodottoRepository.findBySottotitoloContainingIgnoreCase(sottotitolo);
    }
    
    public List<Prodotto> search(String query) {
        return prodottoRepository.findByTitoloContainingIgnoreCaseOrSottotitoloContainingIgnoreCase(query, query);
    }
    
    public List<Prodotto> findByPrezzoBetween(BigDecimal minPrezzo, BigDecimal maxPrezzo) {
        return prodottoRepository.findByPrezzoBetween(minPrezzo, maxPrezzo);
    }

    @Transactional
    public Prodotto save(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }

    @Transactional
    public void delete(Long id) {
        prodottoRepository.deleteById(id);
    }

    @Transactional
    public Prodotto update(Prodotto prodotto) {
        return prodottoRepository.save(prodotto);
    }
}
