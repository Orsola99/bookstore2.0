## Bookstore Application - Spring + Angular
Aggiornamento della web app Bookstore.

L'applicazione Bookstore è un sistema completo per la gestione di una libreria, sviluppato utilizzando Spring Boot per il backend e Angular per il frontend. Questa applicazione consente agli utenti di visualizzare i libri dal catalogo della libreria ed aggiungerli al carrello e proseguire all'acquisto.


## Prerequisiti
   Assicurati di avere installato sul tuo sistema i seguenti strumenti:
- ✅ **Maven**
- ✅ **JDK 11** 
- ✅ **PostgreSQL**

## ⚙️ Configurazione iniziale

### 1. **Clona il repository**  
   Apri il terminale ed esegui il comando:
   ```bash
   git clone https://github.com/Orsola99/bookstore2.0.git
   ```

### 2. **Modifica del file (application.properties**)
   > Dopo aver clonato il progetto, apri il file `src/main/resources/application.properties` e aggiorna i valori con le tue credenziali locali (nome del database, utente, password, ecc.).

### 3. **Esegui il database PostgreSQL**  
   Assicurati che il tuo server PostgreSQL sia in esecuzione e che il database specificato nel file `application.properties` sia stato creato.

## 🚀 Avvio dell'applicazione
### 1. **Avvia il backend Spring Boot**  
   Nel terminale, naviga nella directory del progetto e esegui:
   ```bash
   mvn clean install -DskipTests
   mvn spring-boot:run
   ```
   L'applicazione backend sarà accessibile su `http://localhost:8080`. 

   E' possibile visualizzare la documentazione delle API tramite Swagger all'indirizzo:
   `http://localhost:8080/swagger-ui.html`

### 2. **Avvia il frontend Angular**
   

   Apri un nuovo terminale, naviga nella cartella `frontend` e esegui:
   ```bash
   npm install
   ng serve
   ```
   L'applicazione frontend sarà accessibile su `http://localhost:4200`.

## **Caratteristiche principali**
   - Visualizzazione del catalogo dei libri
   - Aggiunta di libri al carrello
   - Procedura di checkout per completare l'acquisto
   - Gestione degli ordini

## 📚 Tecnologie utilizzate
- **Backend**: Spring Boot, Spring Data JPA, PostgreSQL
- **Frontend**: Angular, TypeScript, HTML, CSS
- **Strumenti di build**: Maven, npm
- **Documentazione API**: Swagger