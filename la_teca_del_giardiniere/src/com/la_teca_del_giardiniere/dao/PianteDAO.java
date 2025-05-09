package com.la_teca_del_giardiniere.dao; 

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.la_teca_del_giardiniere.classes.piante;
import com.mysql.cj.jdbc.MysqlDataSource;

public class PianteDAO {

    private MysqlDataSource dataSource;

    public PianteDAO() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("la_teca_del_giardiniere");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
    }
    
    
 // Metodo per aggiungere una nuova pianta nel database
    public void aggiungiPianta(piante pianta) throws SQLException {
        String sql = "INSERT INTO piante(NomeComune,Tipo, NomeScientificoBotanico, Categoria, DescrizioneBreve, DescrizioneDettagliata, EsposizioneLuminosa, TipoDiTerreno, TemperaturaIdeale, FrequenzaIrrigazione, Prezzo, Disponibilita, Data_inserimento ) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setBoolean(2, pianta.isTipo());
            preparedStatement.setString(3, pianta.getNomeScientificoBotanico());
            preparedStatement.setString(4, pianta.getCategoria());
            preparedStatement.setString(5, pianta.getDescrizioneBreve());
            preparedStatement.setString(6, pianta.getDescrizioneDettagliata());
            preparedStatement.setString(7, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(8, pianta.getTipoDiTerreno());
            preparedStatement.setInt(9, pianta.getTemperaturaIdeale());
            preparedStatement.setString(10, pianta.getFrequenzaIrrigazione());
            preparedStatement.setFloat(11, pianta.getPrezzo());
            preparedStatement.setInt(12, pianta.getDisponibilita());
            preparedStatement.setTimestamp(13, pianta.getData_inserimento());

            preparedStatement.executeUpdate();
        }
    }

    // Metodo per recuperare una pianta dal database tramite il suo NomeComune (esempio)
    public piante getPiantaByNomeComune(String nomeComune) throws SQLException {
        String sql = "SELECT * FROM piante WHERE NomeComune = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nomeComune);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPianta(resultSet);
                } else {
                    return null; // Nessuna pianta trovata con quel nome comune
                }
            }
        }
    }

    // Metodo per recuperare tutte le piante dal database
    public List<piante> getAllPiante() throws SQLException {
        String sql = "SELECT * FROM piante";
        List<piante> pianteList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                pianteList.add(mapResultSetToPianta(resultSet));
            }
        }
        return pianteList;
    }

    // Metodo per aggiornare i dati di una pianta nel database
    public void aggiornaPianta(piante pianta) throws SQLException {
        String sql = "UPDATE piante SET Tipo = ?, NomeScientificoBotanico = ?, Categoria = ?, DescrizioneBreve = ?, DescrizioneDettagliata = ?, EsposizioneLuminosa = ?, TipoDiTerreno = ?, TemperaturaIdeale = ?, FrequenzaIrrigazione = ?, Prezzo = ?, Disponibilita = ?, Data_inserimento = ? WHERE NomeComune = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBoolean(1, pianta.isTipo());
            preparedStatement.setString(2, pianta.getNomeScientificoBotanico());
            preparedStatement.setString(3, pianta.getCategoria());
            preparedStatement.setString(4, pianta.getDescrizioneBreve());
            preparedStatement.setString(5, pianta.getDescrizioneDettagliata());
            preparedStatement.setString(6, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(7, pianta.getTipoDiTerreno());
            preparedStatement.setInt(8, pianta.getTemperaturaIdeale());
            preparedStatement.setString(9, pianta.getFrequenzaIrrigazione());
            preparedStatement.setFloat(10, pianta.getPrezzo());
            preparedStatement.setInt(11, pianta.getDisponibilita());
            preparedStatement.setTimestamp(12, pianta.getData_inserimento());
            preparedStatement.setString(13, pianta.getNomeComune()); // Condizione WHERE

            preparedStatement.executeUpdate();
        }
    }

    // Metodo per eliminare una pianta dal database tramite il suo NomeComune (esempio)
    public void eliminaPianta(String nomeComune) throws SQLException {
        String sql = "DELETE FROM piante WHERE NomeComune = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nomeComune);
            preparedStatement.executeUpdate();
        }
    }

    // Metodo privato per mappare una riga del ResultSet a un oggetto Piante
    private piante mapResultSetToPianta(ResultSet resultSet) throws SQLException {
        piante pianta = new piante();
        pianta.setNomeComune(resultSet.getString("NomeComune"));
        pianta.setTipo(resultSet.getBoolean("Tipo"));
        pianta.setNomeScientificoBotanico(resultSet.getString("NomeScientificoBotanico"));
        pianta.setCategoria(resultSet.getString("Categoria"));
        pianta.setDescrizioneBreve(resultSet.getString("DescrizioneBreve"));
        pianta.setDescrizioneDettagliata(resultSet.getString("DescrizioneDettagliata"));
        pianta.setEsposizioneLuminosa(resultSet.getString("EsposizioneLuminosa"));
        pianta.setTipoDiTerreno(resultSet.getString("TipoDiTerreno"));
        pianta.setTemperaturaIdeale(resultSet.getInt("TemperaturaIdeale")); // CORREZIONE QUI
        pianta.setFrequenzaIrrigazione(resultSet.getString("FrequenzaIrrigazione"));
        pianta.setPrezzo(resultSet.getFloat("Prezzo"));
        pianta.setDisponibilita(resultSet.getInt("Disponibilita"));
        pianta.setData_inserimento(resultSet.getTimestamp("Data_inserimento"));
        return pianta;
    }
}