package it.orsola.bookstore.service;

import it.orsola.bookstore.model.*;
import it.orsola.bookstore.repository.OrdineDettaglioRepository;
import it.orsola.bookstore.repository.OrdineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final OrdineDettaglioRepository ordineDettaglioRepository;
    private final CarrelloService carrelloService;
    private final IndirizzoService indirizzoService;

    @Autowired
    public OrdineService(OrdineRepository ordineRepository, 
                         OrdineDettaglioRepository ordineDettaglioRepository, 
                         CarrelloService carrelloService,
                         IndirizzoService indirizzoService) {
        this.ordineRepository = ordineRepository;
        this.ordineDettaglioRepository = ordineDettaglioRepository;
        this.carrelloService = carrelloService;
        this.indirizzoService = indirizzoService;
    }

    public List<Ordine> findAll() {
        return ordineRepository.findAll();
    }

    public Optional<Ordine> findById(Long id) {
        return ordineRepository.findById(id);
    }

    public List<Ordine> findByUtente(Utente utente) {
        return ordineRepository.findByUtenteOrderByDataOrdineDesc(utente);
    }
    
    public List<Ordine> findByStatus(int status) {
        return ordineRepository.findByStatus(status);
    }
    
    public List<Ordine> findByDataOrdineAfter(Date data) {
        return ordineRepository.findByDataOrdineAfter(data);
    }
    
    public List<Ordine> findByDataOrdineAfterAndStatus(Date data, int status) {
        return ordineRepository.findByDataOrdineAfterAndStatus(data, status);
    }

    @Transactional
    public Ordine creaOrdine(Utente utente, Carrello carrello, Long idIndirizzoSpedizione, Long idIndirizzoFatturazione) {
        List<CarrelloDettaglio> dettagliCarrello = carrello.getDettagli();
        
        if (dettagliCarrello.isEmpty()) {
            throw new IllegalStateException("Il carrello è vuoto");
        }
        
        // Recupera gli indirizzi
        Indirizzo indirizzoSpedizione = indirizzoService.findById(idIndirizzoSpedizione)
            .orElseThrow(() -> new IllegalArgumentException("Indirizzo di spedizione non trovato"));
            
        Indirizzo indirizzoFatturazione = indirizzoService.findById(idIndirizzoFatturazione)
            .orElseThrow(() -> new IllegalArgumentException("Indirizzo di fatturazione non trovato"));

        Ordine ordine = new Ordine();
        ordine.setUtente(utente);
        ordine.setDataOrdine(new Date());
        ordine.setStatus(0); // Nuovo ordine
        ordine.setIndirizzoSpedizione(indirizzoSpedizione);
        ordine.setIndirizzoFatturazione(indirizzoFatturazione);
        ordine.setDettagli(new ArrayList<>());
        
        Ordine ordineSalvato = ordineRepository.save(ordine);
        
        // Crea i dettagli dell'ordine dal carrello
        for (CarrelloDettaglio dettaglioCarrello : dettagliCarrello) {
            OrdineDettaglio dettaglioOrdine = new OrdineDettaglio();
            dettaglioOrdine.setOrdine(ordineSalvato);
            dettaglioOrdine.setProdotto(dettaglioCarrello.getProdotto());
            dettaglioOrdine.setQuantita(dettaglioCarrello.getQuantita());
            dettaglioOrdine.setPrezzo(dettaglioCarrello.getPrezzo());
            dettaglioOrdine.setAliquota(dettaglioCarrello.getAliquota());
            ordineDettaglioRepository.save(dettaglioOrdine);
            ordineSalvato.getDettagli().add(dettaglioOrdine);
        }
        
        // Svuota il carrello dopo aver creato l'ordine
        carrelloService.svuotaCarrello(carrello);
        
        return ordineSalvato;
    }
    
    @Transactional
    public Ordine aggiornaStatoOrdine(Long idOrdine, int nuovoStato) {
        Ordine ordine = ordineRepository.findById(idOrdine)
            .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato"));
            
        ordine.setStatus(nuovoStato);
        
        // Se lo stato è "spedito", aggiorna la data di spedizione
        if (nuovoStato == 2) { // Assumiamo che 2 sia lo stato "spedito"
            ordine.setDataSpedizione(new Date());
        }
        
        return ordineRepository.save(ordine);
    }

    public BigDecimal calcolaTotaleOrdine(Ordine ordine) {
        return ordineDettaglioRepository.findByOrdine(ordine).stream()
                .map(dettaglio -> dettaglio.getPrezzo().multiply(dettaglio.getQuantita()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
