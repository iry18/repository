
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un utente registrato nel sistema.
 * Contiene informazioni personali, di contatto e i dettagli di autenticazione.
 */
public class Utente {

    private int id; // L'ID univoco dell'utente nel database
    private String nome;
    private String cognome;
    private String email;
    private String passwordHash; // L'hash della password (non la password in chiaro)
    private String indirizzo;
    private String citta;
    private int CAP;
    private String telefono;
    private String provincia;
    private Timestamp data_registrazione;
    private boolean isAdmin; // Flag per indicare se l'utente è un amministratore

    /**
     * Costruttore completo per la classe Utente.
     * @param id L'ID dell'utente.
     * @param nome Il nome dell'utente.
     * @param cognome Il cognome dell'utente.
     * @param email L'email dell'utente (utilizzata anche come username).
     * @param passwordHash L'hash Bcrypt della password.
     * @param isAdmin True se l'utente è un amministratore, false altrimenti.
     * @param indirizzo L'indirizzo di residenza/spedizione dell'utente.
     * @param citta La città di residenza/spedizione dell'utente.
     * @param CAP Il Codice di Avviamento Postale.
     * @param telefono Il numero di telefono dell'utente.
     * @param provincia La provincia (sigla di due lettere).
     * @param data_registrazione La data e l'ora di registrazione dell'utente.
     */
    public Utente(int id, String nome, String cognome, String email, String passwordHash, boolean isAdmin,
                  String indirizzo, String citta, int CAP, String telefono, String provincia, Timestamp data_registrazione) {
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

    /**
     * Costruttore per la creazione di un nuovo utente (senza ID, che verrà generato dal DB).
     * @param nome Il nome dell'utente.
     * @param cognome Il cognome dell'utente.
     * @param email L'email dell'utente (utilizzata anche come username).
     * @param passwordHash L'hash Bcrypt della password.
     * @param isAdmin True se l'utente è un amministratore, false altrimenti.
     * @param indirizzo L'indirizzo di residenza/spedizione dell'utente.
     * @param citta La città di residenza/spedizione dell'utente.
     * @param CAP Il Codice di Avviamento Postale.
     * @param telefono Il numero di telefono dell'utente.
     * @param provincia La provincia (sigla di due lettere).
     * @param data_registrazione La data e l'ora di registrazione dell'utente.
     */
    public Utente(String nome, String cognome, String email, String passwordHash, boolean isAdmin,
                  String indirizzo, String citta, int CAP, String telefono, String provincia, Timestamp data_registrazione) {
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

    /**
     * Costruttore vuoto.
     */
    public Utente() {
    }

    // --- Metodi Getter e Setter ---

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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Restituisce una lista di ruoli associati all'utente basandosi sul campo `isAdmin`.
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
        this.isAdmin = (ruoli != null && ruoli.contains("amministratore"));
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
                + ", telefono=" + telefono + ", provincia=" + provincia
                + ", data_registrazione=" + data_registrazione + "]";
    }
}
