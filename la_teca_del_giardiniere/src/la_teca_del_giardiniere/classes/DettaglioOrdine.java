// File: la_teca_del_giardiniere/classes/DettaglioOrdine.java
package la_teca_del_giardiniere.classes;

import java.math.BigDecimal;

public class DettaglioOrdine {
    private int id; // Corresponds to dettaglio_id in DB
    private int ordineId;
    private Integer prodottoId; // Use Integer to allow null for plant product code
    private Integer accessorioId; // Use Integer to allow null for accessory ID
    private BigDecimal prezzoUnitario;
    private int quantita;
    private BigDecimal totaleDettaglio;
    private String nomeProdotto; // To store the resolved product name (plant or accessory)
    private String tipoProdotto; // "pianta" or "accessorio"

    public DettaglioOrdine() {
    }

    public DettaglioOrdine(int id, int ordineId, Integer prodottoId, Integer accessorioId,
                           BigDecimal prezzoUnitario, int quantita, BigDecimal totaleDettaglio,
                           String nomeProdotto, String tipoProdotto) {
        this.id = id;
        this.ordineId = ordineId;
        this.prodottoId = prodottoId;
        this.accessorioId = accessorioId;
        this.prezzoUnitario = prezzoUnitario;
        this.quantita = quantita;
        this.totaleDettaglio = totaleDettaglio;
        this.nomeProdotto = nomeProdotto;
        this.tipoProdotto = tipoProdotto;
    }

    // --- Getters and Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdineId() {
        return ordineId;
    }

    public void setOrdineId(int ordineId) {
        this.ordineId = ordineId;
    }

    public Integer getProdottoId() {
        return prodottoId;
    }

    public void setProdottoId(Integer prodottoId) {
        this.prodottoId = prodottoId;
    }

    public Integer getAccessorioId() {
        return accessorioId;
    }

    public void setAccessorioId(Integer accessorioId) {
        this.accessorioId = accessorioId;
    }

    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public BigDecimal getTotaleDettaglio() {
        return totaleDettaglio;
    }

    public void setTotaleDettaglio(BigDecimal totaleDettaglio) {
        this.totaleDettaglio = totaleDettaglio;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public String getTipoProdotto() {
        return tipoProdotto;
    }

    public void setTipoProdotto(String tipoProdotto) {
        this.tipoProdotto = tipoProdotto;
    }

    @Override
    public String toString() {
        return "DettaglioOrdine{" +
               "id=" + id +
               ", ordineId=" + ordineId +
               ", prodottoId=" + prodottoId +
               ", accessorioId=" + accessorioId +
               ", prezzoUnitario=" + prezzoUnitario +
               ", quantita=" + quantita +
               ", totaleDettaglio=" + totaleDettaglio +
               ", nomeProdotto='" + nomeProdotto + '\'' +
               ", tipoProdotto='" + tipoProdotto + '\'' +
               '}';
    }
}