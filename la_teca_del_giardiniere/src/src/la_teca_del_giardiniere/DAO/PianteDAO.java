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
        dataSource.setCharacterEncoding("UTF-8"); 
        dataSource.setUseSSL(false);            
        dataSource.setAllowPublicKeyRetrieval(true);
        LOGGER.info("PianteDAO inizializzato con successo.");
    }

    /**
     * Metodo per aggiungere una nuova pianta nel database.
     * @param pianta L'oggetto Piante da aggiungere.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void aggiungiPianta(Piante pianta) throws SQLException {
        String sql = "INSERT INTO piante(" +
                     "nome_comune, tipo, nome_scientifico, categoria, " +
                     "descrizione, esposizione_luminosa, tipo_di_terreno, temperatura_ideale, " +
                     "frequenza_irrigazione, prezzo, quantita_disponibile, data_inserimento, url_immagine) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setString(2, pianta.getTipo());
            preparedStatement.setString(3, pianta.getNomeBotanico());
            preparedStatement.setString(4, pianta.getCategoria());
            preparedStatement.setString(5, pianta.getDescrizione());
            preparedStatement.setString(6, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(7, pianta.getTipoDiTerreno());

            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setInt(8, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }

            preparedStatement.setString(9, pianta.getFrequenzaIrrigazione());
            preparedStatement.setBigDecimal(10, pianta.getPrezzo()); // Usa BigDecimal per il prezzo

            if (pianta.getQuantitaDisponibile() != null) {
                preparedStatement.setInt(11, pianta.getQuantitaDisponibile());
            } else {
                preparedStatement.setNull(11, Types.INTEGER);
            }

            preparedStatement.setTimestamp(12, pianta.getDataInserimento());
            preparedStatement.setString(13, pianta.getUrlImmagine());

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
     * Metodo per recuperare una pianta dal database tramite il suo NomeComune.
     * @param nomeComune Il nome comune della pianta da cercare.
     * @return L'oggetto Piante corrispondente, o null se non trovato.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Piante getPiantaByNomeComune(String nomeComune) throws SQLException {
        String sql = "SELECT * FROM piante WHERE nome_comune = ?"; // Corretto nome colonna
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
                     "nome_comune = ?, tipo = ?, nome_scientifico = ?, categoria = ?, " +
                     "descrizione = ?, esposizione_luminosa = ?, tipo_di_terreno = ?, temperatura_ideale = ?, " +
                     "frequenza_irrigazione = ?, prezzo = ?, quantita_disponibile = ?, data_inserimento = ?, url_immagine = ? " +
                     "WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setString(2, pianta.getTipo());
            preparedStatement.setString(3, pianta.getNomeBotanico());
            preparedStatement.setString(4, pianta.getCategoria());
            preparedStatement.setString(5, pianta.getDescrizione());
            preparedStatement.setString(6, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(7, pianta.getTipoDiTerreno());

            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setInt(8, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }

            preparedStatement.setString(9, pianta.getFrequenzaIrrigazione());
            preparedStatement.setBigDecimal(10, pianta.getPrezzo());

            if (pianta.getQuantitaDisponibile() != null) {
                preparedStatement.setInt(11, pianta.getQuantitaDisponibile());
            } else {
                preparedStatement.setNull(11, Types.INTEGER);
            }

            preparedStatement.setTimestamp(12, pianta.getDataInserimento());
            preparedStatement.setString(13, pianta.getUrlImmagine());
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
        // TODO: Gestire il vincolo di integrità referenziale (e.g., eliminare da tabelle correlate o impedire eliminazione)
    }

    /**
     * Metodo privato per mappare una riga del ResultSet a un oggetto Piante.
     * @param resultSet Il ResultSet da cui leggere i dati.
     * @return Un oggetto Piante popolato con i dati del ResultSet.
     * @throws SQLException Se si verifica un errore SQL durante la lettura del ResultSet.
     */
    private Piante mapResultSetToPianta(ResultSet resultSet) throws SQLException {
        Piante pianta = new Piante();
        pianta.setId(resultSet.getInt("id"));
        pianta.setNomeComune(resultSet.getString("nome_comune")); // Colonna 'nome_comune'
        pianta.setTipo(resultSet.getString("tipo")); // Colonna 'tipo'
        pianta.setNomeBotanico(resultSet.getString("nome_scientifico")); // Colonna 'nome_scientifico'
        pianta.setCategoria(resultSet.getString("categoria"));
        pianta.setDescrizione(resultSet.getString("descrizione"));
        pianta.setEsposizioneLuminosa(resultSet.getString("esposizione_luminosa")); // Colonna 'esposizione_luminosa'
        pianta.setTipoDiTerreno(resultSet.getString("tipo_di_terreno")); // Colonna 'tipo_di_terreno'

        int temperatura = resultSet.getInt("temperatura_ideale"); // Colonna 'temperatura_ideale'
        if (!resultSet.wasNull()) {
            pianta.setTemperaturaIdeale(temperatura);
        } else {
            pianta.setTemperaturaIdeale(null);
        }

        pianta.setFrequenzaIrrigazione(resultSet.getString("frequenza_irrigazione")); // Colonna 'frequenza_irrigazione'
        pianta.setPrezzo(resultSet.getBigDecimal("prezzo"));

        int disponibilita = resultSet.getInt("quantita_disponibile"); // Colonna 'quantita_disponibile'
        if (!resultSet.wasNull()) {
            pianta.setQuantitaDisponibile(disponibilita);
        } else {
            pianta.setQuantitaDisponibile(null);
        }

        pianta.setDataInserimento(resultSet.getTimestamp("data_inserimento")); // Colonna 'data_inserimento'
        pianta.setUrlImmagine(resultSet.getString("url_immagine")); // Colonna 'url_immagine'

        return pianta;
    }
}