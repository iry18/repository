package src.com.la_teca_del_giardiniere.classes;

import java.sql.Timestamp; 
import java.math.BigDecimal; 

public class Carrello {
    private int carrelloId; // Mappa a carrello_id
    private int utenteId; // Mappa a utente_id
    private int prodottoId; // Mappa a prodotto_id
    private String tipoProdotto; // Mappa a tipo_prodotto
    private int quantita; // Mappa a quantita
    private Timestamp dataAggiunta; // Mappa a data_aggiunta

    // Campi aggiuntivi per comodità nella visualizzazione (non persistiti nella tabella Carrello)
    private String nomeProdotto;
    private BigDecimal prezzoUnitario; // Usa java.math.BigDecimal direttamente

    // Costruttore vuoto
    public Carrello() {
    }

    // Costruttore per recupero dal DB
    public Carrello(int carrelloId, int utenteId, int prodottoId, String tipoProdotto, int quantita, Timestamp dataAggiunta) {
        this.carrelloId = carrelloId;
        this.utenteId = utenteId;
        this.prodottoId = prodottoId;
        this.tipoProdotto = tipoProdotto;
        this.quantita = quantita;
        this.dataAggiunta = dataAggiunta;
    }

    // Costruttore per inserimento nel DB (senza ID, che è AUTO_INCREMENT)
    public Carrello(int utenteId, int prodottoId, String tipoProdotto, int quantita, Timestamp dataAggiunta) {
        this.utenteId = utenteId;
        this.prodottoId = prodottoId;
        this.tipoProdotto = tipoProdotto;
        this.quantita = quantita;
        this.dataAggiunta = dataAggiunta;
    }

    // Getters e Setters
    public int getCarrelloId() { return carrelloId; }
    public void setCarrelloId(int carrelloId) { this.carrelloId = carrelloId; }
    public int getUtenteId() { return utenteId; }
    public void setUtenteId(int utenteId) { this.utenteId = utenteId; }
    public int getProdottoId() { return prodottoId; }
    public void setProdottoId(int prodottoId) { this.prodottoId = prodottoId; }
    public String getTipoProdotto() { return tipoProdotto; }
    public void setTipoProdotto(String tipoProdotto) { this.tipoProdotto = tipoProdotto; }
    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public Timestamp getDataAggiunta() { return dataAggiunta; }
    public void setDataAggiunta(Timestamp dataAggiunta) { this.dataAggiunta = dataAggiunta; }

    // Getters e Setters per i campi aggiuntivi
    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public BigDecimal getPrezzoUnitario() { // Tipo corretto
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(BigDecimal prezzoUnitario) { // Tipo corretto
        this.prezzoUnitario = prezzoUnitario;
    }

    // Metodo helper per calcolare il totale del singolo articolo nel carrello
    public BigDecimal getTotaleArticolo() { // Tipo corretto
        if (prezzoUnitario == null) {
            return BigDecimal.ZERO;
        }
        return prezzoUnitario.multiply(new BigDecimal(quantita));
    }

    @Override
    public String toString() {
        return "Carrello{" +
                "carrelloId=" + carrelloId +
                ", utenteId=" + utenteId +
                ", prodottoId=" + prodottoId +
                ", tipoProdotto='" + tipoProdotto + '\'' +
                ", quantita=" + quantita +
                ", dataAggiunta=" + dataAggiunta +
                ", nomeProdotto='" + nomeProdotto + '\'' +
                ", prezzoUnitario=" + prezzoUnitario +
                '}';
    }
}