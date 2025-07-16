package la_teca_del_giardiniere.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// Import corretto: assicurati che il percorso del package per DettaglioOrdine sia questo
import la_teca_del_giardiniere.classes.DettaglioOrdine;

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
    private List<DettaglioOrdine> dettagliOrdine; // Nome del campo consistente

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
        this.dettagliOrdine = new ArrayList<>();
    }

    // Costruttore senza ID (per nuovo inserimento)
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
        this.dettagliOrdine = new ArrayList<>();
    }

    // Getters and Setters

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

    public List<DettaglioOrdine> getDettagliOrdine() { // Getter corretto
        return dettagliOrdine;
    }

    public void setDettagliOrdine(List<DettaglioOrdine> dettagliOrdine) { // Setter corretto
        this.dettagliOrdine = dettagliOrdine;
    }

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