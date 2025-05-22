package src.com.la_teca_del_giardiniere.classes;

import java.sql.Timestamp;
import java.util.List;

public class registrazione {
	
	private int utente_id;
	private String nome; 
	private String cognome; 
	private String email; 
	private String password;
	private String indirizzo;
	private String citta; 
	private int CAP; 
	private int telefono; 
	private Timestamp data_registrazione;
	private String provincia;
	private List<String> ruoli;
 
	
	public registrazione(String nome, String cognome, String email, String password, String indirizzo,
			String citta, int cAP, int telefono, Timestamp data_registrazione, String provincia) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.indirizzo = indirizzo;
		this.citta = citta;
		this.CAP = cAP;
		this.telefono = telefono;
		this.data_registrazione = data_registrazione;
		this.provincia = provincia;
	}
	
	

	public registrazione(int utente_id, String cognome, String email, String password, boolean isAdmin, String indirizzo, String citta,
			int cAP, int telefono, Timestamp data_registrazione, String provincia) {
		super();
		
		this.utente_id = utente_id;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.indirizzo = indirizzo;
		this.citta = citta;
		this.CAP = cAP;
		this.telefono = telefono;
		this.data_registrazione = data_registrazione;
		this.provincia = provincia;
	}



	public registrazione() {
	}

	public int getUtente_id() {
	    return utente_id;
	}

	public void setUtente_id(int utente_id) {
	    this.utente_id = utente_id;
	}

	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getCognome() {
		return cognome;
	}



	public void setCognome(String cognome) {
		this.cognome = cognome;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getIndirizzo() {
		return indirizzo;
	}



	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}



	public String getCitta() {
		return citta;
	}



	public void setCitta(String citta) {
		this.citta = citta;
	}



	public int getCAP() {
		return CAP;
	}



	public void setCAP(int cAP) {
		CAP = cAP;
	}



	public int getTelefono() {
		return telefono;
	}



	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}



	public Timestamp getData_registrazione() {
		return data_registrazione;
	}



	public void setData_registrazione(Timestamp data_registrazione) {
		this.data_registrazione = data_registrazione;
	}



	public String getProvincia() {
		return provincia;
	}



	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public List<String> getRuoli() {
	    return ruoli;
	}

	public void setRuoli(List<String> ruoli) {
	    this.ruoli = ruoli;
	}



	@Override
	public String toString() {
		return "registrazione [utente_id=" + utente_id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email
				+ ", password=" + password + ", indirizzo=" + indirizzo + ", citta=" + citta + ", CAP=" + CAP
				+ ", telefono=" + telefono + ", data_registrazione=" + data_registrazione + ", provincia=" + provincia
				+ "]";
	}



	

	
	
	
	
}



