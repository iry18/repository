package src.com.la_teca_del_giardiniere.classes;

import java.sql.Timestamp;
import java.util.ArrayList; // Import per List
import java.util.List;    // Import per List

public class Utente { 

    private int id; // l'ID dell'utente 
    private String nome;
    private String cognome;
    private String email;
    private String passwordHash; 
    private String indirizzo;
    private String citta;
    private int CAP;
    private String telefono; 
    private String provincia; 
    private Timestamp data_registrazione;
    private boolean isAdmin; 

    
    public Utente(int id, String nome, String cognome, String email, String passwordHash, boolean isAdmin,
                  String indirizzo, String citta, int CAP, String telefono, String provincia, Timestamp data_registrazione) { // <--- Provincia aggiunta qui
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.CAP = CAP;
        this.telefono = telefono;
        this.provincia = provincia; 
        this.data_registrazione = data_registrazione;
    }

    public Utente(String nome, String cognome, String email, String passwordHash, boolean isAdmin,
                  String indirizzo, String citta, int CAP, String telefono, String provincia, Timestamp data_registrazione) { // <--- Provincia aggiunta qui
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.CAP = CAP;
        this.telefono = telefono;
        this.provincia = provincia; 
        this.data_registrazione = data_registrazione;
    }

    public Utente() {
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Metodo per ottenere la password hashata
    public String getPasswordHash() {
        return passwordHash;
    }

    // Metodo per impostare la password hashata (assicurati che sia l'hash, non la password in chiaro)
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    // --- NUOVA LOGICA PER I RUOLI BASATA SU isAdmin ---
    /**
     * Restituisce una lista di ruoli associati all'utente.
     * Basandosi sul campo `isAdmin`.
     * @return una List<String> contenente i ruoli (es. "compratore", "amministratore").
     */
    public List<String> getRuoli() {
        List<String> ruoli = new ArrayList<>();
        ruoli.add("compratore"); // Ruolo di base per tutti gli utenti
        if (this.isAdmin) {
            ruoli.add("amministratore");
        }
        return ruoli;
    }

    /**
     * Imposta il flag `isAdmin` basandosi sulla presenza del ruolo "amministratore"
     * in una lista di ruoli fornita.
     * @param ruoli la lista di ruoli da controllare.
     */
    public void setRuoli(List<String> ruoli) {
        if (ruoli != null && ruoli.contains("amministratore")) {
            this.isAdmin = true;
        } else {
            this.isAdmin = false;
        }
    }
    
    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public int getCAP() {
        return CAP;
    }

    public void setCAP(int CAP) {
        this.CAP = CAP;
    }

    public String getTelefono() { // Restituito String
        return telefono;
    }

    public void setTelefono(String telefono) { // Accetta String
        this.telefono = telefono;
    }

    // --- Getter e Setter per il nuovo campo 'provincia' ---
    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
    
    public Timestamp getData_registrazione() {
        return data_registrazione;
    }

    public void setData_registrazione(Timestamp data_registrazione) {
        this.data_registrazione = data_registrazione;
    }

    @Override
    public String toString() {
        return "Utente [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email
                + ", isAdmin=" + isAdmin + ", indirizzo=" + indirizzo + ", citta=" + citta + ", CAP=" + CAP
                + ", telefono=" + telefono + ", provincia=" + provincia // <--- AGGIUNTO PROVINCIA NEL toString
                + ", data_registrazione=" + data_registrazione + "]";
    }
}