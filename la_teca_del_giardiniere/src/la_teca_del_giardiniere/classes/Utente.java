package la_teca_del_giardiniere.classes;

import java.sql.Timestamp;
import java.util.Collections; // Import per Collections.unmodifiableSet
import java.util.HashSet;
import java.util.Set;

public class Utente {
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String passwordHash; // Preferibile chiamarlo passwordHash
    private String indirizzo;
    private String citta;
    private int CAP;
    private String telefono;
    private Timestamp data_registrazione; // CamelCase: dataRegistrazione
    private String provincia;
    private boolean isAdmin; // Flag che determina il ruolo di amministratore
    private boolean isVenditore;
    private Set<String> ruoli; // Questo set conterrà i ruoli attuali (es. "admin", "venditore")

    public Utente() {
        this.ruoli = new HashSet<>(); // Inizializza il set per evitare NullPointerException
    }

    // --- Getters and Setters ---

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Timestamp getData_registrazione() { // Meglio dataRegistrazione
        return data_registrazione;
    }

    public void setData_registrazione(Timestamp data_registrazione) { // Meglio dataRegistrazione
        this.data_registrazione = data_registrazione;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    // Metodi per isAdmin e ruoli
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        // Aggiorna il set di ruoli basandosi sul flag isAdmin
        if (isAdmin) {
            this.ruoli.add("amministratore");
        } else {
            this.ruoli.remove("amministratore");
        }
    }
    
    public boolean isVenditore() {
        return isVenditore;
    }

    public void setVenditore(boolean isVenditore) {
        this.isVenditore = isVenditore;
        if (isVenditore) {
            this.ruoli.add("venditore");
        } else {
            this.ruoli.remove("venditore");
        }
    }

    /**
     * Restituisce un set non modificabile dei ruoli dell'utente.
     * Questo metodo non dovrebbe essere chiamato direttamente per impostare i ruoli
     * ma per recuperarli. I ruoli sono derivati dal flag isAdmin e da eventuali
     * altri ruoli specifici (es. "venditore") che potresti aggiungere in futuro.
     */
    public Set<String> getRuoli() {
        // Se isAdmin è true, assicurati che "amministratore" sia nel set
        // Questo è per robustezza, anche se setAdmin() dovrebbe già farlo
        if (this.isAdmin && !this.ruoli.contains("amministratore")) {
             this.ruoli.add("amministratore");
        } else if (!this.isAdmin && this.ruoli.contains("amministratore")) {
            this.ruoli.remove("amministratore");
        }

        if (this.isVenditore && !this.ruoli.contains("venditore")) {
            this.ruoli.add("venditore");
        } else if (!this.isVenditore && this.ruoli.contains("venditore")) {
            this.ruoli.remove("venditore");
        }
        // Restituisce una copia immutabile per prevenire modifiche esterne
        return Collections.unmodifiableSet(this.ruoli);
    }

    /**
     * Metodo per aggiungere ruoli specifici (es. "venditore").
     * @param ruolo Il ruolo da aggiungere.
     */
    public void addRuolo(String ruolo) {
        this.ruoli.add(ruolo);
    }

    /**
     * Metodo per rimuovere ruoli specifici.
     * @param ruolo Il ruolo da rimuovere.
     */
    public void removeRuolo(String ruolo) {
        this.ruoli.remove(ruolo);
    }
}