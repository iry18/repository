package la_teca_del_giardiniere.classes;

import java.math.BigDecimal;

public class DettaglioOrdine {
    private int dettaglio_id; // Corresponds to dettaglio_id in DB
    private int ordine_id;
    private Integer p_codprodotto; // Use Integer to allow null for plant product code
    private Integer accessorio_id; // Use Integer to allow null for accessory ID
    private BigDecimal prezzo_unitario;
    private int quantita;
    
    
    public DettaglioOrdine() {
    }

    public DettaglioOrdine(int id, int ordineId, Integer prodottoId, Integer accessorioId,
                           BigDecimal prezzoUnitario, int quantita, BigDecimal totaleDettaglio,
                           String nomeProdotto, String tipoProdotto) {
        this.dettaglio_id = id;
        this.ordine_id = ordineId;
        this.p_codprodotto = prodottoId;
        this.accessorio_id = accessorioId;
        this.prezzo_unitario = prezzoUnitario;
        this.quantita = quantita;
    }

    // --- Getters and Setters ---
    public int getId() {
        return dettaglio_id;
    }

    public void setId(int id) {
        this.dettaglio_id = id;
    }

    public int getOrdineId() {
        return ordine_id;
    }

    public void setOrdineId(int ordineId) {
        this.ordine_id = ordineId;
    }

    public Integer getProdottoId() {
        return p_codprodotto;
    }

    public void setProdottoId(Integer prodottoId) {
        this.p_codprodotto = prodottoId;
    }

    public Integer getAccessorioId() {
        return accessorio_id;
    }

    public void setAccessorioId(Integer accessorioId) {
        this.accessorio_id = accessorioId;
    }

    public BigDecimal getPrezzoUnitario() {
        return prezzo_unitario;
    }

    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzo_unitario = prezzoUnitario;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }



    @Override
    public String toString() {
        return "DettaglioOrdine{" +
               "id=" + dettaglio_id +
               ", ordineId=" + ordine_id +
               ", prodottoId=" + p_codprodotto +
               ", accessorioId=" + accessorio_id +
               ", prezzoUnitario=" + prezzo_unitario +
               ", quantita=" + quantita + '\'' +
               '}';
    }
}