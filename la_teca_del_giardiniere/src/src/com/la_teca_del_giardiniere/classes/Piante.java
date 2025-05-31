/**Classe JavaBean per l'Entità Pianta
 * */
package src.com.la_teca_del_giardiniere.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Piante {

    private int id;
    private String nomeComune;
    private String nomeBotanico; // Nome Scientifico Botanico
    private String categoria;
    private String descrizione; // Puoi unire breve e dettagliata, o crearne due
    private String esposizioneLuminosa;
    private String tipoDiTerreno;
    private Integer temperaturaIdeale; // Usa Integer per permettere valori null
    private String frequenzaIrrigazione;
    private BigDecimal prezzo; // Usa BigDecimal
    private Integer quantitaDisponibile; // Usa Integer per permettere valori null
    private Timestamp dataInserimento; // Ho rinominato per chiarezza e per allinearsi a data_registrazione in Utente
    private String urlImmagine; // Campo immagine

    // Ho cambiato il tipo di pianta da boolean a String per maggiore flessibilità
    private String tipo; // Es: "interno", "esterno"

    // Costruttore vuoto
    public Piante() {}

    // Costruttore completo (adattalo alle tue esigenze)
    public Piante(int id, String nomeComune, String tipo, String nomeBotanico, String categoria,
                  String descrizione, String esposizioneLuminosa, String tipoDiTerreno,
                  Integer temperaturaIdeale, String frequenzaIrrigazione, BigDecimal prezzo,
                  Integer quantitaDisponibile, Timestamp dataInserimento, String urlImmagine) {
        this.id = id;
        this.nomeComune = nomeComune;
        this.tipo = tipo;
        this.nomeBotanico = nomeBotanico;
        this.categoria = categoria;
        this.descrizione = descrizione;
        this.esposizioneLuminosa = esposizioneLuminosa;
        this.tipoDiTerreno = tipoDiTerreno;
        this.temperaturaIdeale = temperaturaIdeale;
        this.frequenzaIrrigazione = frequenzaIrrigazione;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
        this.dataInserimento = dataInserimento;
        this.urlImmagine = urlImmagine;
    }

    // --- Getter e Setter per tutti i campi ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeComune() { return nomeComune; }
    public void setNomeComune(String nomeComune) { this.nomeComune = nomeComune; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; } // Setter per il tipo (String)
    public String getNomeBotanico() { return nomeBotanico; }
    public void setNomeBotanico(String nomeBotanico) { this.nomeBotanico = nomeBotanico; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public String getEsposizioneLuminosa() { return esposizioneLuminosa; }
    public void setEsposizioneLuminosa(String esposizioneLuminosa) { this.esposizioneLuminosa = esposizioneLuminosa; }
    public String getTipoDiTerreno() { return tipoDiTerreno; }
    public void setTipoDiTerreno(String tipoDiTerreno) { this.tipoDiTerreno = tipoDiTerreno; }
    public Integer getTemperaturaIdeale() { return temperaturaIdeale; }
    public void setTemperaturaIdeale(Integer temperaturaIdeale) { this.temperaturaIdeale = temperaturaIdeale; }
    public String getFrequenzaIrrigazione() { return frequenzaIrrigazione; }
    public void setFrequenzaIrrigazione(String frequenzaIrrigazione) { this.frequenzaIrrigazione = frequenzaIrrigazione; }
    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }
    public Integer getQuantitaDisponibile() { return quantitaDisponibile; }
    public void setQuantitaDisponibile(Integer quantitaDisponibile) { this.quantitaDisponibile = quantitaDisponibile; }
    public Timestamp getDataInserimento() { return dataInserimento; }
    public void setDataInserimento(Timestamp dataInserimento) { this.dataInserimento = dataInserimento; }
    public String getUrlImmagine() { return urlImmagine; }
    public void setUrlImmagine(String urlImmagine) { this.urlImmagine = urlImmagine; }

    @Override
    public String toString() {
        return "Pianta [id=" + id + ", nomeComune=" + nomeComune + ", tipo=" + tipo + ", prezzo=" + prezzo
                + ", quantitaDisponibile=" + quantitaDisponibile + "]";
    }
} 
