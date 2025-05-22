package src.com.la_teca_del_giardiniere.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import src.com.la_teca_del_giardiniere.classes.registrazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO {

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
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 1. Metodo per aggiungere un nuovo utente e assegnargli un ruolo
    public void aggiungiUtenteConRuolo(registrazione utente, String nomeRuolo) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatementUtente = null;
        PreparedStatement preparedStatementRuolo = null;
        PreparedStatement preparedStatementGetRuoloId = null;
        ResultSet generatedKeys = null;
        ResultSet ruoloResult = null;

        String sqlUtente = "INSERT INTO utente (nome, cognome, email, password, indirizzo, citta, CAP, telefono, data_registrazione, provincia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlRuolo = "INSERT INTO utente_ruolo (utente_id, ruolo_id) VALUES (?, ?)";
        String sqlGetRuoloId = "SELECT ruolo_id FROM ruolo WHERE nome_ruolo = ?";

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Inizia la transazione

            // Inserisci l'utente
            preparedStatementUtente = connection.prepareStatement(sqlUtente, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatementUtente.setString(1, utente.getNome());
            preparedStatementUtente.setString(2, utente.getCognome());
            preparedStatementUtente.setString(3, utente.getEmail());
            preparedStatementUtente.setString(4, utente.getPassword());
            preparedStatementUtente.setString(5, utente.getIndirizzo());
            preparedStatementUtente.setString(6, utente.getCitta());
            preparedStatementUtente.setInt(7, utente.getCAP());
            preparedStatementUtente.setInt(8, utente.getTelefono());
            preparedStatementUtente.setTimestamp(9, utente.getData_registrazione());
            preparedStatementUtente.setString(10, utente.getProvincia());
            preparedStatementUtente.executeUpdate();

            // Ottieni l'ID dell'utente appena inserito
            generatedKeys = preparedStatementUtente.getGeneratedKeys();
            int utenteId = 0;
            if (generatedKeys.next()) {
                utenteId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creazione utente fallita, nessun ID generato.");
            }

            // Ottieni l'ID del ruolo
            preparedStatementGetRuoloId = connection.prepareStatement(sqlGetRuoloId);
            preparedStatementGetRuoloId.setString(1, nomeRuolo);
            ruoloResult = preparedStatementGetRuoloId.executeQuery();
            int ruoloId = 0;
            if (ruoloResult.next()) {
                ruoloId = ruoloResult.getInt("ruolo_id");
            } else {
                throw new SQLException("Ruolo '" + nomeRuolo + "' non trovato nel database.");
            }

            // associazione utente-ruolo nella tabella utente_ruolo
            preparedStatementRuolo = connection.prepareStatement(sqlRuolo);
            preparedStatementRuolo.setInt(1, utenteId);
            preparedStatementRuolo.setInt(2, ruoloId);
            preparedStatementRuolo.executeUpdate();

            connection.commit(); // Commit della transazione
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rollback in caso di errore
            }
            throw e;
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) {}
            if (ruoloResult != null) try { ruoloResult.close(); } catch (SQLException e) {}
            if (preparedStatementUtente != null) try { preparedStatementUtente.close(); } catch (SQLException e) {}
            if (preparedStatementRuolo != null) try { preparedStatementRuolo.close(); } catch (SQLException e) {}
            if (preparedStatementGetRuoloId != null) try { preparedStatementGetRuoloId.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.setAutoCommit(true); connection.close(); } catch (SQLException e) {}
        }
    }

    // Metodo semplificato per la registrazione (assume sempre il ruolo 'compratore')
    public void aggiungiUtenteRegistrato(registrazione utente) throws SQLException {
        aggiungiUtenteConRuolo(utente, "compratore");
    }

    // 2. Metodo per recuperare le informazioni dell'utente in base all'email (per il login) includendo anche i ruoli
    public registrazione getUtenteByEmailWithRuoli(String email) throws SQLException {
        registrazione utente = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "SELECT u.*, r.nome_ruolo FROM utente u " +
                     "LEFT JOIN utente_ruolo ur ON u.utente_id = ur.utente_id " +
                     "LEFT JOIN ruolo r ON ur.ruolo_id = r.ruolo_id " +
                     "WHERE u.email = ?";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            List<String> ruoli = new ArrayList<>();
            while (resultSet.next()) {
                if (utente == null) {
                    utente = new registrazione();
                    utente.setNome(resultSet.getString("nome"));
                    utente.setCognome(resultSet.getString("cognome"));
                    utente.setEmail(resultSet.getString("email"));
                    utente.setPassword(resultSet.getString("password"));
                    utente.setIndirizzo(resultSet.getString("indirizzo"));
                    utente.setCitta(resultSet.getString("citta"));
                    utente.setCAP(resultSet.getInt("CAP"));
                    utente.setTelefono(resultSet.getInt("telefono"));
                    utente.setData_registrazione(resultSet.getTimestamp("data_registrazione"));
                    utente.setProvincia(resultSet.getString("provincia"));
                }
                String ruolo = resultSet.getString("nome_ruolo");
                if (ruolo != null && !ruoli.contains(ruolo)) {
                    ruoli.add(ruolo);
                }
            }
            if (utente != null) {
                utente.setRuoli(ruoli);
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }
        return utente;
    }

    // 3. Metodi per filtrare gli utenti per ruolo
    public List<registrazione> getUsersByRuolo(String nomeRuolo) throws SQLException {
        List<registrazione> utenti = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "SELECT u.* FROM utente u " +
                     "JOIN utente_ruolo ur ON u.utente_id = ur.utente_id " +
                     "JOIN ruolo r ON ur.ruolo_id = r.ruolo_id " +
                     "WHERE r.nome_ruolo = ?";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nomeRuolo);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                registrazione utente = new registrazione();
                utente.setNome(resultSet.getString("nome"));
                utente.setCognome(resultSet.getString("cognome"));
                utente.setEmail(resultSet.getString("email"));
                utente.setIndirizzo(resultSet.getString("indirizzo"));
                utente.setCitta(resultSet.getString("citta"));
                utente.setCAP(resultSet.getInt("CAP"));
                utente.setTelefono(resultSet.getInt("telefono"));
                utente.setData_registrazione(resultSet.getTimestamp("data_registrazione"));
                utente.setProvincia(resultSet.getString("provincia"));
                utenti.add(utente);
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }
        return utenti;
    }

    public List<registrazione> getAllVenditori() throws SQLException {
        return getUsersByRuolo("venditore");
    }

    public List<registrazione> getAllCompratori() throws SQLException {
        return getUsersByRuolo("compratore");
    }

    public List<registrazione> getAllUtentiConRuoli() throws SQLException {
        List<registrazione> utenti = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "SELECT u.*, GROUP_CONCAT(r.nome_ruolo SEPARATOR ', ') AS ruoli " +
                     "FROM utente u " +
                     "LEFT JOIN utente_ruolo ur ON u.utente_id = ur.utente_id " +
                     "LEFT JOIN ruolo r ON ur.ruolo_id = r.ruolo_id " +
                     "GROUP BY u.utente_id";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                registrazione utente = new registrazione();
                utente.setNome(resultSet.getString("nome"));
                utente.setCognome(resultSet.getString("cognome"));
                utente.setEmail(resultSet.getString("email"));
                utente.setIndirizzo(resultSet.getString("indirizzo"));
                utente.setCitta(resultSet.getString("citta"));
                utente.setCAP(resultSet.getInt("CAP"));
                utente.setTelefono(resultSet.getInt("telefono"));
                utente.setData_registrazione(resultSet.getTimestamp("data_registrazione"));
                utente.setProvincia(resultSet.getString("provincia"));
                String ruoliStr = resultSet.getString("ruoli");
                
                if (ruoliStr != null) {
                    utente.setRuoli(List.of(ruoliStr.split(", ")));
                } else {
                    utente.setRuoli(new ArrayList<>());
                }
                utenti.add(utente);
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }
        return utenti;
    }

    public boolean checkEmailExists(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean exists = false;

        String sql = "SELECT COUNT(*) FROM utente WHERE email = ?";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exists = resultSet.getInt(1) > 0;
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }
        return exists;
    }

    // Metodo per recuperare un utente con tutti i suoi ruoli dato l'utente_id (potrebbe essere utile in futuro)
    public registrazione getUtenteByIdWithRuoli(int utenteId) throws SQLException {
        registrazione utente = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "SELECT u.*, r.nome_ruolo FROM utente u " +
                     "LEFT JOIN utente_ruolo ur ON u.utente_id = ur.utente_id " +
                     "LEFT JOIN ruolo r ON ur.ruolo_id = r.ruolo_id " +
                     "WHERE u.utente_id = ?";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, utenteId);
            resultSet = preparedStatement.executeQuery();

            List<String> ruoli = new ArrayList<>();
            while (resultSet.next()) {
                if (utente == null) {
                    utente = new registrazione();
                    utente.setNome(resultSet.getString("nome"));
                    utente.setCognome(resultSet.getString("cognome"));
                    utente.setEmail(resultSet.getString("email"));
                    utente.setPassword(resultSet.getString("password"));
                    utente.setIndirizzo(resultSet.getString("indirizzo"));
                    utente.setCitta(resultSet.getString("citta"));
                    utente.setCAP(resultSet.getInt("CAP"));
                    utente.setTelefono(resultSet.getInt("telefono"));
                    utente.setData_registrazione(resultSet.getTimestamp("data_registrazione"));
                    utente.setProvincia(resultSet.getString("provincia"));
                    utente.setUtente_id(utenteId); // Imposta l'ID dell'utente
                }
                String ruolo = resultSet.getString("nome_ruolo");
                if (ruolo != null && !ruoli.contains(ruolo)) {
                    ruoli.add(ruolo);
                }
            }
            if (utente != null) {
                utente.setRuoli(ruoli);
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (preparedStatement != null) try { preparedStatement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }
        return utente;
    }
}