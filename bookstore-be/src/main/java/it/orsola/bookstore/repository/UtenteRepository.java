package it.orsola.bookstore.repository;

import it.orsola.bookstore.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByUsername(String username);
    Optional<Utente> findByUsernameAndUserPass(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByMail(String mail);
}
