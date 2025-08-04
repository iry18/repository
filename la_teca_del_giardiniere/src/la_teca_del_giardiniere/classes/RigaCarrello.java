package la_teca_del_giardiniere.classes;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

public class RigaCarrello implements Serializable {
    private int idProdotto;
    private String tipoProdotto; // "PIANTA" o "ACCESSORIO"
    private int quantita;
    private String nomeProdotto;
    private BigDecimal prezzoUnitario;

    // Costruttore
    public RigaCarrello(int idProdotto, String tipoProdotto, int quantita, String nomeProdotto, BigDecimal prezzoUnitario) {
        this.idProdotto = idProdotto;
        this.tipoProdotto = tipoProdotto;
        this.quantita = quantita;
        this.nomeProdotto = nomeProdotto;
        this.prezzoUnitario = prezzoUnitario;
    }

    // Getters e Setters
    public int getIdProdotto() { return idProdotto; }
    public void setIdProdotto(int idProdotto) { this.idProdotto = idProdotto; }
    public String getTipoProdotto() { return tipoProdotto; }
    public void setTipoProdotto(String tipoProdotto) { this.tipoProdotto = tipoProdotto; }
    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public String getNomeProdotto() { return nomeProdotto; }
    public void setNomeProdotto(String nomeProdotto) { this.nomeProdotto = nomeProdotto; }
    public BigDecimal getPrezzoUnitario() { return prezzoUnitario; }
    public void setPrezzoUnitario(BigDecimal prezzoUnitario) { this.prezzoUnitario = prezzoUnitario; }
    
    // Metodo per calcolare il totale della riga
    public BigDecimal getTotaleArticolo() {
        return prezzoUnitario.multiply(new BigDecimal(quantita));
    }
    
    // Metodo per incrementare la quantità
    public void incrementaQuantita(int aggiunta) {
        this.quantita += aggiunta;
    }

    // equals() e hashCode() basati su idProdotto e tipoProdotto
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RigaCarrello that = (RigaCarrello) o;
        return idProdotto == that.idProdotto && Objects.equals(tipoProdotto, that.tipoProdotto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProdotto, tipoProdotto);
    }

    @Override
    public String toString() {
        return "RigaCarrello{" +
                "idProdotto=" + idProdotto +
                ", tipoProdotto='" + tipoProdotto + '\'' +
                ", quantita=" + quantita +
                ", nomeProdotto='" + nomeProdotto + '\'' +
                ", prezzoUnitario=" + prezzoUnitario +
                '}';
    }
}