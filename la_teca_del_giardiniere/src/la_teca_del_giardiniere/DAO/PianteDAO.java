package la_teca_del_giardiniere.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;    // Import per java.sql.Types per setNull
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.cj.jdbc.MysqlDataSource;

import la_teca_del_giardiniere.classes.Piante; // Conferma che questa è la tua classe

public class PianteDAO {

    private static final Logger LOGGER = Logger.getLogger(PianteDAO.class.getName());
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
        LOGGER.info("MysqlDataSource inizializzato.");
    }

    /**
     * Metodo per aggiungere una nuova pianta nel database.
     * @param pianta L'oggetto Piante da aggiungere.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void aggiungiPianta(Piante pianta) throws SQLException {
        // Query SQL aggiornata per includere DescrizioneDettagliata
        String sql = "INSERT INTO piante(" +
                             "NomeComune, Tipo, NomeScientificoBotanico, Categoria, " +
                             "DescrizioneBreve, DescrizioneDettagliata, EsposizioneLuminosa, TipoDiTerreno, TemperaturaIdeale, " +
                             "FrequenzaIrrigazione, Prezzo, Disponibilita, data_inserimento, Immagine) " +
                             "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setString(2, pianta.getTipo());
            preparedStatement.setString(3, pianta.getNomeScientificoBotanico());
            preparedStatement.setString(4, pianta.getCategoria());
            preparedStatement.setString(5, pianta.getDescrizioneBreve());
            preparedStatement.setString(6, pianta.getDescrizioneDettagliata());
            preparedStatement.setString(7, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(8, pianta.getTipoDiTerreno());

            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setString(9, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(9, Types.VARCHAR);
            }

            preparedStatement.setString(10, pianta.getFrequenzaIrrigazione());
            preparedStatement.setBigDecimal(11, pianta.getPrezzo());

            if (pianta.getdisponibilita() != null) {
                preparedStatement.setInt(12, pianta.getdisponibilita());
            } else {
                preparedStatement.setNull(12, Types.INTEGER);
            }

            preparedStatement.setTimestamp(13, pianta.getDataInserimento());
            preparedStatement.setString(14, pianta.getImmagine());

            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Pianta aggiunta con successo: {0}", pianta.getNomeComune());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta della pianta: " + pianta.getNomeComune(), e);
            throw e;
        }
    }

    /**
     * Metodo per recuperare una pianta dal database tramite il suo ID.
     * @param id L'ID della pianta da cercare.
     * @return L'oggetto Piante corrispondente, o null se non trovato.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Piante getPiantaById(int id) throws SQLException {
        String sql = "SELECT * FROM piante WHERE P_CodProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPianta(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero della pianta per ID: " + id, e);
            throw e;
        }
    }

    /**
     * Metodo per recuperare una pianta dal database tramite il suo NomeComune.
     * @param nomeComune Il nome comune della pianta da cercare.
     * @return L'oggetto Piante corrispondente, o null se non trovato.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Piante getPiantaByNomeComune(String nomeComune) throws SQLException {
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
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero della pianta per nome: " + nomeComune, e);
            throw e;
        }
    }

    /**
     * Metodo per recuperare tutte le piante dal database.
     * @return Una lista di oggetti Piante.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Piante> getAllPiante() throws SQLException {
        String sql = "SELECT * FROM piante";
        List<Piante> listaPiante = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                listaPiante.add(mapResultSetToPianta(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero di tutte le piante.", e);
            throw e;
        }
        return listaPiante;
    }

    /**
     * Metodo per aggiornare i dati di una pianta nel database tramite il suo ID.
     * @param pianta L'oggetto Piante con i dati aggiornati.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void aggiornaPianta(Piante pianta) throws SQLException {
        String sql = "UPDATE piante SET " +
                             "NomeComune = ?, Tipo = ?, NomeScientificoBotanico = ?, Categoria = ?, " +
                             "DescrizioneBreve = ?, DescrizioneDettagliata = ?, EsposizioneLuminosa = ?, TipoDiTerreno = ?, TemperaturaIdeale = ?, " +
                             "FrequenzaIrrigazione = ?, Prezzo = ?, Disponibilita = ?, data_inserimento = ?, Immagine = ? " +
                             "WHERE P_CodProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setString(2, pianta.getTipo());
            preparedStatement.setString(3, pianta.getNomeScientificoBotanico());
            preparedStatement.setString(4, pianta.getCategoria());
            preparedStatement.setString(5, pianta.getDescrizioneBreve());
            preparedStatement.setString(6, pianta.getDescrizioneDettagliata());
            preparedStatement.setString(7, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(8, pianta.getTipoDiTerreno());

            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setString(9, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(9, Types.VARCHAR);
            }

            preparedStatement.setString(10, pianta.getFrequenzaIrrigazione());
            preparedStatement.setBigDecimal(11, pianta.getPrezzo());

            if (pianta.getdisponibilita() != null) {
                preparedStatement.setInt(12, pianta.getdisponibilita());
            } else {
                preparedStatement.setNull(12, Types.INTEGER);
            }

            preparedStatement.setTimestamp(13, pianta.getDataInserimento());
            preparedStatement.setString(14, pianta.getImmagine());
            preparedStatement.setInt(15, pianta.getId());

            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Pianta aggiornata con successo: {0}", pianta.getNomeComune());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento della pianta: " + pianta.getNomeComune(), e);
            throw e;
        }
    }

    /**
     * Metodo per eliminare una pianta dal database tramite il suo ID.
     * @param idPianta L'ID della pianta da eliminare.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void eliminaPianta(int idPianta) throws SQLException {
        String sql = "DELETE FROM piante WHERE P_CodProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPianta);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Pianta eliminata con successo, ID: {0}", idPianta);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione della pianta con ID: " + idPianta, e);
            throw e;
        }
    }

    /**
     * Metodo privato per mappare una riga del ResultSet a un oggetto Piante.
     * @param resultSet Il ResultSet da cui leggere i dati.
     * @return Un oggetto Piante popolato con i dati del ResultSet.
     * @throws SQLException Se si verifica un errore SQL durante la lettura del ResultSet.
     */
    private Piante mapResultSetToPianta(ResultSet resultSet) throws SQLException {
        Piante pianta = new Piante();
        pianta.setId(resultSet.getInt("P_CodProdotto"));
        pianta.setNomeComune(resultSet.getString("NomeComune"));
        pianta.setTipo(resultSet.getString("Tipo"));
        pianta.setNomeScientificoBotanico(resultSet.getString("NomeScientificoBotanico"));
        pianta.setCategoria(resultSet.getString("Categoria"));
        pianta.setDescrizioneBreve(resultSet.getString("DescrizioneBreve"));
        pianta.setDescrizioneDettagliata(resultSet.getString("DescrizioneDettagliata"));

        pianta.setEsposizioneLuminosa(resultSet.getString("EsposizioneLuminosa"));
        pianta.setTipoDiTerreno(resultSet.getString("TipoDiTerreno"));

        // Retrieve TemperaturaIdeale as String, handle potential null
        String temperaturaIdeale = resultSet.getString("TemperaturaIdeale");
        if (resultSet.wasNull()) {
            pianta.setTemperaturaIdeale(null);
        } else {
            pianta.setTemperaturaIdeale(temperaturaIdeale);
        }

        pianta.setFrequenzaIrrigazione(resultSet.getString("FrequenzaIrrigazione"));
        pianta.setPrezzo(resultSet.getBigDecimal("Prezzo"));

        int disponibilita = resultSet.getInt("Disponibilita");
        if (!resultSet.wasNull()) {
            pianta.setdisponibilita(disponibilita);
        } else {
            pianta.setdisponibilita(null);
        }

        pianta.setDataInserimento(resultSet.getTimestamp("data_inserimento"));
        pianta.setImmagine(resultSet.getString("Immagine"));

        return pianta;
    }
}