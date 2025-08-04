package la_teca_del_giardiniere.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class Piante {
    private int id; 
    private String NomeComune;
    private String tipo;
    private String NomeScientificoBotanico; 
    private String descrizioneBreve;
    private String descrizioneDettagliata; 
    private String esposizioneLuminosa;
    private String tipoDiTerreno;
    private String temperaturaIdeale; 
    private String frequenzaIrrigazione;
    private BigDecimal prezzo;
    private Integer disponibilita; 
    private Timestamp dataInserimento;
    private String Immagine; 

    // Costruttore vuoto
    public Piante() {}

    // Getter e Setter 
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeComune() { return NomeComune; }
    public void setNomeComune(String nomeComune) { this.NomeComune = nomeComune; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNomeScientificoBotanico() { return NomeScientificoBotanico; }
    public void setNomeScientificoBotanico(String nomeScientificoBotanico) { this.NomeScientificoBotanico = nomeScientificoBotanico; }

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

    public Integer getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(Integer disponibilita) {
        this.disponibilita = disponibilita;
    }

    public Timestamp getDataInserimento() { return dataInserimento; }
    public void setDataInserimento(Timestamp dataInserimento) { this.dataInserimento = dataInserimento; }

    public String getImmagine() { return Immagine; }
    public void setImmagine(String Immagine) { this.Immagine = Immagine; }

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
               ", nomeComune='" + NomeComune + '\'' +
               ", tipo='" + tipo + '\'' +
               ", NomeScientificoBotanico='" + NomeScientificoBotanico + '\'' +
               ", descrizioneBreve='" + descrizioneBreve + '\'' +
               ", descrizioneDettagliata='" + descrizioneDettagliata + '\'' +
               ", esposizioneLuminosa='" + esposizioneLuminosa + '\'' +
               ", tipoDiTerreno='" + tipoDiTerreno + '\'' +
               ", temperaturaIdeale='" + temperaturaIdeale + '\'' +
               ", frequenzaIrrigazione='" + frequenzaIrrigazione + '\'' +
               ", prezzo=" + prezzo +
               ", disponibilita=" + disponibilita +
               ", dataInserimento=" + dataInserimento +
               ", Immagine='" + Immagine + '\'' +
               '}';
    }
}