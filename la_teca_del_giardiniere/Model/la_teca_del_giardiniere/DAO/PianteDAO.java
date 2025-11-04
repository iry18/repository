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

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Metodo per aggiungere una nuova pianta nel database.
     *
     * @param pianta L'oggetto Piante da aggiungere.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void aggiungiPianta(Piante pianta) throws SQLException {
        String sql = "INSERT INTO piante(" +
                "NomeComune, Tipo, NomeScientificoBotanico, DescrizioneBreve, DescrizioneDettagliata, " +
                "EsposizioneLuminosa, TipoDiTerreno, TemperaturaIdeale, FrequenzaIrrigazione, Prezzo, " +
                "Disponibilita, data_inserimento, Immagine) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setString(2, pianta.getTipo());
            preparedStatement.setString(3, pianta.getNomeScientificoBotanico());
            preparedStatement.setString(4, pianta.getDescrizioneBreve());
            preparedStatement.setString(5, pianta.getDescrizioneDettagliata());
            preparedStatement.setString(6, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(7, pianta.getTipoDiTerreno());

            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setString(8, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(8, Types.VARCHAR);
            }

            preparedStatement.setString(9, pianta.getFrequenzaIrrigazione());
            preparedStatement.setBigDecimal(10, pianta.getPrezzo());

            if (pianta.getDisponibilita() != null) {
                preparedStatement.setInt(11, pianta.getDisponibilita());
            } else {
                preparedStatement.setNull(11, Types.INTEGER);
            }

            preparedStatement.setTimestamp(12, pianta.getDataInserimento());
            preparedStatement.setString(13, pianta.getImmagine());

            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Pianta aggiunta con successo: {0}", pianta.getNomeComune());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta della pianta: " + pianta.getNomeComune(), e);
            throw e;
        }
    }

    /**
     * Metodo per recuperare una pianta dal database tramite il suo ID.
     *
     * @param id L'ID della pianta da cercare.
     * @return L'oggetto Piante corrispondente, o null se non trovato.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Piante getPiantaById(int id) throws SQLException {
        String sql = "SELECT * FROM piante WHERE Id = ?";
        try (Connection connection = getConnection();
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
     * Metodo per recuperare una lista di piante dal database tramite il loro NomeComune.
     *
     * @param nome Il nome comune della pianta da cercare.
     * @return Una lista di oggetti Piante.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Piante> getPianteByNomeComune(String nome) throws SQLException {
        List<Piante> piante = new ArrayList<>();
        String sql = "SELECT * FROM piante WHERE LOWER(NomeComune) LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nome.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    piante.add(mapResultSetToPianta(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL nella ricerca per nome comune: " + nome, e);
            throw e;
        }
        return piante;
    }

    /**
     * Metodo per recuperare tutte le piante dal database.
     *
     * @return Una lista di oggetti Piante.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Piante> getAllPiante() throws SQLException {
        String sql = "SELECT * FROM piante";
        List<Piante> listaPiante = new ArrayList<>();
        try (Connection connection = getConnection();
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
     *
     * @param pianta L'oggetto Piante con i dati aggiornati.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void aggiornaPianta(Piante pianta) throws SQLException {
        String sql = "UPDATE piante SET " +
                "NomeComune = ?, Tipo = ?, NomeScientificoBotanico = ?, DescrizioneBreve = ?, DescrizioneDettagliata = ?, " +
                "EsposizioneLuminosa = ?, TipoDiTerreno = ?, TemperaturaIdeale = ?, FrequenzaIrrigazione = ?, Prezzo = ?, " +
                "Disponibilita = ?, data_inserimento = ?, Immagine = ? " +
                "WHERE Id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, pianta.getNomeComune());
            preparedStatement.setString(2, pianta.getTipo());
            preparedStatement.setString(3, pianta.getNomeScientificoBotanico());
            preparedStatement.setString(4, pianta.getDescrizioneBreve());
            preparedStatement.setString(5, pianta.getDescrizioneDettagliata());
            preparedStatement.setString(6, pianta.getEsposizioneLuminosa());
            preparedStatement.setString(7, pianta.getTipoDiTerreno());
            
            if (pianta.getTemperaturaIdeale() != null) {
                preparedStatement.setString(8, pianta.getTemperaturaIdeale());
            } else {
                preparedStatement.setNull(8, Types.VARCHAR);
            }

            preparedStatement.setString(9, pianta.getFrequenzaIrrigazione());
            preparedStatement.setBigDecimal(10, pianta.getPrezzo());
            
            if (pianta.getDisponibilita() != null) {
                preparedStatement.setInt(11, pianta.getDisponibilita());
            } else {
                preparedStatement.setNull(11, Types.INTEGER);
            }

            preparedStatement.setTimestamp(12, pianta.getDataInserimento());
            preparedStatement.setString(13, pianta.getImmagine());
            preparedStatement.setInt(14, pianta.getId());

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
     * @param id L'ID della pianta da eliminare.
     * @return true se la pianta è stata eliminata, false altrimenti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public boolean deletePianta(int id) throws SQLException {
        String sql = "DELETE FROM piante WHERE Id = ?";
        boolean deleted = false;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                deleted = true;
                LOGGER.log(Level.INFO, "Pianta eliminata con successo, ID: {0}", id);
            } else {
                LOGGER.log(Level.WARNING, "Nessuna pianta trovata con ID: {0} per l'eliminazione.", id);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione della pianta con ID: " + id, e);
            throw e;
        }
        return deleted;
    }

    /**
     * Metodo privato per mappare una riga del ResultSet a un oggetto Piante.
     *
     * @param resultSet Il ResultSet da cui leggere i dati.
     * @return Un oggetto Piante popolato con i dati del ResultSet.
     * @throws SQLException Se si verifica un errore SQL.
     */
    private Piante mapResultSetToPianta(ResultSet resultSet) throws SQLException {
        Piante pianta = new Piante();
        pianta.setId(resultSet.getInt("Id"));
        pianta.setNomeComune(resultSet.getString("NomeComune"));
        pianta.setTipo(resultSet.getString("Tipo"));
        pianta.setNomeScientificoBotanico(resultSet.getString("NomeScientificoBotanico"));
        pianta.setDescrizioneBreve(resultSet.getString("DescrizioneBreve"));
        pianta.setDescrizioneDettagliata(resultSet.getString("DescrizioneDettagliata"));
        pianta.setEsposizioneLuminosa(resultSet.getString("EsposizioneLuminosa"));
        pianta.setTipoDiTerreno(resultSet.getString("TipoDiTerreno"));
        pianta.setTemperaturaIdeale(resultSet.getString("TemperaturaIdeale"));
        pianta.setFrequenzaIrrigazione(resultSet.getString("FrequenzaIrrigazione"));
        pianta.setPrezzo(resultSet.getBigDecimal("Prezzo"));
        pianta.setDisponibilita(resultSet.getInt("Disponibilita"));
        if (resultSet.wasNull()) {
            pianta.setDisponibilita(null);
        }
        pianta.setDataInserimento(resultSet.getTimestamp("data_inserimento"));
        pianta.setImmagine(resultSet.getString("Immagine"));
        return pianta;
    }
    
    /**
     * Metodo per recuperare un numero limitato di piante in evidenza (ad es. per la homepage).
     *
     * @param limit Il numero massimo di piante da recuperare.
     * @return Una lista di oggetti Piante.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Piante> getPianteInEvidenza(int limit) throws SQLException {
        String sql = "SELECT * FROM piante ORDER BY data_inserimento DESC LIMIT ?";
        List<Piante> piante = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    piante.add(mapResultSetToPianta(rs));
                }
            }
        }
        return piante;
    }
    
    /**
     * Metodo per recuperare le piante per tipologia (interno/esterno).
     *
     * @param tipo Il tipo di pianta da cercare.
     * @return Una lista di oggetti Piante.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Piante> filterPianteByNomeComune(List<Piante> lista, String nome) {
        if (lista == null || nome == null || nome.trim().isEmpty()) {
            return lista; 
        }
        
        List<Piante> filteredPiante = new ArrayList<>();
        String lowerCaseNome = nome.toLowerCase().trim();
        
        for (Piante pianta : lista) {
            if (pianta.getNomeComune() != null && pianta.getNomeComune().toLowerCase().contains(lowerCaseNome)) {
                filteredPiante.add(pianta);
            }
        }
        
        return filteredPiante;
    }
    
    public List<Piante> getPianteByTipo(String tipo) throws SQLException {
        List<Piante> piante = new ArrayList<>();
        String sql = "SELECT * FROM piante WHERE LOWER(Tipo) = ?"; 
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tipo.toLowerCase().trim()); 
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    piante.add(mapResultSetToPianta(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL nella ricerca per tipo: " + tipo, e);
            throw e;
        }
        return piante;
    }
    
    public List<Piante> searchByQuery(String searchQuery) throws SQLException {
        List<Piante> risultatiPiante = new ArrayList<>();

        // La query usa LIKE per la ricerca parziale e LOWER() per la case-insensitivity.
        String sql = "SELECT * FROM piante WHERE "
                   + "LOWER(NomeComune) LIKE LOWER(?) OR "
                   + "LOWER(NomeScientificoBotanico) LIKE LOWER(?) OR "
                   + "LOWER(DescrizioneBreve) LIKE LOWER(?)";

        // Prepara la query di ricerca aggiungendo i simboli jolly '%'
        String likeQuery = "%" + searchQuery.trim() + "%";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Imposta il parametro di ricerca per i tre campi
            preparedStatement.setString(1, likeQuery);
            preparedStatement.setString(2, likeQuery);
            preparedStatement.setString(3, likeQuery);

            LOGGER.log(Level.INFO, "Esecuzione query di ricerca piante per: " + likeQuery);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    // Riutilizza il metodo helper esistente
                    risultatiPiante.add(mapResultSetToPianta(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante la ricerca delle piante per query: " + searchQuery, e);
            // Rilancia l'eccezione per essere gestita dalla Servlet
            throw e;
        }

        return risultatiPiante;
    }
}