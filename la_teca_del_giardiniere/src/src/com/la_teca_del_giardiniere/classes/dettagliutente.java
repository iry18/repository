package src.com.la_teca_del_giardiniere.classes;


import java.math.BigDecimal;

public class dettagliutente {
    private int dettaglio_id;
    private int ordine_id;
    private Integer p_codprodotto; // Può essere null se non è un prodotto (pianta o accessorio)
    private Integer accessorio_id; // Può essere null se non è un accessorio
    private BigDecimal prezzo_unitario;
    private int quantita;

    // Per memorizzare il nome del prodotto/accessorio associato (non nel DB, ma utile nel codice)
    private String nomeProdotto; 

    // Costruttore vuoto
    public dettagliutente() {
    }

    // Costruttore con tutti i campi (senza ID per l'inserimento)
    public dettagliutente(int ordine_id, Integer p_codprodotto, Integer accessorio_id,
                           BigDecimal prezzo_unitario, int quantita) {
        this.ordine_id = ordine_id;
        this.p_codprodotto = p_codprodotto;
        this.accessorio_id = accessorio_id;
        this.prezzo_unitario = prezzo_unitario;
        this.quantita = quantita;
    }

    // Getters and Setters

    public int getDettaglio_id() {
        return dettaglio_id;
    }

    public void setDettaglio_id(int dettaglio_id) {
        this.dettaglio_id = dettaglio_id;
    }

    public int getOrdine_id() {
        return ordine_id;
    }

    public void setOrdine_id(int ordine_id) {
        this.ordine_id = ordine_id;
    }

    public Integer getP_codprodotto() {
        return p_codprodotto;
    }

    public void setP_codprodotto(Integer p_codprodotto) {
        this.p_codprodotto = p_codprodotto;
    }

    public Integer getAccessorio_id() {
        return accessorio_id;
    }

    public void setAccessorio_id(Integer accessorio_id) {
        this.accessorio_id = accessorio_id;
    }

    public BigDecimal getPrezzo_unitario() {
        return prezzo_unitario;
    }

    public void setPrezzo_unitario(BigDecimal prezzo_unitario) {
        this.prezzo_unitario = prezzo_unitario;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    // Metodo toString per debugging (opzionale ma utile)
    @Override
    public String toString() {
        return "DettaglioOrdine{" +
               "dettaglio_id=" + dettaglio_id +
               ", ordine_id=" + ordine_id +
               ", p_codprodotto=" + p_codprodotto +
               ", accessorio_id=" + accessorio_id +
               ", prezzo_unitario=" + prezzo_unitario +
               ", quantita=" + quantita +
               ", nomeProdotto='" + nomeProdotto + '\'' +
               '}';
    }
}
