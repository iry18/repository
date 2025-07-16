package la_teca_del_giardiniere.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class Piante {
    private int id; // Corrisponde a P_CodProdotto
    private String nomeComune;
    private String tipo;
    private String nomeBotanico; // Corrisponde a NomeScientificoBotanico
    private String categoria;
    private String descrizioneBreve;
    private String descrizioneDettagliata; // Assicurati che esista
    private String esposizioneLuminosa;
    private String tipoDiTerreno;
    private String temperaturaIdeale; // <-- DEVE ESSERE STRING QUI
    private String frequenzaIrrigazione;
    private BigDecimal prezzo;
    private Integer quantitaDisponibile; // Corrisponde a Disponibilita, può essere null
    private Timestamp dataInserimento;
    private String urlImmagine; // Corrisponde a Immagine

    // Costruttore vuoto
    public Piante() {}

    // Getter e Setter (assicurati che setTemperaturaIdeale accetti una String)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeComune() { return nomeComune; }
    public void setNomeComune(String nomeComune) { this.nomeComune = nomeComune; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNomeBotanico() { return nomeBotanico; }
    public void setNomeBotanico(String nomeBotanico) { this.nomeBotanico = nomeBotanico; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescrizioneBreve() { return descrizioneBreve; }
    public void setDescrizioneBreve(String descrizioneBreve) { this.descrizioneBreve = descrizioneBreve; }

    public String getDescrizioneDettagliata() { return descrizioneDettagliata; }
    public void setDescrizioneDettagliata(String descrizioneDettagliata) { this.descrizioneDettagliata = descrizioneDettagliata; }

    public String getEsposizioneLuminosa() { return esposizioneLuminosa; }
    public void setEsposizioneLuminosa(String esposizioneLuminosa) { this.esposizioneLuminosa = esposizioneLuminosa; }

    public String getTipoDiTerreno() { return tipoDiTerreno; }
    public void setTipoDiTerreno(String tipoDiTerreno) { this.tipoDiTerreno = tipoDiTerreno; }

    // Questo è il setter cruciale: deve accettare una String
    public String getTemperaturaIdeale() { return temperaturaIdeale; }
    public void setTemperaturaIdeale(String temperaturaIdeale) { // <-- Qui deve essere String
        this.temperaturaIdeale = temperaturaIdeale;
    }

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

    // Override di equals e hashCode (opzionale ma consigliato per oggetti ben definiti)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piante piante = (Piante) o;
        return id == piante.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Override di toString (utile per il logging e il debugging)
    @Override
    public String toString() {
        return "Piante{" +
               "id=" + id +
               ", nomeComune='" + nomeComune + '\'' +
               ", tipo='" + tipo + '\'' +
               ", nomeBotanico='" + nomeBotanico + '\'' +
               ", categoria='" + categoria + '\'' +
               ", descrizioneBreve='" + descrizioneBreve + '\'' +
               ", descrizioneDettagliata='" + descrizioneDettagliata + '\'' +
               ", esposizioneLuminosa='" + esposizioneLuminosa + '\'' +
               ", tipoDiTerreno='" + tipoDiTerreno + '\'' +
               ", temperaturaIdeale='" + temperaturaIdeale + '\'' +
               ", frequenzaIrrigazione='" + frequenzaIrrigazione + '\'' +
               ", prezzo=" + prezzo +
               ", quantitaDisponibile=" + quantitaDisponibile +
               ", dataInserimento=" + dataInserimento +
               ", urlImmagine='" + urlImmagine + '\'' +
               '}';
    }
}