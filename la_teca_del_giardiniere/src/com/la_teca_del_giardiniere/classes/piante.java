//Classe JavaBean per l'Entità piante
//Model: (Responsabile della gestione dei dati e della logica di business)
package com.la_teca_del_giardiniere.classes;

import java.sql.Timestamp;

public class piante {

	private String NomeComune;
	private boolean tipo;
	private String NomeScientificoBotanico;  
	private String Categoria;
	private String DescrizioneBreve;
	private String DescrizioneDettagliata;
	private String EsposizioneLuminosa;
	private String TipoDiTerreno;
	private int TemperaturaIdeale; 
	private String FrequenzaIrrigazione;
	private float Prezzo;
	private int Disponibilita;
	private Timestamp data_inserimento;
	
	public piante() {
		
	}

	
	public piante(String nomeComune, boolean tipo, String nomeScientificoBotanico, String categoria,
			String descrizioneBreve, String descrizioneDettagliata, String esposizioneLuminosa, String tipoDiTerreno,
			int temperaturaIdeale, String frequenzaIrrigazione, float prezzo, int disponibilita,
			Timestamp data_inserimento) {
		super();
		this.NomeComune = nomeComune;
		this.tipo = tipo;
		this.NomeScientificoBotanico = nomeScientificoBotanico;
		this.Categoria = categoria;
		this.DescrizioneBreve = descrizioneBreve;
		this.DescrizioneDettagliata = descrizioneDettagliata;
		this.EsposizioneLuminosa = esposizioneLuminosa;
		this.TipoDiTerreno = tipoDiTerreno;
		this.TemperaturaIdeale = temperaturaIdeale;
		this.FrequenzaIrrigazione = frequenzaIrrigazione;
		this.Prezzo = prezzo;
		this.Disponibilita = disponibilita;
		this.data_inserimento = data_inserimento;
	}
	
	
	public piante(boolean tipo, String nomeScientificoBotanico, String categoria, String descrizioneBreve,
			String descrizioneDettagliata, String esposizioneLuminosa, String tipoDiTerreno, int temperaturaIdeale,
			String frequenzaIrrigazione, float prezzo, int disponibilita, Timestamp data_inserimento) {
		super();
		this.tipo = tipo;
		this.NomeScientificoBotanico = nomeScientificoBotanico;
		this.Categoria = categoria;
		this.DescrizioneBreve = descrizioneBreve;
		this.DescrizioneDettagliata = descrizioneDettagliata;
		this.EsposizioneLuminosa = esposizioneLuminosa;
		this.TipoDiTerreno = tipoDiTerreno;
		this.TemperaturaIdeale = temperaturaIdeale;
		this.FrequenzaIrrigazione = frequenzaIrrigazione;
		this.Prezzo = prezzo;
		this.Disponibilita = disponibilita;
		this.data_inserimento = data_inserimento;
	}



	public String getNomeComune() {
		return NomeComune;
	}




	public void setNomeComune(String nomeComune) {
		NomeComune = nomeComune;
	}




	public boolean isTipo() {
		return tipo;
	}




	public void setTipo(boolean tipo) {
		this.tipo = tipo;
	}




	public String getNomeScientificoBotanico() {
		return NomeScientificoBotanico;
	}




	public void setNomeScientificoBotanico(String nomeScientificoBotanico) {
		NomeScientificoBotanico = nomeScientificoBotanico;
	}




	public String getCategoria() {
		return Categoria;
	}




	public void setCategoria(String categoria) {
		Categoria = categoria;
	}




	public String getDescrizioneBreve() {
		return DescrizioneBreve;
	}




	public void setDescrizioneBreve(String descrizioneBreve) {
		DescrizioneBreve = descrizioneBreve;
	}




	public String getDescrizioneDettagliata() {
		return DescrizioneDettagliata;
	}




	public void setDescrizioneDettagliata(String descrizioneDettagliata) {
		DescrizioneDettagliata = descrizioneDettagliata;
	}




	public String getEsposizioneLuminosa() {
		return EsposizioneLuminosa;
	}




	public void setEsposizioneLuminosa(String esposizioneLuminosa) {
		EsposizioneLuminosa = esposizioneLuminosa;
	}




	public String getTipoDiTerreno() {
		return TipoDiTerreno;
	}




	public void setTipoDiTerreno(String tipoDiTerreno) {
		TipoDiTerreno = tipoDiTerreno;
	}




	public int getTemperaturaIdeale() {
		return TemperaturaIdeale;
	}




	public void setTemperaturaIdeale(int temperaturaIdeale) {
		TemperaturaIdeale = temperaturaIdeale;
	}




	public String getFrequenzaIrrigazione() {
		return FrequenzaIrrigazione;
	}




	public void setFrequenzaIrrigazione(String frequenzaIrrigazione) {
		FrequenzaIrrigazione = frequenzaIrrigazione;
	}




	public float getPrezzo() {
		return Prezzo;
	}




	public void setPrezzo(float prezzo) {
		Prezzo = prezzo;
	}




	public int getDisponibilita() {
		return Disponibilita;
	}




	public void setDisponibilita(int disponibilita) {
		Disponibilita = disponibilita;
	}




	public Timestamp getData_inserimento() {
		return data_inserimento;
	}




	public void setData_inserimento(Timestamp data_inserimento) {
		this.data_inserimento = data_inserimento;
	}




	@Override
	public String toString() {
		return "piante [NomeComune=" + NomeComune + ", tipo=" + tipo + ", NomeScientificoBotanico="
				+ NomeScientificoBotanico + ", Categoria=" + Categoria + ", DescrizioneBreve=" + DescrizioneBreve
				+ ", DescrizioneDettagliata=" + DescrizioneDettagliata + ", EsposizioneLuminosa=" + EsposizioneLuminosa
				+ ", TipoDiTerreno=" + TipoDiTerreno + ", TemperaturaIdeale=" + TemperaturaIdeale
				+ ", FrequenzaIrrigazione=" + FrequenzaIrrigazione + ", Prezzo=" + Prezzo + ", Disponibilita="
				+ Disponibilita + ", data_inserimento=" + data_inserimento + "]";
	}
	
	//costruttore con i campi


}
	
