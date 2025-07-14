package la_teca_del_giardiniere.DAO;

import com.mysql.cj.jdbc.MysqlDataSource;

import la_teca_del_giardiniere.classes.Ordine;
import la_teca_del_giardiniere.classes.DettaglioOrdine;

import java.math.BigDecimal;
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
        // RECUPERA LA PASSWORD DA UNA VARIABILE D'AMBIENTE O FILE DI CONFIGURAZIONE!
        // Esempio: System.getenv("DB_PASSWORD") per variabile d'ambiente
        // OPPURE leggi da un file di properties
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword == null || dbPassword.isEmpty()) {
            LOGGER.log(Level.WARNING, "Variabile d'ambiente DB_PASSWORD non impostata. Utilizzo 'root' come fallback (NON RACCOMANDATO IN PRODUZIONE).");
            dbPassword = "root"; // Fallback per sviluppo, ma da EVITARE in produzione
        }
        dataSource.setPassword(dbPassword);
        dataSource.setDatabaseName("la_teca_del_giardiniere");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Metodo helper per caricare i dettagli di un ordine specifico.
     * Evita la duplicazione di codice nei metodi di recupero degli ordini.
     *
     * @param connection La connessione SQL da utilizzare.
     * @param ordineId L'ID dell'ordine di cui recuperare i dettagli.
     * @return Una lista di oggetti DettaglioOrdine.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
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

                    Integer prodottoId = rsDettagli.getObject("p_codprodotto", Integer.class);
                    dettaglio.setProdottoId(prodottoId);

                    Integer accessorioId = rsDettagli.getObject("accessorio_id", Integer.class);
                    dettaglio.setAccessorioId(accessorioId);

                    dettaglio.setPrezzoUnitario(rsDettagli.getBigDecimal("prezzo_unitario"));
                    dettaglio.setQuantita(rsDettagli.getInt("quantita"));
                    dettaglio.setTotaleDettaglio(dettaglio.getPrezzoUnitario().multiply(new BigDecimal(dettaglio.getQuantita())));

                    String nomePianta = rsDettagli.getString("nome_pianta");
                    String nomeAccessorio = rsDettagli.getString("nome_accessorio");
                    if (nomePianta != null) {
                        dettaglio.setNomeProdotto(nomePianta);
                        dettaglio.setTipoProdotto("pianta");
                    } else if (nomeAccessorio != null) {
                        dettaglio.setNomeProdotto(nomeAccessorio);
                        dettaglio.setTipoProdotto("accessorio");
                    } else {
                        dettaglio.setNomeProdotto("Prodotto Sconosciuto");
                        dettaglio.setTipoProdotto("sconosciuto");
                    }
                    dettagliOrdineList.add(dettaglio);
                }
            }
        }
        return dettagliOrdineList;
    }

    /**
     * Recupera tutti gli ordini di un utente, inclusi i dettagli.
     *
     * @param utenteId L'ID dell'utente.
     * @return Una lista di oggetti Ordine con i loro dettagli.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
    public List<Ordine> getOrdiniByUtenteId(int utenteId) throws SQLException {
        List<Ordine> ordini = new ArrayList<>();
        String sqlOrdini = "SELECT ordine_id, utente_id, data_ordine, citta_spedizione, paese_spedizione, " +
                            "CAP_spedizione, tot_ordine, metodo_pagamento, IVA, stato_ordine, note " +
                            "FROM ordine WHERE utente_id = ? ORDER BY data_ordine DESC";

        try (Connection connection = getConnection();
             PreparedStatement psOrdini = connection.prepareStatement(sqlOrdini)) {
            psOrdini.setInt(1, utenteId);
            try (ResultSet rsOrdini = psOrdini.executeQuery()) {
                while (rsOrdini.next()) {
                    Ordine ordine = new Ordine(
                        rsOrdini.getInt("ordine_id"),
                        rsOrdini.getInt("utente_id"),
                        rsOrdini.getTimestamp("data_ordine"),
                        rsOrdini.getString("citta_spedizione"),
                        rsOrdini.getString("paese_spedizione"),
                        rsOrdini.getString("CAP_spedizione"),
                        rsOrdini.getBigDecimal("tot_ordine"),
                        rsOrdini.getString("metodo_pagamento"),
                        rsOrdini.getBigDecimal("IVA"),
                        rsOrdini.getString("stato_ordine"),
                        rsOrdini.getString("note")
                    );

                    // Carica i dettagli usando il metodo helper
                    ordine.setDettagliOrdine(loadDettagliOrdine(connection, ordine.getId()));
                    ordini.add(ordine);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero degli ordini per utenteId: " + utenteId, e);
            throw e;
        }
        return ordini;
    }

    /**
     * Inserisce un nuovo ordine nel database.
     * Questo metodo è privato perché l'inserimento completo dovrebbe avvenire tramite insertOrdineConDettagli.
     *
     * @param connection La connessione SQL da utilizzare (per la transazione).
     * @param ordine L'oggetto Ordine da inserire.
     * @return L'ID generato per il nuovo ordine.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
    private int insertOrdine(Connection connection, Ordine ordine) throws SQLException {
        String sql = "INSERT INTO ordine (utente_id, data_ordine, citta_spedizione, paese_spedizione, CAP_spedizione, " +
                     "tot_ordine, metodo_pagamento, IVA, stato_ordine, note) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            ps.setString(10, ordine.getNote());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        ordine.setId(generatedId);
                        LOGGER.log(Level.INFO, "Ordine inserito con successo, ID: {0}", generatedId);
                    }
                }
            }
        }
        return generatedId;
    }

    /**
     * Inserisce un dettaglio ordine nel database.
     * Questo metodo è privato perché l'inserimento completo dovrebbe avvenire tramite insertOrdineConDettagli.
     *
     * @param connection La connessione SQL da utilizzare (per la transazione).
     * @param dettaglio L'oggetto DettaglioOrdine da inserire.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
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

    /**
     * Inserisce un nuovo ordine e tutti i suoi dettagli in una singola transazione.
     * Questo garantisce che l'ordine e i suoi dettagli vengano salvati insieme o che nessuno venga salvato.
     *
     * @param ordine L'oggetto Ordine da inserire, con i suoi dettagli già popolati.
     * @return L'ID generato per il nuovo ordine, o -1 se l'inserimento fallisce.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
    public int insertOrdineConDettagli(Ordine ordine) throws SQLException {
        int generatedOrderId = -1;
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Inizia la transazione

            // 1. Inserisci l'ordine principale
            generatedOrderId = insertOrdine(connection, ordine);

            // Se l'ordine è stato inserito con successo, inserisci i dettagli
            if (generatedOrderId != -1 && ordine.getDettagliOrdine() != null) {
                for (DettaglioOrdine dettaglio : ordine.getDettagliOrdine()) {
                    dettaglio.setOrdineId(generatedOrderId); // Associa il dettaglio all'ID dell'ordine appena creato
                    insertDettaglioOrdine(connection, dettaglio);
                }
            }

            connection.commit(); // Conferma la transazione
            LOGGER.log(Level.INFO, "Ordine e dettagli inseriti con successo in transazione, ID: {0}", generatedOrderId);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Annulla la transazione in caso di errore
                    LOGGER.log(Level.WARNING, "Rollback della transazione per l'ordine con ID: {0}", generatedOrderId);
                } catch (SQLException rbEx) {
                    LOGGER.log(Level.SEVERE, "Errore durante il rollback della transazione.", rbEx);
                }
            }
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inserimento dell'ordine e dei dettagli.", e);
            throw e; // Rilancia l'eccezione dopo il rollback
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Ripristina l'auto-commit
                    connection.close(); // Chiudi la connessione
                } catch (SQLException closeEx) {
                    LOGGER.log(Level.SEVERE, "Errore durante la chiusura della connessione.", closeEx);
                }
            }
        }
        return generatedOrderId;
    }


    /**
     * Recupera un singolo ordine tramite il suo ID, inclusi i dettagli.
     *
     * @param ordineId L'ID dell'ordine da recuperare.
     * @return L'oggetto Ordine corrispondente, o null se non trovato.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
    public Ordine getOrdineById(int ordineId) throws SQLException {
        Ordine ordine = null;
        String sqlOrdine = "SELECT * FROM ordine WHERE ordine_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement psOrdine = connection.prepareStatement(sqlOrdine)) {
            psOrdine.setInt(1, ordineId);
            try (ResultSet rsOrdine = psOrdine.executeQuery()) {
                if (rsOrdine.next()) {
                    ordine = new Ordine(
                        rsOrdine.getInt("ordine_id"),
                        rsOrdine.getInt("utente_id"),
                        rsOrdine.getTimestamp("data_ordine"),
                        rsOrdine.getString("citta_spedizione"),
                        rsOrdine.getString("paese_spedizione"),
                        rsOrdine.getString("CAP_spedizione"),
                        rsOrdine.getBigDecimal("tot_ordine"),
                        rsOrdine.getString("metodo_pagamento"),
                        rsOrdine.getBigDecimal("IVA"),
                        rsOrdine.getString("stato_ordine"),
                        rsOrdine.getString("note")
                    );
                    // Carica i dettagli usando il metodo helper
                    ordine.setDettagliOrdine(loadDettagliOrdine(connection, ordine.getId()));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero dell'ordine con ID: " + ordineId, e);
            throw e;
        }
        return ordine;
    }

    /**
     * Recupera una lista di ordini in base al loro stato.
     *
     * @param stato Lo stato degli ordini da recuperare (es. "PENDENTE", "SPEDITO").
     * @return Una lista di oggetti Ordine corrispondenti allo stato specificato.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
    public List<Ordine> getOrdiniByStato(String stato) throws SQLException {
        List<Ordine> ordini = new ArrayList<>();
        String sqlOrdini = "SELECT * FROM ordine WHERE stato_ordine = ? ORDER BY data_ordine DESC";

        try (Connection connection = getConnection();
             PreparedStatement psOrdini = connection.prepareStatement(sqlOrdini)) {
            psOrdini.setString(1, stato);
            try (ResultSet rsOrdini = psOrdini.executeQuery()) {
                while (rsOrdini.next()) {
                    Ordine ordine = new Ordine(
                        rsOrdini.getInt("ordine_id"),
                        rsOrdini.getInt("utente_id"),
                        rsOrdini.getTimestamp("data_ordine"),
                        rsOrdini.getString("citta_spedizione"),
                        rsOrdini.getString("paese_spedizione"),
                        rsOrdini.getString("CAP_spedizione"),
                        rsOrdini.getBigDecimal("tot_ordine"),
                        rsOrdini.getString("metodo_pagamento"),
                        rsOrdini.getBigDecimal("IVA"),
                        rsOrdini.getString("stato_ordine"),
                        rsOrdini.getString("note")
                    );
                    // Carica i dettagli usando il metodo helper
                    ordine.setDettagliOrdine(loadDettagliOrdine(connection, ordine.getId()));
                    ordini.add(ordine);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero degli ordini per stato: " + stato, e);
            throw e;
        }
        return ordini;
    }

    /**
     * Aggiorna lo stato di un ordine esistente.
     *
     * @param ordine L'oggetto Ordine con l'ID e il nuovo stato da aggiornare.
     * @throws SQLException Se si verifica un errore durante l'accesso al database.
     */
    public void updateStatoOrdine(Ordine ordine) throws SQLException {
        String sql = "UPDATE ordine SET stato_ordine = ? WHERE ordine_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ordine.getStatoOrdine());
            ps.setInt(2, ordine.getId());
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Stato dell'ordine con ID {0} aggiornato a: {1}", new Object[]{ordine.getId(), ordine.getStatoOrdine()});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento dello stato dell'ordine con ID: " + ordine.getId(), e);
            throw e;
        }
    }
}