package src.com.la_teca_del_giardiniere.classes; 

import java.math.BigDecimal;
import java.sql.Timestamp;


public class Accessori {

    private Integer id;
    private String nome;  
    private BigDecimal prezzo;
    private int disponibilita;
    private String descrizioneBreve; 
    private String descrizioneDettagliata; 
    private String dimensioni;
    private String immagine; 
    private String categoria; 
    private Timestamp dataInserimento; 
    
    public Accessori() {
    }

   
    public Accessori(String nome, BigDecimal prezzo, int disponibilita,
                       String descrizioneBreve, String descrizioneDettagliata,
                       String dimensioni, String immagine, String categoria,
                       Timestamp dataInserimento) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.disponibilita = disponibilita;
        this.descrizioneBreve = descrizioneBreve;
        this.descrizioneDettagliata = descrizioneDettagliata;
        this.dimensioni = dimensioni;
        this.immagine = immagine;
        this.categoria = categoria;
        this.dataInserimento = dataInserimento;
    }

    // Costruttore completo (con ID, per recupero e aggiornamento)
    public Accessori(Integer id, String nome, BigDecimal prezzo, int disponibilita,
                       String descrizioneBreve, String descrizioneDettagliata,
                       String dimensioni, String immagine, String categoria,
                       Timestamp dataInserimento) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.disponibilita = disponibilita;
        this.descrizioneBreve = descrizioneBreve;
        this.descrizioneDettagliata = descrizioneDettagliata;
        this.dimensioni = dimensioni;
        this.immagine = immagine;
        this.categoria = categoria;
        this.dataInserimento = dataInserimento;
    }

    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrezzo() { // Rinominato da isPrezzo() a getPrezzo()
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public int getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(int disponibilita) {
        this.disponibilita = disponibilita;
    }

    public String getDescrizioneBreve() {
        return descrizioneBreve;
    }

    public void setDescrizioneBreve(String descrizioneBreve) {
        this.descrizioneBreve = descrizioneBreve;
    }

    public String getDescrizioneDettagliata() {
        return descrizioneDettagliata;
    }

    public void setDescrizioneDettagliata(String descrizioneDettagliata) {
        this.descrizioneDettagliata = descrizioneDettagliata;
    }

    public String getDimensioni() {
        return dimensioni;
    }

    public void setDimensioni(String dimensioni) {
        this.dimensioni = dimensioni;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Timestamp getDataInserimento() { // Rinominato da getData_inserimento() a getDataInserimento()
        return dataInserimento;
    }

    public void setDataInserimento(Timestamp dataInserimento) { // Rinominato da setData_inserimento() a setDataInserimento()
        this.dataInserimento = dataInserimento;
    }

    @Override
    public String toString() {
        return "Accessorio [id=" + id + ", nome=" + nome + ", prezzo=" + prezzo + ", disponibilita=" + disponibilita
                + ", descrizioneBreve=" + descrizioneBreve + ", descrizioneDettagliata=" + descrizioneDettagliata
                + ", dimensioni=" + dimensioni + ", immagine=" + immagine + ", categoria=" + categoria
                + ", dataInserimento=" + dataInserimento + "]";
    }
}

