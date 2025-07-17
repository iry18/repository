package la_teca_del_giardiniere.DAO;

import com.mysql.cj.jdbc.MysqlDataSource;
import la_teca_del_giardiniere.classes.Carrello;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarrelloDAO {
    private static final Logger LOGGER = Logger.getLogger(CarrelloDAO.class.getName());

    private MysqlDataSource dataSource;

    public CarrelloDAO(MysqlDataSource dataSource) {
        this.dataSource = dataSource;
        LOGGER.info("CarrelloDAO inizializzato con MysqlDataSource.");
    }

    public CarrelloDAO() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("root"); // ATTENZIONE: Password hardcoded, considerare JNDI o file di configurazione
        dataSource.setDatabaseName("la_teca_del_giardiniere");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
        LOGGER.info("CarrelloDAO inizializzato con DataSource predefinito.");
    }

    private Connection getConnection() throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            LOGGER.log(Level.FINE, "Connessione al database ottenuta dal DataSource.");
            return connection;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante il recupero della connessione dal DataSource.", e);
            throw e;
        }
    }

    /**
     * Aggiunge un articolo al carrello o aggiorna la quantità se l'articolo esiste già per lo stesso utente e tipo.
     * @param carrello L'oggetto Carrello da aggiungere/aggiornare.
     * @return true se l'operazione ha avuto successo, false altrimenti.
     */
    public boolean aggiungiOAggiornaArticoloCarrello(Carrello carrello) throws SQLException {
        String sqlCheck = "SELECT carrello_id, quantita FROM carrello WHERE utente_id = ? AND prodotto_id = ? AND tipo_prodotto = ?";
        String sqlInsert = "INSERT INTO carrello (utente_id, prodotto_id, tipo_prodotto, quantita, data_aggiunta) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE carrello SET quantita = ?, data_aggiunta = ? WHERE carrello_id = ?";

        boolean success = false;
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Inizia la transazione

            try (PreparedStatement psCheck = connection.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, carrello.getUtenteId());
                psCheck.setInt(2, carrello.getProdottoId());
                psCheck.setString(3, carrello.getTipoProdotto());

                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        // Articolo già presente, aggiorna la quantità
                        int existingCarrelloId = rs.getInt("carrello_id");
                        int oldQuantita = rs.getInt("quantita");
                        try (PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate)) {
                            psUpdate.setInt(1, oldQuantita + carrello.getQuantita());
                            psUpdate.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                            psUpdate.setInt(3, existingCarrelloId);
                            int rowsAffected = psUpdate.executeUpdate();
                            if (rowsAffected > 0) {
                                LOGGER.log(Level.INFO, "Aggiornata quantità articolo nel carrello ID: {0}", existingCarrelloId);
                                success = true;
                            }
                        }
                    } else {
                        // Articolo non presente, inserisci
                        try (PreparedStatement psInsert = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                            psInsert.setInt(1, carrello.getUtenteId());
                            psInsert.setInt(2, carrello.getProdottoId());
                            psInsert.setString(3, carrello.getTipoProdotto());
                            psInsert.setInt(4, carrello.getQuantita());
                            psInsert.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                            int rowsAffected = psInsert.executeUpdate();

                            if (rowsAffected > 0) {
                                try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                                    if (generatedKeys.next()) {
                                        carrello.setCarrelloId(generatedKeys.getInt(1));
                                        LOGGER.log(Level.INFO, "Articolo aggiunto al carrello con ID: {0}", carrello.getCarrelloId());
                                        success = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Errore durante il rollback della transazione.", ex);
                }
            }
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiunta/aggiornamento articolo carrello: {0}", e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione nel finally di aggiungiOAggiornaArticoloCarrello: {0}", e.getMessage());
                }
            }
        }
        return success;
    }

    /**
     * Recupera tutti gli articoli nel carrello di un utente, includendo nome e prezzo unitario.
     * @param utenteId L'ID dell'utente.
     * @return Una lista di oggetti Carrello.
     */
    public List<Carrello> getArticoliCarrelloByUtenteId(int utenteId) throws SQLException {
        List<Carrello> articoli = new ArrayList<>();
        String sql = "SELECT c.carrello_id, c.utente_id, c.prodotto_id, c.tipo_prodotto, c.quantita, c.data_aggiunta, " +
                     "       CASE c.tipo_prodotto WHEN 'PIANTA' THEN p.nomeComune ELSE a.nome END AS nome_articolo, " +
                     "       CASE c.tipo_prodotto WHEN 'PIANTA' THEN p.prezzo ELSE a.prezzo END AS prezzo_unitario_corrente " +
                     "FROM carrello c " +
                     "LEFT JOIN piante p ON c.prodotto_id = p.codprodotto AND c.tipo_prodotto = 'PIANTA' " +
                     "LEFT JOIN accessori a ON c.prodotto_id = a.accessorio_id AND c.tipo_prodotto = 'ACCESSORIO' " +
                     "WHERE c.utente_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, utenteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Carrello articolo = new Carrello();
                    articolo.setCarrelloId(rs.getInt("carrello_id"));
                    articolo.setUtenteId(rs.getInt("utente_id"));
                    articolo.setProdottoId(rs.getInt("prodotto_id"));
                    articolo.setTipoProdotto(rs.getString("tipo_prodotto"));
                    articolo.setQuantita(rs.getInt("quantita"));
                    articolo.setDataAggiunta(rs.getTimestamp("data_aggiunta"));
                    articolo.setNomeProdotto(rs.getString("nome_articolo")); // Assicurati che Carrello abbia questo setter
                    articolo.setPrezzoUnitario(rs.getBigDecimal("prezzo_unitario_corrente")); // Assicurati che Carrello abbia questo setter
                    articoli.add(articolo);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero articoli carrello per utente ID {0}: {1}", new Object[]{utenteId, e.getMessage()});
            throw e;
        }
        return articoli;
    }

    /**
     * Rimuove un articolo specifico dal carrello di un utente.
     * @param carrelloId L'ID dell'articolo nel carrello.
     * @param utenteId L'ID dell'utente a cui appartiene l'articolo (per sicurezza).
     * @return true se l'articolo è stato rimosso con successo, false altrimenti.
     */
    public boolean rimuoviArticoloCarrello(int carrelloId, int utenteId) throws SQLException {
        String sql = "DELETE FROM carrello WHERE carrello_id = ? AND utente_id = ?";
        boolean success = false;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, carrelloId);
            ps.setInt(2, utenteId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Articolo carrello con ID {0} dell'utente {1} rimosso con successo.", new Object[]{carrelloId, utenteId});
                success = true;
            } else {
                LOGGER.log(Level.WARNING, "Nessun articolo carrello trovato con ID: {0} per l'utente: {1}", new Object[]{carrelloId, utenteId});
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante la rimozione articolo carrello ID {0} per utente {1}: {2}", new Object[]{carrelloId, utenteId, e.getMessage()});
            throw e;
        }
        return success;
    }

    /**
     * Aggiorna la quantità di un articolo specifico nel carrello di un utente.
     * Se la nuova quantità è <= 0, l'articolo viene rimosso.
     * @param carrelloId L'ID dell'articolo nel carrello.
     * @param utenteId L'ID dell'utente a cui appartiene l'articolo (per sicurezza).
     * @param nuovaQuantita La nuova quantità desiderata.
     * @return true se l'operazione ha avuto successo (aggiornamento o rimozione), false altrimenti.
     */
    public boolean aggiornaQuantitaArticoloCarrello(int carrelloId, int utenteId, int nuovaQuantita) throws SQLException {
        if (nuovaQuantita <= 0) {
            return rimuoviArticoloCarrello(carrelloId, utenteId);
        }

        String sql = "UPDATE carrello SET quantita = ?, data_aggiunta = ? WHERE carrello_id = ? AND utente_id = ?";
        boolean success = false;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, nuovaQuantita);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, carrelloId);
            ps.setInt(4, utenteId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Quantità articolo carrello ID {0} dell'utente {1} aggiornata a: {2}", new Object[]{carrelloId, utenteId, nuovaQuantita});
                success = true;
            } else {
                LOGGER.log(Level.WARNING, "Nessun articolo carrello trovato con ID: {0} per l'utente: {1}", new Object[]{carrelloId, utenteId});
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento quantità articolo carrello ID {0} per utente {1}: {2}", new Object[]{carrelloId, utenteId, e.getMessage()});
            throw e;
        }
        return success;
    }

    /**
     * Svuota il carrello di un utente.
     * @param utenteId L'ID dell'utente il cui carrello deve essere svuotato.
     * @return true se il carrello è stato svuotato (almeno un articolo rimosso), false altrimenti.
     */
    public boolean svuotaCarrello(int utenteId) throws SQLException {
        String sql = "DELETE FROM carrello WHERE utente_id = ?";
        boolean success = false;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, utenteId);
            int rowsAffected = ps.executeUpdate();
            LOGGER.log(Level.INFO, "Carrello utente ID {0} svuotato. Articoli rimossi: {1}", new Object[]{utenteId, rowsAffected});
            success = rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante lo svuotamento carrello utente ID {0}: {1}", new Object[]{utenteId, e.getMessage()});
            throw e;
        }
        return success;
    }
}