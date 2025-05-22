package src.com.la_teca_del_giardiniere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.MysqlDataSource;

import src.com.la_teca_del_giardiniere.classes.piante;

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
        String sql = "INSERT INTO piante(NomeComune, Tipo, NomeScientificoBotanico, Categoria, DescrizioneBreve, DescrizioneDettagliata, EsposizioneLuminosa, TipoDiTerreno, TemperaturaIdeale, FrequenzaIrrigazione, Prezzo, Disponibilita, Data_inserimento) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setInt(9, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(9, java.sql.Types.INTEGER);
            }
            preparedStatement.setString(10, pianta.getFrequenzaIrrigazione());
            preparedStatement.setFloat(11, pianta.getPrezzo());
            if (pianta.getDisponibilita() != null) {
                preparedStatement.setInt(12, pianta.getDisponibilita());
            } else {
                preparedStatement.setNull(12, java.sql.Types.INTEGER);
            }
            preparedStatement.setTimestamp(13, pianta.getData_inserimento());

            preparedStatement.executeUpdate();
        }
    }

    // Metodo per recuperare una pianta dal database tramite il suo NomeComune
    public piante getPiantaByNomeComune(String nomeComune) throws SQLException {
        String sql = "SELECT * FROM piante WHERE NomeComune = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nomeComune);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPianta(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    // Metodo per recuperare tutte le piante dal database
    public List<piante> getAllPiante() throws SQLException {
        String sql = "SELECT * FROM piante";
        List<piante> listaPiante = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                listaPiante.add(mapResultSetToPianta(resultSet));
            }
        }
        return listaPiante;
    }

    // Metodo per recuperare una pianta dal database tramite il suo ID
    public piante getPiantaById(int id) throws SQLException {
        String sql = "SELECT * FROM piante WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPianta(resultSet);
                }
                return null; // Nessuna pianta trovata con questo ID
            }
        }
    }

    // Metodo per aggiornare i dati di una pianta nel database tramite il suo ID
    public void aggiornaPianta(piante pianta) throws SQLException {
        String sql = "UPDATE piante SET NomeComune = ?, Tipo = ?, NomeScientificoBotanico = ?, Categoria = ?, DescrizioneBreve = ?, DescrizioneDettagliata = ?, EsposizioneLuminosa = ?, TipoDiTerreno = ?, TemperaturaIdeale = ?, FrequenzaIrrigazione = ?, Prezzo = ?, Disponibilita = ?, Data_inserimento = ? WHERE id = ?";
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
            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setInt(9, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(9, java.sql.Types.INTEGER);
            }
            preparedStatement.setString(10, pianta.getFrequenzaIrrigazione());
            preparedStatement.setFloat(11, pianta.getPrezzo());
            if (pianta.getDisponibilita() != null) {
                preparedStatement.setInt(12, pianta.getDisponibilita());
            } else {
                preparedStatement.setNull(12, java.sql.Types.INTEGER);
            }
            preparedStatement.setTimestamp(13, pianta.getData_inserimento());
            preparedStatement.setInt(14, pianta.getId()); 
            preparedStatement.executeUpdate();
        }
    }

    // Metodo per eliminare una pianta dal database tramite il suo ID
    public void eliminaPianta(int idPianta) throws SQLException {
        String sql = "DELETE FROM piante WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPianta);
            preparedStatement.executeUpdate();
        }
        // TODO: Gestire il vincolo di integrità referenziale.
        // Potrebbe essere necessario controllare se ci sono ordini che contengono questa pianta
        // prima di eliminarla o implementare logiche di gestione alternative.
    }

    // Metodo privato per mappare una riga del ResultSet a un oggetto Piante
    private piante mapResultSetToPianta(ResultSet resultSet) throws SQLException {
        piante pianta = new piante();
        pianta.setId(resultSet.getInt("id")); // ASSICURATI CHE LA TUA TABELLA ABBIA UNA COLONNA 'id'
        pianta.setNomeComune(resultSet.getString("NomeComune"));
        pianta.setTipo(resultSet.getBoolean("Tipo"));
        pianta.setNomeScientificoBotanico(resultSet.getString("NomeScientificoBotanico"));
        pianta.setCategoria(resultSet.getString("Categoria"));
        pianta.setDescrizioneBreve(resultSet.getString("DescrizioneBreve"));
        pianta.setDescrizioneDettagliata(resultSet.getString("DescrizioneDettagliata"));
        pianta.setEsposizioneLuminosa(resultSet.getString("EsposizioneLuminosa"));
        pianta.setTipoDiTerreno(resultSet.getString("TipoDiTerreno"));
        Integer temperatura = resultSet.getInt("TemperaturaIdeale");
        if (!resultSet.wasNull()) { // Verifica se il valore era NULL nel database
            pianta.setTemperaturaIdeale(temperatura);
        }
        pianta.setFrequenzaIrrigazione(resultSet.getString("FrequenzaIrrigazione"));
        pianta.setPrezzo(resultSet.getFloat("Prezzo"));
        Integer disponibilita = resultSet.getInt("Disponibilita");
        if (!resultSet.wasNull()) { // Verifica se il valore era NULL nel database
            pianta.setDisponibilita(disponibilita);
        }
        pianta.setData_inserimento(resultSet.getTimestamp("Data_inserimento"));
        return pianta;
    }
}