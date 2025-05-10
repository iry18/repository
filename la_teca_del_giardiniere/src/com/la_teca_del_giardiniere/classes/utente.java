package com.la_teca_del_giardiniere.classes;

import java.sql.Timestamp;

	public class utente {
		
		private String nome; 
		private String cognome; 
		private String email; 
		private String password;
		private boolean isAdmin;
		private String indirizzo;
		private String citta; 
		private int CAP; 
		private int telefono; 
		private Timestamp data_registrazione;
	 
		
		public utente(String nome, String cognome, String email, String password, boolean isAdmin, String indirizzo,
				String citta, int cAP, int telefono, Timestamp data_registrazione) {
			super();
			this.nome = nome;
			this.cognome = cognome;
			this.email = email;
			this.password = password;
			this.isAdmin = isAdmin;
			this.indirizzo = indirizzo;
			this.citta = citta;
			this.CAP = cAP;
			this.telefono = telefono;
			this.data_registrazione = data_registrazione;
		}
		
		

		public utente(String cognome, String email, String password, boolean isAdmin, String indirizzo, String citta,
				int cAP, int telefono, Timestamp data_registrazione) {
			super();
			this.cognome = cognome;
			this.email = email;
			this.password = password;
			this.isAdmin = isAdmin;
			this.indirizzo = indirizzo;
			this.citta = citta;
			this.CAP = cAP;
			this.telefono = telefono;
			this.data_registrazione = data_registrazione;
		}

		public utente() {
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
		
		public boolean isAdmin() {
			return isAdmin;
		}
		
		public void setAdmin(boolean isAdmin) {
			this.isAdmin = isAdmin;
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

		@Override
		public String toString() {
			return "registrazione [nome=" + nome + ", cognome=" + cognome + ", email=" + email + ", password=" + password
					+ ", isAdmin=" + isAdmin + ", indirizzo=" + indirizzo + ", citta=" + citta + ", CAP=" + CAP
					+ ", telefono=" + telefono + ", data_registrazione=" + data_registrazione + "]";
		}
		
		
}

