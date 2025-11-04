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
    private String difficolta;
    private String attrezziUtili;
    private String frequenzaLavorazione;
    private String potatura;
    private String concimazione;
    private String categoria;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piante piante = (Piante) o;
        return id == piante.id;
    }
    public String getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
    }

    public String getAttrezziUtili() {
        return attrezziUtili;
    }

    public void setAttrezziUtili(String attrezziUtili) {
        this.attrezziUtili = attrezziUtili;
    }

    public String getFrequenzaLavorazione() {
        return frequenzaLavorazione;
    }

    public void setFrequenzaLavorazione(String frequenzaLavorazione) {
        this.frequenzaLavorazione = frequenzaLavorazione;
    }

    public String getPotatura() {
        return potatura;
    }

    public void setPotatura(String potatura) {
        this.potatura = potatura;
    }

    public String getConcimazione() {
        return concimazione;
    }

    public void setConcimazione(String concimazione) {
        this.concimazione = concimazione;
    }
    
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

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
               ", categoria='" + categoria + '\'' +
               '}';
    }
}