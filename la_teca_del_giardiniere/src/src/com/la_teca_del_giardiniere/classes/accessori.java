//Classe JavaBean per l'Entità accessori

package src.com.la_teca_del_giardiniere.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class accessori {
    
	private Integer id;
	private String Nome;
	private BigDecimal prezzo;
	private int disponibilita;  
	private String descrizione;
	private String dimensioni;
	private Timestamp data_inserimento;
   

	public accessori() {
		
	}


	
	public accessori(int id, String nome, BigDecimal prezzo, int disponibilita, String descrizione, String dimensioni,
			Timestamp data_inserimento) {
		super();
		this.id = id;
		this.Nome = nome;
		this.prezzo = prezzo;
		this.disponibilita = disponibilita;
		this.descrizione = descrizione;
		this.dimensioni = dimensioni;
		this.data_inserimento = data_inserimento;
	}
	
	


	public accessori(String nome, BigDecimal prezzo, int disponibilita, String descrizione, String dimensioni,
			Timestamp data_inserimento) {
		super();
		this.Nome = nome;
		this.prezzo = prezzo;
		this.disponibilita = disponibilita;
		this.descrizione = descrizione;
		this.dimensioni = dimensioni;
		this.data_inserimento = data_inserimento;
	}



	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


	public String getNome() {
		return Nome;
	}


	public void setNome(String nome) {
		Nome = nome;
	}


	public BigDecimal isPrezzo() {
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
	

	public String getDescrizione() {
		return descrizione;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public String getDimensioni() {
		return dimensioni;
	}


	public void setDimensioni(String dimensioni) {
		this.dimensioni = dimensioni;
	}


	public Timestamp getData_inserimento() {
		return data_inserimento;
	}


	public void setData_inserimento(Timestamp data_inserimento) {
		this.data_inserimento = data_inserimento;
	}


	@Override
	public String toString() {
		return "accessori [id=" + id + ", Nome=" + Nome + ", prezzo=" + prezzo + ", disponibilita=" + disponibilita
				+ ", descrizione=" + descrizione + ", dimensioni=" + dimensioni + ", data_inserimento="
				+ data_inserimento + "]";
	}

	
}
	

