package src.com.la_teca_del_giardiniere.classes;

import java.math.BigDecimal;

public class DettaglioOrdine {
    private int id; // ID del dettaglio ordine
    private int ordineId; // ID dell'ordine a cui questo dettaglio appartiene
    private Integer prodottoId; // ID del prodotto (pianta), può essere null
    private Integer accessorioId; // ID dell'accessorio, può essere null
    private String tipoProdotto; // Es: "pianta", "accessorio" (utile per distinguere)
    private String nomeProdotto; // Nome del prodotto al momento dell'acquisto
    private int quantita;
    private BigDecimal prezzoUnitario; // Prezzo del singolo articolo al momento dell'acquisto
    private BigDecimal totaleDettaglio; // Totale per questo dettaglio (quantita * prezzoUnitario)

    // Costruttore vuoto
    public DettaglioOrdine() {
    }

    // Costruttore con ID (per recupero dal DB)
    public DettaglioOrdine(int id, int ordineId, Integer prodottoId, Integer accessorioId, String tipoProdotto, String nomeProdotto,
                           int quantita, BigDecimal prezzoUnitario, BigDecimal totaleDettaglio) {
        this.id = id;
        this.ordineId = ordineId;
        this.prodottoId = prodottoId;
        this.accessorioId = accessorioId;
        this.tipoProdotto = tipoProdotto;
        this.nomeProdotto = nomeProdotto;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
        this.totaleDettaglio = totaleDettaglio;
    }

    // Costruttore senza ID (per inserimento nel DB)
    public DettaglioOrdine(int ordineId, Integer prodottoId, Integer accessorioId, String tipoProdotto, String nomeProdotto,
                           int quantita, BigDecimal prezzoUnitario, BigDecimal totaleDettaglio) {
        this.ordineId = ordineId;
        this.prodottoId = prodottoId;
        this.accessorioId = accessorioId;
        this.tipoProdotto = tipoProdotto;
        this.nomeProdotto = nomeProdotto;
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
        this.totaleDettaglio = totaleDettaglio;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOrdineId() { return ordineId; }
    public void setOrdineId(int ordineId) { this.ordineId = ordineId; }
    public Integer getProdottoId() { return prodottoId; } // Ora restituisce Integer
    public void setProdottoId(Integer prodottoId) { this.prodottoId = prodottoId; } // Accetta Integer
    public Integer getAccessorioId() { return accessorioId; } // Ora restituisce Integer
    public void setAccessorioId(Integer accessorioId) { this.accessorioId = accessorioId; } // Accetta Integer
    public String getTipoProdotto() { return tipoProdotto; }
    public void setTipoProdotto(String tipoProdotto) { this.tipoProdotto = tipoProdotto; }
    public String getNomeProdotto() { return nomeProdotto; }
    public void setNomeProdotto(String nomeProdotto) { this.nomeProdotto = nomeProdotto; }
    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public BigDecimal getPrezzoUnitario() { return prezzoUnitario; }
    public void setPrezzoUnitario(BigDecimal prezzoUnitario) { this.prezzoUnitario = prezzoUnitario; }
    public BigDecimal getTotaleDettaglio() { return totaleDettaglio; }
    public void setTotaleDettaglio(BigDecimal totaleDettaglio) { this.totaleDettaglio = totaleDettaglio; }

    @Override
    public String toString() {
        return "DettaglioOrdine{" +
               "id=" + id +
               ", ordineId=" + ordineId +
               ", prodottoId=" + prodottoId +
               ", accessorioId=" + accessorioId +
               ", tipoProdotto='" + tipoProdotto + '\'' +
               ", nomeProdotto='" + nomeProdotto + '\'' +
               ", quantita=" + quantita +
               ", prezzoUnitario=" + prezzoUnitario +
               ", totaleDettaglio=" + totaleDettaglio +
               '}';
    }
}