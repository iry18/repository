package la_teca_del_giardiniere.DAO;

import com.mysql.cj.jdbc.MysqlDataSource;

import la_teca_del_giardiniere.classes.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtenteDAO {

    private static final Logger LOGGER = Logger.getLogger(UtenteDAO.class.getName());
    private MysqlDataSource dataSource;

    public UtenteDAO() throws SQLException {
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
     * Aggiunge un nuovo utente registrato al database, inclusi i flag di ruolo.
     * La password deve essere già hashata prima di essere passata a questo metodo.
     * @param utente L'oggetto Utente da salvare (con password hashata e ruoli impostati)
     * @throws SQLException In caso di errori SQL
     */
    public void aggiungiUtenteRegistrato(Utente utente) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Inizia la transazione

            // AGGIUNTO 'isVenditore' alla query INSERT
            String sqlUtente = "INSERT INTO utente (nome, cognome, email, password_hash, indirizzo, citta, CAP, telefono, data_registrazione, provincia, isAdmin, isVenditore) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatementUtente = connection.prepareStatement(sqlUtente, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatementUtente.setString(1, utente.getNome());
                preparedStatementUtente.setString(2, utente.getCognome());
                preparedStatementUtente.setString(3, utente.getEmail());
                preparedStatementUtente.setString(4, utente.getPasswordHash());
                preparedStatementUtente.setString(5, utente.getIndirizzo());
                preparedStatementUtente.setString(6, utente.getCitta());
                preparedStatementUtente.setString(7, utente.getCAP() > 0 ? String.valueOf(utente.getCAP()) : null); // Gestione CAP come String per consistenza con DB se necessario, altrimenti int
                preparedStatementUtente.setString(8, utente.getTelefono());
                preparedStatementUtente.setTimestamp(9, utente.getData_registrazione());
                preparedStatementUtente.setString(10, utente.getProvincia());
                preparedStatementUtente.setBoolean(11, utente.isAdmin());
                preparedStatementUtente.setBoolean(12, utente.isVenditore()); // NUOVO PARAMETRO

                preparedStatementUtente.executeUpdate();

                try (ResultSet generatedKeys = preparedStatementUtente.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        utente.setId(generatedKeys.getInt(1));
                        LOGGER.info("Utente " + utente.getEmail() + " registrato con ID: " + utente.getId());
                    } else {
                        throw new SQLException("La creazione dell'utente ha fallito, nessun ID generato.");
                    }
                }
            }
            connection.commit(); // Conferma la transazione

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Esegui il rollback in caso di errore
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Errore durante il rollback della transazione.", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta dell'utente: " + e.getMessage(), e);
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione al database.", e);
                }
            }
        }
    }

    /**
     * Recupera un utente tramite email, includendo la password hashata e i ruoli (isAdmin, isVenditore).
     * @param email L'email dell'utente
     * @return L'oggetto Utente se trovato, altrimenti null.
     * @throws SQLException In caso di errori SQL.
     */
    public Utente getUtenteByEmailWithRuoli(String email) throws SQLException {
        Utente utente = null;
        // AGGIUNTO 'isVenditore' alla query SELECT
        String sql = "SELECT utente_id, nome, cognome, email, password_hash, indirizzo, citta, CAP, telefono, data_registrazione, provincia, isAdmin, isVenditore FROM utente WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    utente = new Utente();
                    utente.setId(resultSet.getInt("utente_id"));
                    utente.setNome(resultSet.getString("nome"));
                    utente.setCognome(resultSet.getString("cognome"));
                    utente.setEmail(resultSet.getString("email"));
                    utente.setPasswordHash(resultSet.getString("password_hash"));
                    utente.setIndirizzo(resultSet.getString("indirizzo"));
                    utente.setCitta(resultSet.getString("citta"));
                    utente.setCAP(resultSet.getInt("CAP"));
                    utente.setTelefono(resultSet.getString("telefono"));
                    utente.setData_registrazione(resultSet.getTimestamp("data_registrazione"));
                    utente.setProvincia(resultSet.getString("provincia"));
                    utente.setAdmin(resultSet.getBoolean("isAdmin"));
                    utente.setVenditore(resultSet.getBoolean("isVenditore")); // IMPOSTA IL RUOLO VENDITORE
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero dell'utente per email: " + email, e);
            throw e;
        }
        return utente;
    }

    /**
     * Recupera tutti gli utenti dal database, inclusi i loro ruoli (basati su isAdmin e isVenditore).
     * @return Una lista di oggetti Utente.
     * @throws SQLException In caso di errori SQL.
     */
    public List<Utente> getAllUtentiConRuoli() throws SQLException {
        List<Utente> listaUtenti = new ArrayList<>();
        // AGGIUNTO 'isVenditore' alla query SELECT
        String sql = "SELECT utente_id, nome, cognome, email, password_hash, indirizzo, citta, CAP, telefono, data_registrazione, provincia, isAdmin, isVenditore FROM utente";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Utente utente = new Utente();
                utente.setId(resultSet.getInt("utente_id"));
                utente.setNome(resultSet.getString("nome"));
                utente.setCognome(resultSet.getString("cognome"));
                utente.setEmail(resultSet.getString("email"));
                utente.setPasswordHash(resultSet.getString("password_hash"));
                utente.setIndirizzo(resultSet.getString("indirizzo"));
                utente.setCitta(resultSet.getString("citta"));
                utente.setCAP(resultSet.getInt("CAP"));
                utente.setTelefono(resultSet.getString("telefono"));
                utente.setData_registrazione(resultSet.getTimestamp("data_registrazione"));
                utente.setProvincia(resultSet.getString("provincia"));
                utente.setAdmin(resultSet.getBoolean("isAdmin"));
                utente.setVenditore(resultSet.getBoolean("isVenditore")); // IMPOSTA IL RUOLO VENDITORE
                listaUtenti.add(utente);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero di tutti gli utenti.", e);
            throw e;
        }
        return listaUtenti;
    }

    /**
     * Controlla se un'email esiste già nel database.
     * @param email L'email da controllare.
     * @return true se l'email esiste, false altrimenti.
     * @throws SQLException In caso di errori SQL.
     */
    public boolean checkEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utente WHERE email = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il controllo dell'email esistente: " + email, e);
            throw e;
        }
        return false;
    }

    /**
     * Recupera un utente tramite ID, inclusi i ruoli.
     * @param id L'ID dell'utente.
     * @return L'oggetto Utente se trovato, altrimenti null.
     * @throws SQLException In caso di errori SQL.
     */
    public Utente getUtenteByIdWithRuoli(int id) throws SQLException {
        Utente utente = null;
        // AGGIUNTO 'isVenditore' alla query SELECT
        String sql = "SELECT utente_id, nome, cognome, email, password_hash, indirizzo, citta, CAP, telefono, data_registrazione, provincia, isAdmin, isVenditore FROM utente WHERE utente_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    utente = new Utente();
                    utente.setId(resultSet.getInt("utente_id"));
                    utente.setNome(resultSet.getString("nome"));
                    utente.setCognome(resultSet.getString("cognome"));
                    utente.setEmail(resultSet.getString("email"));
                    utente.setPasswordHash(resultSet.getString("password_hash"));
                    utente.setIndirizzo(resultSet.getString("indirizzo"));
                    utente.setCitta(resultSet.getString("citta"));
                    utente.setCAP(resultSet.getInt("CAP"));
                    utente.setTelefono(resultSet.getString("telefono"));
                    utente.setData_registrazione(resultSet.getTimestamp("data_registrazione"));
                    utente.setProvincia(resultSet.getString("provincia"));
                    utente.setAdmin(resultSet.getBoolean("isAdmin"));
                    utente.setVenditore(resultSet.getBoolean("isVenditore")); // IMPOSTA IL RUOLO VENDITORE
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero dell'utente per ID: " + id, e);
            throw e;
        }
        return utente;
    }

    /**
     * Aggiorna i dati di un utente esistente, inclusi i flag di ruolo.
     * @param utente L'oggetto Utente con i dati aggiornati.
     * @return true se l'aggiornamento è riuscito, false altrimenti.
     * @throws SQLException In caso di errori SQL.
     */
    public boolean updateUtente(Utente utente) throws SQLException {
        // AGGIUNTO 'isVenditore' alla query UPDATE
        String sql = "UPDATE utente SET nome = ?, cognome = ?, email = ?, password_hash = ?, indirizzo = ?, citta = ?, CAP = ?, telefono = ?, provincia = ?, isAdmin = ?, isVenditore = ? WHERE utente_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, utente.getNome());
            preparedStatement.setString(2, utente.getCognome());
            preparedStatement.setString(3, utente.getEmail());
            preparedStatement.setString(4, utente.getPasswordHash());
            preparedStatement.setString(5, utente.getIndirizzo());
            preparedStatement.setString(6, utente.getCitta());
            preparedStatement.setInt(7, utente.getCAP());
            preparedStatement.setString(8, utente.getTelefono());
            preparedStatement.setString(9, utente.getProvincia());
            preparedStatement.setBoolean(10, utente.isAdmin());
            preparedStatement.setBoolean(11, utente.isVenditore()); // NUOVO PARAMETRO
            preparedStatement.setInt(12, utente.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento dell'utente con ID: " + utente.getId(), e);
            throw e;
        }
    }

    public boolean deleteUtente(int id) throws SQLException {
        String sql = "DELETE FROM utente WHERE utente_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione dell'utente con ID: " + id, e);
            throw e;
        }
    }
}