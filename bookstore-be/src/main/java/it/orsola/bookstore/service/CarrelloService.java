package it.orsola.bookstore.service;

import it.orsola.bookstore.model.Carrello;
import it.orsola.bookstore.model.CarrelloDettaglio;
import it.orsola.bookstore.model.Prodotto;
import it.orsola.bookstore.model.Utente;
import it.orsola.bookstore.repository.CarrelloDettaglioRepository;
import it.orsola.bookstore.repository.CarrelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class CarrelloService {

    private final CarrelloRepository carrelloRepository;
    private final CarrelloDettaglioRepository carrelloDettaglioRepository;

    @Autowired
    public CarrelloService(CarrelloRepository carrelloRepository, CarrelloDettaglioRepository carrelloDettaglioRepository) {
        this.carrelloRepository = carrelloRepository;
        this.carrelloDettaglioRepository = carrelloDettaglioRepository;
    }

    public Optional<Carrello> findByUtente(Utente utente) {
        return carrelloRepository.findByUtente(utente);
    }

    @Transactional
    public Carrello getOrCreateCarrello(Utente utente) {
        return carrelloRepository.findByUtente(utente)
                .orElseGet(() -> {
                    Carrello nuovoCarrello = new Carrello();
                    nuovoCarrello.setUtente(utente);
                    nuovoCarrello.setDettagli(new ArrayList<>());
                    nuovoCarrello.setCreated(new Date());
                    nuovoCarrello.setStatus(0); // Carrello attivo
                    return carrelloRepository.save(nuovoCarrello);
                });
    }

    @Transactional
    public CarrelloDettaglio aggiungiProdotto(Carrello carrello, Prodotto prodotto, BigDecimal quantita) {
        Optional<CarrelloDettaglio> dettaglioEsistente = carrelloDettaglioRepository.findByCarrelloAndProdotto(carrello, prodotto);

        if (dettaglioEsistente.isPresent()) {
            CarrelloDettaglio dettaglio = dettaglioEsistente.get();
            dettaglio.setQuantita(dettaglio.getQuantita().add(quantita));
            return carrelloDettaglioRepository.save(dettaglio);
        } else {
            CarrelloDettaglio nuovoDettaglio = new CarrelloDettaglio();
            nuovoDettaglio.setCarrello(carrello);
            nuovoDettaglio.setProdotto(prodotto);
            nuovoDettaglio.setQuantita(quantita);
            nuovoDettaglio.setPrezzo(prodotto.getPrezzo());
            nuovoDettaglio.setAliquota(prodotto.getAliquota());
            return carrelloDettaglioRepository.save(nuovoDettaglio);
        }
    }

    @Transactional
    public void rimuoviProdotto(Carrello carrello, Prodotto prodotto) {
        Optional<CarrelloDettaglio> dettaglio = carrelloDettaglioRepository.findByCarrelloAndProdotto(carrello, prodotto);
        dettaglio.ifPresent(carrelloDettaglioRepository::delete);
    }

    @Transactional
    public void aggiornaQuantita(CarrelloDettaglio dettaglio, BigDecimal quantita) {
        dettaglio.setQuantita(quantita);
        carrelloDettaglioRepository.save(dettaglio);
    }

    @Transactional
    public void svuotaCarrello(Carrello carrello) {
        carrelloDettaglioRepository.deleteAll(carrelloDettaglioRepository.findByCarrello(carrello));
    }

    public BigDecimal calcolaTotale(Carrello carrello) {
        return carrelloDettaglioRepository.findByCarrello(carrello).stream()
                .map(dettaglio -> dettaglio.getPrezzo().multiply(dettaglio.getQuantita()))
                .reduce(BigDecimal.ZERO, (subtotal, element) -> subtotal.add(element));
    }
    
    @Transactional
    public Carrello aggiornaStatoCarrello(Carrello carrello, int nuovoStato) {
        carrello.setStatus(nuovoStato);
        return carrelloRepository.save(carrello);
    }
}
