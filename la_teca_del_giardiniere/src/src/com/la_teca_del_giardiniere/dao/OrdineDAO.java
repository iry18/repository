package src.com.la_teca_del_giardiniere.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import src.com.la_teca_del_giardiniere.classes.Ordine;
import src.com.la_teca_del_giardiniere.classes.DettaglioOrdine; // AGGIUNTO: Import per DettaglioOrdine

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger; // AGGIUNTO: Per un logging più robusto

public class OrdineDAO {

    private static final Logger LOGGER = Logger.getLogger(OrdineDAO.class.getName()); // Logger
    private MysqlDataSource dataSource;

    public OrdineDAO() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("root"); // ATTENZIONE: Password hardcoded, considera un file di configurazione
        dataSource.setDatabaseName("la_teca_del_giardiniere");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Metodo per recuperare tutti gli ordini di un utente, inclusi i dettagli
    public List<Ordine> getOrdiniByUtenteId(int utenteId) throws SQLException {
        List<Ordine> ordini = new ArrayList<>();
        Connection connection = null;
        PreparedStatement psOrdini = null;
        PreparedStatement psDettagli = null; // Dichiaralo qui per poterlo chiudere nel finally
        ResultSet rsOrdini = null;
        ResultSet rsDettagli = null; // Dichiaralo qui per poterlo chiudere nel finally

        // Query per recuperare gli ordini dell'utente
        // CAMBIATO: Nomi colonne per seguire la convenzione camelCase del modello Ordine
        String sqlOrdini = "SELECT ordine_id, utente_id, data_ordine, citta_spedizione, paese_spedizione, " +
                           "CAP_spedizione, tot_ordine, metodo_pagamento, IVA, stato_ordine, note " +
                           "FROM ordine WHERE utente_id = ? ORDER BY data_ordine DESC";

        // Query per recuperare i dettagli di un ordine specifico
        // Unisci con piante e accessori per ottenere i nomi dei prodotti
        // CAMBIATO: Nomi colonne per seguire la convenzione camelCase del modello DettaglioOrdine
        String sqlDettagli = "SELECT do.dettaglio_id, do.ordine_id, do.p_codprodotto, do.accessorio_id, " +
                             "do.prezzo_unitario, do.quantita, " + // Assumi che 'totale_dettaglio' non sia nel DB se lo calcoli in Java
                             "p.nomeComune AS nome_pianta, a.nome AS nome_accessorio " +
                             "FROM dettagli_ordine do " +
                             "LEFT JOIN piante p ON do.p_codprodotto = p.id " + // ASSUMENDO: id è il PK di piante
                             "LEFT JOIN accessori a ON do.accessorio_id = a.id " + // ASSUMENDO: id è il PK di accessori
                             "WHERE do.ordine_id = ?";

        try {
            connection = getConnection();
            psOrdini = connection.prepareStatement(sqlOrdini);
            psOrdini.setInt(1, utenteId);
            rsOrdini = psOrdini.executeQuery();

            while (rsOrdini.next()) {
                // CAMBIATO: Utilizzo del costruttore con tutti i campi (incluso ID)
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

                // Recupera i dettagli per l'ordine corrente
                List<DettaglioOrdine> dettagliOrdineList = new ArrayList<>();
                psDettagli = connection.prepareStatement(sqlDettagli); // Prepara statement Dettagli all'interno del loop
                psDettagli.setInt(1, ordine.getId()); // CAMBIATO: ordine.getId()
                rsDettagli = psDettagli.executeQuery();

                while (rsDettagli.next()) {
                    DettaglioOrdine dettaglio = new DettaglioOrdine();
                    dettaglio.setId(rsDettagli.getInt("dettaglio_id")); // CAMBIATO: setDettaglio_id a setId
                    dettaglio.setOrdineId(rsDettagli.getInt("ordine_id")); // CAMBIATO: setOrdine_id a setOrdineId

                    // Gestione di p_codprodotto e accessorio_id che possono essere NULL
                    Integer prodottoId = rsDettagli.getObject("p_codprodotto", Integer.class); // Usa getObject per tipi Wrapper
                    if (rsDettagli.wasNull()) {
                        prodottoId = null; // Assegna null se il valore del DB è NULL
                    }
                    dettaglio.setProdottoId(prodottoId != null ? prodottoId : 0); // Assegna il valore o 0 se null

                    Integer accessorioId = rsDettagli.getObject("accessorio_id", Integer.class);
                     if (rsDettagli.wasNull()) {
                        accessorioId = null;
                    }
                    // DEVI DECIDERE: se prodottoId e accessorioId sono entrambi nullable nel DB,
                    // la tua classe DettaglioOrdine dovrebbe avere campi Integer (wrapper) per gestirli come null
                    // Altrimenti, se sono int primitivi, devi decidere un valore di default (es. 0) per null.
                    // Per ora userò 0 come default per i primitivi int.
                    dettaglio.setId(accessorioId != null ? accessorioId : 0); // Assegna il valore o 0 se null

                    dettaglio.setPrezzoUnitario(rsDettagli.getBigDecimal("prezzo_unitario")); // CAMBIATO: setPrezzo_unitario a setPrezzoUnitario
                    dettaglio.setQuantita(rsDettagli.getInt("quantita"));

                    // Calcola il totale dettaglio se non è nel DB, altrimenti recuperalo
                    // Se 'totale_dettaglio' è nel DB, recuperalo: dettaglio.setTotaleDettaglio(rsDettagli.getBigDecimal("totale_dettaglio"));
                    dettaglio.setTotaleDettaglio(dettaglio.getPrezzoUnitario().multiply(new BigDecimal(dettaglio.getQuantita()))); // Calcolo

                    // Imposta il nome del prodotto/accessorio e il tipo di prodotto
                    String nomePianta = rsDettagli.getString("nome_pianta");
                    String nomeAccessorio = rsDettagli.getString("nome_accessorio");
                    if (nomePianta != null) {
                        dettaglio.setNomeProdotto(nomePianta);
                        dettaglio.setTipoProdotto("pianta"); // AGGIUNTO: Imposta il tipo
                    } else if (nomeAccessorio != null) {
                        dettaglio.setNomeProdotto(nomeAccessorio);
                        dettaglio.setTipoProdotto("accessorio"); // AGGIUNTO: Imposta il tipo
                    } else {
                        dettaglio.setNomeProdotto("Prodotto Sconosciuto"); // Fallback
                        dettaglio.setTipoProdotto("sconosciuto"); // Fallback
                    }
                    dettagliOrdineList.add(dettaglio);
                }
                ordine.setDettagliOrdine(dettagliOrdineList);
                ordini.add(ordine);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero degli ordini per utenteId: " + utenteId, e);
            throw e; // Rilancia l'eccezione per essere gestita a un livello superiore (es. nella Servlet)
        } finally {
            // Chiudi le risorse in ordine inverso di apertura e gestisci le eccezioni di chiusura
            try { if (rsDettagli != null) rsDettagli.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di rsDettagli.", e); }
            try { if (psDettagli != null) psDettagli.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di psDettagli.", e); }
            try { if (rsOrdini != null) rsOrdini.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di rsOrdini.", e); }
            try { if (psOrdini != null) psOrdini.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di psOrdini.", e); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione.", e); }
        }
        return ordini;
    }

    // AGGIUNTO: Metodo per inserire un nuovo ordine (senza dettagli inizialmente)
    // Questo è fondamentale per creare l'ordine prima di aggiungervi i dettagli
    public int insertOrdine(Ordine ordine) throws SQLException {
        String sql = "INSERT INTO ordine (utente_id, data_ordine, citta_spedizione, paese_spedizione, CAP_spedizione, " +
                     "tot_ordine, metodo_pagamento, IVA, stato_ordine, note) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int generatedId = -1;

        try {
            connection = getConnection();
            // Ottieni l'ID generato automaticamente
            ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
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
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    ordine.setId(generatedId); // Imposta l'ID generato nell'oggetto Ordine
                    LOGGER.log(Level.INFO, "Ordine inserito con successo, ID: {0}", generatedId);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inserimento dell'ordine.", e);
            throw e;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di rs.", e); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di ps.", e); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione.", e); }
        }
        return generatedId;
    }

    // AGGIUNTO: Metodo per inserire un dettaglio ordine
    public void insertDettaglioOrdine(DettaglioOrdine dettaglio) throws SQLException {
        String sql = "INSERT INTO dettagli_ordine (ordine_id, p_codprodotto, accessorio_id, prezzo_unitario, quantita) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, dettaglio.getOrdineId());
            // Gestisci i casi in cui p_codprodotto o accessorio_id potrebbero essere NULL
            if (dettaglio.getProdottoId() > 0) { // Presumendo 0 come default per NULL, o usa Integer wrapper
                ps.setInt(2, dettaglio.getProdottoId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            if (dettaglio.getId() > 0) { // Presumendo 0 come default per NULL, o usa Integer wrapper
                ps.setInt(3, dettaglio.getId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setBigDecimal(4, dettaglio.getPrezzoUnitario());
            ps.setInt(5, dettaglio.getQuantita());
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Dettaglio ordine inserito: Ordine ID {0}, Prodotto ID {1}, Accessorio ID {2}",
                       new Object[]{dettaglio.getOrdineId(), dettaglio.getProdottoId(), dettaglio.getId()});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'inserimento del dettaglio ordine.", e);
            throw e;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di ps.", e); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione.", e); }
        }
    }
 // Nel tuo OrdineDAO
    public Ordine getOrdineById(int ordineId) throws SQLException {
        Ordine ordine = null;
        Connection connection = null;
        PreparedStatement psOrdine = null;
        PreparedStatement psDettagli = null;
        ResultSet rsOrdine = null;
        ResultSet rsDettagli = null;

        String sqlOrdine = "SELECT * FROM ordine WHERE ordine_id = ?";
        String sqlDettagli = "SELECT do.*, p.nomeComune AS nome_pianta, a.nome AS nome_accessorio " +
                             "FROM dettagli_ordine do " +
                             "LEFT JOIN piante p ON do.p_codprodotto = p.id " +
                             "LEFT JOIN accessori a ON do.accessorio_id = a.id " +
                             "WHERE do.ordine_id = ?";

        try {
            connection = getConnection();
            psOrdine = connection.prepareStatement(sqlOrdine);
            psOrdine.setInt(1, ordineId);
            rsOrdine = psOrdine.executeQuery();

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

                // Recupera i dettagli per l'ordine corrente
                List<DettaglioOrdine> dettagliOrdineList = new ArrayList<>();
                psDettagli = connection.prepareStatement(sqlDettagli);
                psDettagli.setInt(1, ordine.getId());
                rsDettagli = psDettagli.executeQuery();

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
                ordine.setDettagliOrdine(dettagliOrdineList);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante il recupero dell'ordine con ID: " + ordineId, e);
            throw e;
        } finally {
            try { if (rsDettagli != null) rsDettagli.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di rsDettagli.", e); }
            try { if (psDettagli != null) psDettagli.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di psDettagli.", e); }
            try { if (rsOrdine != null) rsOrdine.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di rsOrdine.", e); }
            try { if (psOrdine != null) psOrdine.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di psOrdine.", e); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione.", e); }
        }
        return ordine;
    }
 // Nel tuo OrdineDAO
    public List<Ordine> getOrdiniByStato(String stato) throws SQLException {
        List<Ordine> ordini = new ArrayList<>();
        Connection connection = null;
        PreparedStatement psOrdini = null;
        PreparedStatement psDettagli = null;
        ResultSet rsOrdini = null;
        ResultSet rsDettagli = null;

        String sqlOrdini = "SELECT * FROM ordine WHERE stato_ordine = ? ORDER BY data_ordine DESC";
        String sqlDettagli = "SELECT do.*, p.nomeComune AS nome_pianta, a.nome AS nome_accessorio " +
                             "FROM dettagli_ordine do " +
                             "LEFT JOIN piante p ON do.p_codprodotto = p.id " +
                             "LEFT JOIN accessori a ON do.accessorio_id = a.id " +
                             "WHERE do.ordine_id = ?";

        try {
            connection = getConnection();
            psOrdini = connection.prepareStatement(sqlOrdini);
            psOrdini.setString(1, stato);
            rsOrdini = psOrdini.executeQuery();

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

                List<DettaglioOrdine> dettagliOrdineList = new ArrayList<>();
                psDettagli = connection.prepareStatement(sqlDettagli);
                psDettagli.setInt(1, ordine.getId());
                rsDettagli = psDettagli.executeQuery();

                while (rsDettagli.next()) {
                    DettaglioOrdine dettaglio = new DettaglioOrdine();
                    dettaglio.setId(rsDettagli.getInt("dettaglio_id"));
                    dettaglio.setOrdineId(rsDettagli.getInt("ordine_id"));
                    dettaglio.setProdottoId(rsDettagli.getObject("p_codprodotto", Integer.class));
                    dettaglio.setAccessorioId(rsDettagli.getObject("accessorio_id", Integer.class));
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
                ordine.setDettagliOrdine(dettagliOrdineList);
                ordini.add(ordine);
            }
        } finally {
            try { if (rsDettagli != null) rsDettagli.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di rsDettagli.", e); }
            try { if (psDettagli != null) psDettagli.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di psDettagli.", e); }
            try { if (rsOrdini != null) rsOrdini.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di rsOrdini.", e); }
            try { if (psOrdini != null) psOrdini.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di psOrdini.", e); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione.", e); }
        }
        return ordini;
    }
 // Nel tuo OrdineDAO
    public void updateStatoOrdine(Ordine ordine) throws SQLException {
        String sql = "UPDATE ordine SET stato_ordine = ? WHERE ordine_id = ?";
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, ordine.getStatoOrdine());
            ps.setInt(2, ordine.getId());
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Stato dell'ordine con ID {0} aggiornato a: {1}", new Object[]{ordine.getId(), ordine.getStatoOrdine()});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'aggiornamento dello stato dell'ordine con ID: " + ordine.getId(), e);
            throw e;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura di ps.", e); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione.", e); }
        }
    }
}