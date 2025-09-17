package la_teca_del_giardiniere.DAO;

import com.mysql.cj.jdbc.MysqlDataSource;
import la_teca_del_giardiniere.classes.Ordine;
import la_teca_del_giardiniere.classes.DettaglioOrdine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineDAO {

    private static final Logger LOGGER = Logger.getLogger(OrdineDAO.class.getName());
    private MysqlDataSource dataSource;

    public OrdineDAO() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword == null || dbPassword.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Variabile d'ambiente DB_PASSWORD non impostata. Impossibile avviare il DAO.");
            throw new SQLException("Database password not found. Cannot connect to the database securely.");
        }
        dataSource.setPassword(dbPassword);
        dataSource.setDatabaseName("la_teca_del_giardiniere");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private Ordine createOrdineFromResultSet(ResultSet rs) throws SQLException {
        return new Ordine(
            rs.getInt("ordine_id"),
            rs.getInt("utente_id"),
            rs.getTimestamp("data_ordine"),
            rs.getString("citta_spedizione"),
            rs.getString("paese_spedizione"),
            rs.getString("CAP_spedizione"),
            rs.getBigDecimal("tot_ordine"),
            rs.getString("metodo_pagamento"),
            rs.getBigDecimal("IVA"),
            rs.getString("stato_ordine"),
            rs.getString("note"),
            rs.getString("nome_spedizione"),
            rs.getString("cognome_spedizione"),
            rs.getString("indirizzo_spedizione"),
            rs.getString("telefono_spedizione"),
            rs.getString("email_spedizione")
        );
    }
    private List<DettaglioOrdine> loadDettagliOrdine(Connection connection, int ordineId) throws SQLException {
        List<DettaglioOrdine> dettagliOrdineList = new ArrayList<>();
        String sqlDettagli = "SELECT do.dettaglio_id, do.ordine_id, do.p_codprodotto, do.accessorio_id, " +
                             "do.prezzo_unitario, do.quantita, " +
                             "p.nomeComune AS nome_pianta, a.nome AS nome_accessorio " +
                             "FROM dettagli_ordine do " +
                             "LEFT JOIN piante p ON do.p_codprodotto = p.id " +
                             "LEFT JOIN accessori a ON do.accessorio_id = a.id " +
                             "WHERE do.ordine_id = ?";

        try (PreparedStatement psDettagli = connection.prepareStatement(sqlDettagli)) {
            psDettagli.setInt(1, ordineId);
            try (ResultSet rsDettagli = psDettagli.executeQuery()) {
                while (rsDettagli.next()) {
                    DettaglioOrdine dettaglio = new DettaglioOrdine();
                    dettaglio.setId(rsDettagli.getInt("dettaglio_id"));
                    dettaglio.setOrdineId(rsDettagli.getInt("ordine_id"));
                    dettaglio.setProdottoId(rsDettagli.getObject("p_codprodotto", Integer.class));
                    dettaglio.setAccessorioId(rsDettagli.getObject("accessorio_id", Integer.class));
                    dettaglio.setPrezzoUnitario(rsDettagli.getBigDecimal("prezzo_unitario"));
                    dettaglio.setQuantita(rsDettagli.getInt("quantita"));
                    dettagliOrdineList.add(dettaglio);
                }
            }
        }
        return dettagliOrdineList;
    }

    public List<Ordine> getOrdiniByUtenteId(int utenteId) throws SQLException {
        List<Ordine> ordini = new ArrayList<>();
        String sqlOrdini = "SELECT ordine_id, utente_id, data_ordine, citta_spedizione, paese_spedizione, " +
                            "CAP_spedizione, tot_ordine, metodo_pagamento, IVA, stato_ordine FROM ordine WHERE utente_id = ? ORDER BY data_ordine DESC";

        try (Connection connection = getConnection();
             PreparedStatement psOrdini = connection.prepareStatement(sqlOrdini)) {
            psOrdini.setInt(1, utenteId);
            try (ResultSet rsOrdini = psOrdini.executeQuery()) {
                while (rsOrdini.next()) {
                    Ordine ordine = createOrdineFromResultSet(rsOrdini);
                    ordini.add(ordine);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero degli ordini per utenteId: " + utenteId, e);
            throw e;
        }
        return ordini;
    }

    private int insertOrdine(Connection connection, Ordine ordine) throws SQLException {
        String sql = "INSERT INTO ordine (utente_id, data_ordine, citta_spedizione, paese_spedizione, CAP_spedizione, " +
                     "tot_ordine, metodo_pagamento, IVA, stato_ordine, nome_spedizione, cognome_spedizione, " +
                     "indirizzo_spedizione, telefono_spedizione, email_spedizione) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int generatedId = -1;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ordine.getUtenteId());
            ps.setTimestamp(2, ordine.getDataOrdine());
            ps.setString(3, ordine.getCittaSpedizione());
            ps.setString(4, ordine.getPaeseSpedizione());
            ps.setString(5, ordine.getCapSpedizione());
            ps.setBigDecimal(6, ordine.getTotaleOrdine());
            ps.setString(7, ordine.getMetodoPagamento());
            ps.setBigDecimal(8, ordine.getIva());
            ps.setString(9, ordine.getStatoOrdine());
            // Add the new parameters
            ps.setString(10, ordine.getNomeSpedizione());
            ps.setString(11, ordine.getCognomeSpedizione());
            ps.setString(12, ordine.getIndirizzoSpedizione());
            ps.setString(13, ordine.getTelefonoSpedizione());
            ps.setString(14, ordine.getEmailSpedizione());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        ordine.setOrdineId(generatedId);
                        LOGGER.log(Level.INFO, "Ordine inserito con successo, ID: {0}", generatedId);
                    }
                }
            }
        }
        return generatedId;
    }

    private void insertDettaglioOrdine(Connection connection, DettaglioOrdine dettaglio) throws SQLException {
        String sql = "INSERT INTO dettagli_ordine (ordine_id, p_codprodotto, accessorio_id, prezzo_unitario, quantita) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dettaglio.getOrdineId());
            if (dettaglio.getProdottoId() != null) {
                ps.setInt(2, dettaglio.getProdottoId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            if (dettaglio.getAccessorioId() != null) {
                ps.setInt(3, dettaglio.getAccessorioId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setBigDecimal(4, dettaglio.getPrezzoUnitario());
            ps.setInt(5, dettaglio.getQuantita());
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Dettaglio ordine inserito: Ordine ID {0}, Prodotto ID {1}, Accessorio ID {2}",
                                    new Object[]{dettaglio.getOrdineId(), dettaglio.getProdottoId(), dettaglio.getAccessorioId()});
        }
    }

    public int insertOrdineConDettagli(Ordine ordine) throws SQLException {
        int generatedOrderId = -1;
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            generatedOrderId = insertOrdine(connection, ordine);

            connection.commit();
            LOGGER.log(Level.INFO, "Ordine e dettagli inseriti con successo in transazione, ID: {0}", generatedOrderId);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    LOGGER.log(Level.WARNING, "Rollback della transazione per l'ordine con ID: {0}", generatedOrderId);
                } catch (SQLException rbEx) {
                    LOGGER.log(Level.SEVERE, "Errore durante il rollback della transazione.", rbEx);
                }
            }
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inserimento dell'ordine e dei dettagli.", e);
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeEx) {
                    LOGGER.log(Level.SEVERE, "Errore durante la chiusura della connessione.", closeEx);
                }
            }
        }
        return generatedOrderId;
    }

    public Ordine getOrdineById(int ordineId) throws SQLException {
        Ordine ordine = null;
        String sqlOrdine = "SELECT * FROM ordine WHERE ordine_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement psOrdine = connection.prepareStatement(sqlOrdine)) {
            psOrdine.setInt(1, ordineId);
            try (ResultSet rsOrdine = psOrdine.executeQuery()) {
                if (rsOrdine.next()) {
                    ordine = createOrdineFromResultSet(rsOrdine);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero dell'ordine con ID: " + ordineId, e);
            throw e;
        }
        return ordine;
    }

    public List<Ordine> getOrdiniByStato(String stato) throws SQLException {
        List<Ordine> ordini = new ArrayList<>();
        String sqlOrdini = "SELECT * FROM ordine WHERE stato_ordine = ? ORDER BY data_ordine DESC";

        try (Connection connection = getConnection();
             PreparedStatement psOrdini = connection.prepareStatement(sqlOrdini)) {
            psOrdini.setString(1, stato);
            try (ResultSet rsOrdini = psOrdini.executeQuery()) {
                while (rsOrdini.next()) {
                    Ordine ordine = createOrdineFromResultSet(rsOrdini);
                    ordini.add(ordine);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero degli ordini per stato: " + stato, e);
            throw e;
        }
        return ordini;
    }

    public void updateStatoOrdine(Ordine ordine) throws SQLException {
        String sql = "UPDATE ordine SET stato_ordine = ? WHERE ordine_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ordine.getStatoOrdine());
            ps.setInt(2, ordine.getOrdineId());
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Stato dell'ordine con ID {0} aggiornato a: {1}", new Object[]{ordine.getOrdineId(), ordine.getStatoOrdine()});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento dello stato dell'ordine con ID: " + ordine.getOrdineId(), e);
            throw e;
        }
    }
       
    public List<Ordine> getAllOrdini() throws SQLException {
    	        List<Ordine> ordini = new ArrayList<>();
    	        String sql = "SELECT * FROM ordine ORDER BY data_ordine DESC";

    	        try (Connection connection = getConnection();
    	             PreparedStatement ps = connection.prepareStatement(sql);
    	             ResultSet rs = ps.executeQuery()) {
    	            
    	            while (rs.next()) {
    	                Ordine ordine = createOrdineFromResultSet(rs);
    	               ordini.add(ordine);
    	            }
    	        } catch (SQLException e) {
    	            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero di tutti gli ordini.", e);
    	            throw e;
    	        }
    	        return ordini;
    	    }
}