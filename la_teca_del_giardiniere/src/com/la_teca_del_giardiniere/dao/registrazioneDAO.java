package com.la_teca_del_giardiniere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.la_teca_del_giardiniere.classes.registrazione; // Assicurati che l'import sia corretto
import com.mysql.cj.jdbc.MysqlDataSource;


public class registrazioneDAO {

	    private MysqlDataSource dataSource;

	    public registrazioneDAO() throws SQLException {
	        dataSource = new MysqlDataSource();
	        dataSource.setServerName("localhost");
	        dataSource.setPort(3306);
	        dataSource.setUser("root");
	        dataSource.setPassword("root");
	        dataSource.setDatabaseName("la_teca_del_giardiniere");
	        dataSource.setUseSSL(false);
	        dataSource.setAllowPublicKeyRetrieval(true);
	    }
	 // Metodo per aggiungere un nuovo utente registrato nel database
	    public void aggiungiUtenteRegistrato(registrazione utente) throws SQLException {
	        String sql = "INSERT INTO utente (nome, cognome, email, password, isAdmin, indirizzo, citta, CAP, telefono, data_registrazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        try (Connection connection = dataSource.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	            preparedStatement.setString(1, utente.getNome());
	            preparedStatement.setString(2, utente.getCognome());
	            preparedStatement.setString(3, utente.getEmail());
	            preparedStatement.setString(4, utente.getPassword());
	            preparedStatement.setBoolean(5, utente.isAdmin());
	            preparedStatement.setString(6, utente.getIndirizzo());
	            preparedStatement.setString(7, utente.getCitta());
	            preparedStatement.setInt(8, utente.getCAP());
	            preparedStatement.setInt(9, utente.getTelefono());
	            preparedStatement.setTimestamp(10, utente.getData_registrazione());

	            preparedStatement.executeUpdate();
	        }
	    }

	    // Potresti aggiungere altri metodi qui, ad esempio per verificare se un'email è già registrata

	}