package la_teca_del_giardiniere.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;     // Import per java.sql.Types per setNull
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger; // Per un logging più robusto

import com.mysql.cj.jdbc.MysqlDataSource;

import la_teca_del_giardiniere.classes.Piante; // CAMBIATO: da 'Piante' a 'Pianta'

public class PianteDAO {

    private static final Logger LOGGER = Logger.getLogger(PianteDAO.class.getName()); // Logger
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
        LOGGER.info("PianteDAO inizializzato con successo.");
    }

    /**
     * Metodo per aggiungere una nuova pianta nel database.
     *
     * @param pianta L'oggetto Pianta da aggiungere.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void aggiungiPianta(Piante pianta) throws SQLException { // CAMBIATO: da 'piante' a 'Pianta'
        String sql = "INSERT INTO piante(" +
                     "NomeComune, Tipo, NomeScientificoBotanico, Categoria, " +
                     "Descrizione, EsposizioneLuminosa, TipoDiTerreno, TemperaturaIdeale, " +
                     "FrequenzaIrrigazione, Prezzo, QuantitaDisponibile, Data_inserimento, Immagine) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setString(2, pianta.getTipo()); // CAMBIATO: da setBoolean a setString
            preparedStatement.setString(3, pianta.getNomeBotanico()); // CAMBIATO: Nome Scientifico Botanico
            preparedStatement.setString(4, pianta.getCategoria());
            preparedStatement.setString(5, pianta.getDescrizione()); // CAMBIATO: unito breve e dettagliata
            preparedStatement.setString(6, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(7, pianta.getTipoDiTerreno());

            // Gestione dei valori Integer che possono essere null
            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setInt(8, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(8, Types.INTEGER); // Utilizzo di java.sql.Types
            }

            preparedStatement.setString(9, pianta.getFrequenzaIrrigazione());
            preparedStatement.setBigDecimal(10, pianta.getPrezzo()); // CAMBIATO: da setFloat a setBigDecimal

            // Gestione dei valori Integer che possono essere null
            if (pianta.getQuantitaDisponibile() != null) {
                preparedStatement.setInt(11, pianta.getQuantitaDisponibile());
            } else {
                preparedStatement.setNull(11, Types.INTEGER); // Utilizzo di java.sql.Types
            }

            preparedStatement.setTimestamp(12, pianta.getDataInserimento()); // CAMBIATO: data_inserimento a dataInserimento
            preparedStatement.setString(13, pianta.getUrlImmagine()); // CAMBIATO: Immagine a UrlImmagine

            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Pianta aggiunta con successo: {0}", pianta.getNomeComune());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta della pianta: " + pianta.getNomeComune(), e);
            throw e; // Rilancia l'eccezione per essere gestita a livello superiore
        }
    }

    /**
     * Metodo per recuperare una pianta dal database tramite il suo NomeComune.
     *
     * @param nomeComune Il nome comune della pianta da cercare.
     * @return L'oggetto Pianta corrispondente, o null se non trovato.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Piante getPiantaByNomeComune(String nomeComune) throws SQLException { // CAMBIATO: da 'piante' a 'Pianta'
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
     *
     * @return Una lista di oggetti Pianta.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Piante> getAllPiante() throws SQLException { // CAMBIATO: da 'piante' a 'Pianta'
        String sql = "SELECT * FROM piante";
        List<Piante> listaPiante = new ArrayList<>(); // CAMBIATO: da 'piante' a 'Pianta'
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
     * Metodo per recuperare una pianta dal database tramite il suo ID.
     *
     * @param id L'ID della pianta da cercare.
     * @return L'oggetto Pianta corrispondente, o null se non trovato.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Piante getPiantaById(int id) throws SQLException { // CAMBIATO: da 'piante' a 'Pianta'
        String sql = "SELECT * FROM piante WHERE id = ?";
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
     * Metodo per aggiornare i dati di una pianta nel database tramite il suo ID.
     *
     * @param pianta L'oggetto Piante con i dati aggiornati.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void aggiornaPianta(Piante pianta) throws SQLException { // CAMBIATO: da 'piante' a 'Pianta'
        String sql = "UPDATE piante SET " +
                     "NomeComune = ?, Tipo = ?, NomeScientificoBotanico = ?, Categoria = ?, " +
                     "Descrizione = ?, EsposizioneLuminosa = ?, TipoDiTerreno = ?, TemperaturaIdeale = ?, " +
                     "FrequenzaIrrigazione = ?, Prezzo = ?, QuantitaDisponibile = ?, Data_inserimento = ?, Immagine = ? " +
                     "WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setString(2, pianta.getTipo()); // CAMBIATO: da setBoolean a setString
            preparedStatement.setString(3, pianta.getNomeBotanico()); // CAMBIATO: NomeScientificoBotanico
            preparedStatement.setString(4, pianta.getCategoria());
            preparedStatement.setString(5, pianta.getDescrizione()); // CAMBIATO: unito breve e dettagliata
            preparedStatement.setString(6, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(7, pianta.getTipoDiTerreno());

            // Gestione dei valori Integer che possono essere null
            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setInt(8, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }

            preparedStatement.setString(9, pianta.getFrequenzaIrrigazione());
            preparedStatement.setBigDecimal(10, pianta.getPrezzo()); // CAMBIATO: da setFloat a setBigDecimal

            // Gestione dei valori Integer che possono essere null
            if (pianta.getQuantitaDisponibile() != null) {
                preparedStatement.setInt(11, pianta.getQuantitaDisponibile());
            } else {
                preparedStatement.setNull(11, Types.INTEGER);
            }

            preparedStatement.setTimestamp(12, pianta.getDataInserimento()); // CAMBIATO: data_inserimento a dataInserimento
            preparedStatement.setString(13, pianta.getUrlImmagine()); // CAMBIATO: Immagine a UrlImmagine
            preparedStatement.setInt(14, pianta.getId()); // ID per la clausola WHERE

            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Pianta aggiornata con successo: {0}", pianta.getNomeComune());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento della pianta: " + pianta.getNomeComune(), e);
            throw e;
        }
    }

    /**
     * Metodo per eliminare una pianta dal database tramite il suo ID.
     *
     * @param idPianta L'ID della pianta da eliminare.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void eliminaPianta(int idPianta) throws SQLException {
        String sql = "DELETE FROM piante WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPianta);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Pianta eliminata con successo, ID: {0}", idPianta);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione della pianta con ID: " + idPianta, e);
            throw e;
        }
        // TODO: Gestire il vincolo di integrità referenziale.
        // Potrebbe essere necessario controllare se ci sono ordini che contengono questa pianta
        // prima di eliminarla o implementare logiche di gestione alternative.
    }

    /**
     * Metodo privato per mappare una riga del ResultSet a un oggetto Pianta.
     *
     * @param resultSet Il ResultSet da cui leggere i dati.
     * @return Un oggetto Pianta popolato con i dati del ResultSet.
     * @throws SQLException Se si verifica un errore SQL durante la lettura del ResultSet.
     */
    private Piante mapResultSetToPianta(ResultSet resultSet) throws SQLException { // CAMBIATO: da 'piante' a 'Pianta'
        Piante pianta = new Piante(); // CAMBIATO: da 'piante' a 'Pianta'
        pianta.setId(resultSet.getInt("id"));
        pianta.setNomeComune(resultSet.getString("NomeComune"));
        pianta.setTipo(resultSet.getString("Tipo")); // CAMBIATO: da getBoolean a getString
        pianta.setNomeBotanico(resultSet.getString("NomeScientificoBotanico")); // CAMBIATO: Nome Scientifico Botanico
        pianta.setCategoria(resultSet.getString("Categoria"));
        pianta.setDescrizione(resultSet.getString("Descrizione")); // CAMBIATO: unito breve e dettagliata
        pianta.setEsposizioneLuminosa(resultSet.getString("EsposizioneLuminosa"));
        pianta.setTipoDiTerreno(resultSet.getString("TipoDiTerreno"));

        // Recupera TemperaturaIdeale come Integer, gestendo i valori NULL
        int temperatura = resultSet.getInt("TemperaturaIdeale");
        if (!resultSet.wasNull()) {
            pianta.setTemperaturaIdeale(temperatura);
        } else {
            pianta.setTemperaturaIdeale(null);
        }

        pianta.setFrequenzaIrrigazione(resultSet.getString("FrequenzaIrrigazione"));
        pianta.setPrezzo(resultSet.getBigDecimal("Prezzo")); // CAMBIATO: da getFloat a getBigDecimal

        // Recupera QuantitaDisponibile come Integer, gestendo i valori NULL
        int disponibilita = resultSet.getInt("QuantitaDisponibile"); // CAMBIATO: Disponibilita a QuantitaDisponibile
        if (!resultSet.wasNull()) {
            pianta.setQuantitaDisponibile(disponibilita);
        } else {
            pianta.setQuantitaDisponibile(null);
        }

        pianta.setDataInserimento(resultSet.getTimestamp("Data_inserimento")); // CAMBIATO: data_inserimento a dataInserimento
        pianta.setUrlImmagine(resultSet.getString("Immagine")); // CAMBIATO: Immagine a UrlImmagine

        return pianta;
    }
}