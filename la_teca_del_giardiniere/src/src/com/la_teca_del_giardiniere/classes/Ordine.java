package src.com.la_teca_del_giardiniere.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList; // Suggerimento: importa ArrayList per inizializzazione
import java.util.List;

// CAMBIATO: L'import di DettaglioOrdine non è nel package 'src.com.la_teca_del_giardiniere'
// ma nel subpackage 'classes'. Assicurati che DettaglioOrdine sia in 'src.com.la_teca_del_giardiniere.classes'.
import src.com.la_teca_del_giardiniere.classes.DettaglioOrdine; // Corretto import

public class Ordine {
    private int id;
    private int utenteId; 
    private Timestamp dataOrdine; 
    private String cittaSpedizione; 
    private String paeseSpedizione; 
    private String capSpedizione; 
    private BigDecimal totaleOrdine;
    private String metodoPagamento; 
    private BigDecimal iva; 
    private String statoOrdine; 
    private String note;
    private List<DettaglioOrdine> dettagliOrdine;

    
    public Ordine() {
       
        this.dettagliOrdine = new ArrayList<>();
    }

    // Costruttore con ID (utile quando si recupera l'ordine dal DB)
    public Ordine(int id, int utenteId, Timestamp dataOrdine, String cittaSpedizione,
                   String paeseSpedizione, String capSpedizione, BigDecimal totaleOrdine,
                   String metodoPagamento, BigDecimal iva, String statoOrdine, String note) {
        this.id = id;
        this.utenteId = utenteId;
        this.dataOrdine = dataOrdine;
        this.cittaSpedizione = cittaSpedizione;
        this.paeseSpedizione = paeseSpedizione;
        this.capSpedizione = capSpedizione;
        this.totaleOrdine = totaleOrdine;
        this.metodoPagamento = metodoPagamento;
        this.iva = iva;
        this.statoOrdine = statoOrdine;
        this.note = note;
        this.dettagliOrdine = new ArrayList<>(); // Inizializza anche qui
    }

    // Costruttore senza ID (per nuovo inserimento, come quello che avevi)
    public Ordine(int utenteId, Timestamp dataOrdine, String cittaSpedizione, String paeseSpedizione, String capSpedizione,
                   BigDecimal totaleOrdine, String metodoPagamento, BigDecimal iva, String statoOrdine, String note) {
        this.utenteId = utenteId;
        this.dataOrdine = dataOrdine;
        this.cittaSpedizione = cittaSpedizione;
        this.paeseSpedizione = paeseSpedizione;
        this.capSpedizione = capSpedizione;
        this.totaleOrdine = totaleOrdine;
        this.metodoPagamento = metodoPagamento;
        this.iva = iva;
        this.statoOrdine = statoOrdine;
        this.note = note;
        this.dettagliOrdine = new ArrayList<>(); // Inizializza anche qui
    }

    // Getters and Setters (Aggiornati ai nuovi nomi dei campi)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(int utenteId) {
        this.utenteId = utenteId;
    }

    public Timestamp getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Timestamp dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getCittaSpedizione() {
        return cittaSpedizione;
    }

    public void setCittaSpedizione(String cittaSpedizione) {
        this.cittaSpedizione = cittaSpedizione;
    }

    public String getPaeseSpedizione() {
        return paeseSpedizione;
    }

    public void setPaeseSpedizione(String paeseSpedizione) {
        this.paeseSpedizione = paeseSpedizione;
    }

    public String getCapSpedizione() {
        return capSpedizione;
    }

    public void setCapSpedizione(String capSpedizione) {
        this.capSpedizione = capSpedizione;
    }

    public BigDecimal getTotaleOrdine() {
        return totaleOrdine;
    }

    public void setTotaleOrdine(BigDecimal totaleOrdine) {
        this.totaleOrdine = totaleOrdine;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public String getStatoOrdine() {
        return statoOrdine;
    }

    public void setStatoOrdine(String statoOrdine) {
        this.statoOrdine = statoOrdine;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<DettaglioOrdine> getDettagliOrdine() {
        return dettagliOrdine;
    }

    public void setDettagliOrdine(List<DettaglioOrdine> dettagliOrdine) {
        this.dettagliOrdine = dettagliOrdine;
    }

    // Metodo toString per debugging (opzionale ma utile)
    @Override
    public String toString() {
        return "Ordine{" +
                "id=" + id +
                ", utenteId=" + utenteId +
                ", dataOrdine=" + dataOrdine +
                ", cittaSpedizione='" + cittaSpedizione + '\'' +
                ", paeseSpedizione='" + paeseSpedizione + '\'' +
                ", capSpedizione='" + capSpedizione + '\'' +
                ", totaleOrdine=" + totaleOrdine +
                ", metodoPagamento='" + metodoPagamento + '\'' +
                ", iva=" + iva +
                ", statoOrdine='" + statoOrdine + '\'' +
                ", note='" + note + '\'' +
                ", dettagliOrdine=" + dettagliOrdine +
                '}';
    }
}