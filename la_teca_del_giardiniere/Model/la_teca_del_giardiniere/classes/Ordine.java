package la_teca_del_giardiniere.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ordine {
    private int ordineId;
    private int utenteId;
    private Timestamp dataOrdine;
    private String cittaSpedizion;
    private String paeseSpedizione;
    private String capSpedizione;
    private String cittaSpedizione;
    private BigDecimal totaleOrdine;
    private String metodoPagamento;
    private BigDecimal iva;
    private String statoOrdine;
    private String note;
    private String nomeSpedizione;
    private String cognomeSpedizione;
    private String indirizzoSpedizione;
    private String telefonoSpedizione;
    private String emailSpedizione;

    public Ordine(int ordineId, int utenteId, Timestamp dataOrdine, String cittaSpedizione, String paeseSpedizione, String capSpedizione, BigDecimal totaleOrdine, String metodoPagamento, BigDecimal iva, String statoOrdine, String note, String nomeSpedizione, String cognomeSpedizione, String indirizzoSpedizione, String telefonoSpedizione, String emailSpedizione) {
        this.ordineId = ordineId;
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
        this.nomeSpedizione = nomeSpedizione;
        this.cognomeSpedizione = cognomeSpedizione;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.telefonoSpedizione = telefonoSpedizione;
        this.emailSpedizione = emailSpedizione;
    }
    
    // Costruttore semplificato (per il salvataggio nel DB)
    public Ordine() {}

    // Getters e Setters
    public int getOrdineId() {
        return ordineId;
    }

    public void setOrdineId(int ordineId) {
        this.ordineId = ordineId;
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
    
    // Missing getters and setters for shipping details
    public String getNomeSpedizione() {
        return nomeSpedizione;
    }

    public void setNomeSpedizione(String nomeSpedizione) {
        this.nomeSpedizione = nomeSpedizione;
    }
    
    public String getCognomeSpedizione() {
        return cognomeSpedizione;
    }

    public void setCognomeSpedizione(String cognomeSpedizione) {
        this.cognomeSpedizione = cognomeSpedizione;
    }

    public String getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    public void setIndirizzoSpedizione(String indirizzoSpedizione) {
        this.indirizzoSpedizione = indirizzoSpedizione;
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
    
    public String getTelefonoSpedizione() {
        return telefonoSpedizione;
    }

    public void setTelefonoSpedizione(String telefonoSpedizione) {
        this.telefonoSpedizione = telefonoSpedizione;
    }
    
    public String getEmailSpedizione() {
        return emailSpedizione;
    }

    public void setEmailSpedizione(String emailSpedizione) {
        this.emailSpedizione = emailSpedizione;
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
}