package la_teca_del_giardiniere.DAO;

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

import com.mysql.cj.jdbc.MysqlDataSource;
import la_teca_del_giardiniere.classes.Accessori;

public class AccessoriDAO {

    private MysqlDataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(AccessoriDAO.class.getName());

    public AccessoriDAO() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("la_teca_del_giardiniere");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
        LOGGER.info("MysqlDataSource inizializzato in AccessoriDAO.");
    }
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private Accessori mapResultSetToAccessorio(ResultSet rs) throws SQLException {
        Accessori accessorio = new Accessori();
        accessorio.setAccessorio_id(rs.getInt("accessorio_id"));
        accessorio.setNome(rs.getString("nome"));
        accessorio.setPrezzo(rs.getBigDecimal("prezzo"));
        accessorio.setDisponibilita(rs.getInt("disponibilita"));
        accessorio.setDescrizioneBreve(rs.getString("descrizioneBreve"));
        accessorio.setDescrizioneDettagliata(rs.getString("descrizioneDettagliata"));
        accessorio.setDimensioni(rs.getString("dimensioni"));
        accessorio.setImmagine(rs.getString("immagine"));
        accessorio.setCategoria(rs.getString("categoria"));
        accessorio.setDataInserimento(rs.getTimestamp("dataInserimento"));
        return accessorio;
    }
    
    // AGGIUNGI
    public void aggiungiAccessori(Accessori accessorio) throws SQLException {
        // L'ID è solitamente AUTO_INCREMENT, quindi non lo inseriamo nella lista delle colonne da inserire.
        // La dataInserimento è spesso gestita dal DB (DEFAULT CURRENT_TIMESTAMP) o impostata qui.
        // Se accessorio_id è AUTO_INCREMENT, non lo si include nell'INSERT. La query è già corretta così.
        String sql = "INSERT INTO accessori(nome, prezzo, disponibilita, descrizioneBreve, descrizioneDettagliata, dimensioni, immagine, categoria, dataInserimento) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, accessorio.getNome());
            preparedStatement.setBigDecimal(2, accessorio.getPrezzo());
            preparedStatement.setInt(3, accessorio.getDisponibilita());
            preparedStatement.setString(4, accessorio.getDescrizioneBreve());
            preparedStatement.setString(5, accessorio.getDescrizioneDettagliata());
            preparedStatement.setString(6, accessorio.getDimensioni());
            preparedStatement.setString(7, accessorio.getImmagine());
            preparedStatement.setString(8, accessorio.getCategoria());
            if (accessorio.getDataInserimento() == null) {
                preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            } else {
                preparedStatement.setTimestamp(9, accessorio.getDataInserimento());
            }

            preparedStatement.executeUpdate();

            // Opzionale: recupera l'ID generato se necessario
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    accessorio.setAccessorio_id(generatedKeys.getInt(1));
                }
            }

        }
    }

    // Leggi by accessorio_id
    public Accessori getAccessorioByaccessorio_id(int accessorio_id) throws SQLException { 
        String sql = "SELECT * FROM accessori WHERE accessorio_id = ?"; 
        Accessori accessorio = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accessorio_id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    accessorio = new Accessori();
                    accessorio.setAccessorio_id(rs.getInt("accessorio_id")); 
                    accessorio.setNome(rs.getString("nome"));
                    accessorio.setPrezzo(rs.getBigDecimal("prezzo"));
                    accessorio.setDisponibilita(rs.getInt("disponibilita"));
                    accessorio.setDescrizioneBreve(rs.getString("descrizioneBreve"));
                    accessorio.setDescrizioneDettagliata(rs.getString("descrizioneDettagliata"));
                    accessorio.setDimensioni(rs.getString("dimensioni"));
                    accessorio.setImmagine(rs.getString("immagine"));
                    accessorio.setCategoria(rs.getString("categoria"));
                    accessorio.setDataInserimento(rs.getTimestamp("dataInserimento"));
                }
            }
        }
        return accessorio;
    }

    // READ ALL
    public List<Accessori> getAllAccessori() throws SQLException {
        List<Accessori> listaAccessori = new ArrayList<>();
        String sql = "SELECT * FROM accessori ORDER BY nome ASC";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Accessori accessorio = new Accessori();
                accessorio.setAccessorio_id(rs.getInt("accessorio_id")); 
                accessorio.setNome(rs.getString("nome"));
                accessorio.setPrezzo(rs.getBigDecimal("prezzo"));
                accessorio.setDisponibilita(rs.getInt("disponibilita"));
                accessorio.setDescrizioneBreve(rs.getString("descrizioneBreve"));
                accessorio.setDescrizioneDettagliata(rs.getString("descrizioneDettagliata"));
                accessorio.setDimensioni(rs.getString("dimensioni"));
                accessorio.setImmagine(rs.getString("immagine"));
                accessorio.setCategoria(rs.getString("categoria"));
                accessorio.setDataInserimento(rs.getTimestamp("dataInserimento"));
                listaAccessori.add(accessorio);
            }
        }
        return listaAccessori;
    }

    public List<Accessori> getAccessoriInEvidenza(int limit) throws SQLException {
        List<Accessori> listaAccessori = new ArrayList<>();
        String sql = "SELECT * FROM accessori ORDER BY dataInserimento DESC LIMIT ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaAccessori.add(mapResultSetToAccessorio(rs));
                }
            }
        }
        return listaAccessori;
    }
    
    // Aggiorna
    public boolean updateAccessori(Accessori accessorio) throws SQLException {
        String sql = "UPDATE accessori SET nome = ?, prezzo = ?, disponibilita = ?, descrizioneBreve = ?, descrizioneDettagliata = ?, dimensioni = ?, immagine = ?, categoria = ? WHERE accessorio_id = ?"; 
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accessorio.getNome());
            preparedStatement.setBigDecimal(2, accessorio.getPrezzo());
            preparedStatement.setInt(3, accessorio.getDisponibilita());
            preparedStatement.setString(4, accessorio.getDescrizioneBreve());
            preparedStatement.setString(5, accessorio.getDescrizioneDettagliata());
            preparedStatement.setString(6, accessorio.getDimensioni());
            preparedStatement.setString(7, accessorio.getImmagine());
            preparedStatement.setString(8, accessorio.getCategoria());
            preparedStatement.setInt(9, accessorio.getAccessorio_id()); 

            return preparedStatement.executeUpdate() > 0;
        }
    }

    // Elimina
    public boolean deleteAccessori(int accessorio_id) throws SQLException { 
        String sql = "DELETE FROM accessori WHERE accessorio_id = ?"; 
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, accessorio_id); 
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante l'eliminazione dell'accessorio con ID: " + accessorio_id, e);
            throw e;
        }
    }
    public List<Accessori> searchByQuery(String searchQuery) throws SQLException {
        List<Accessori> risultatiAccessori = new ArrayList<>();
        
        // La query cerca la stringa in tre campi principali: nome, descrizione e categoria.
        // L'uso di LOWER() e il Prepared Statement con parametri (?) garantisce sicurezza e flessibilità.
        String sql = "SELECT * FROM accessori WHERE "
                   + "LOWER(nome) LIKE LOWER(?) OR "
                   + "LOWER(descrizioneBreve) LIKE LOWER(?) OR "
                   + "LOWER(categoria) LIKE LOWER(?)";
        
        // La stringa di ricerca viene preparata aggiungendo i simboli jolly '%'
        String likeQuery = "%" + searchQuery.trim() + "%";
        
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            // Imposta il parametro di ricerca per i tre campi nella query.
            // Poiché usiamo LOWER(?) e la stringa è già formattata con %, la ricerca è case-insensitive.
            preparedStatement.setString(1, likeQuery); 
            preparedStatement.setString(2, likeQuery); 
            preparedStatement.setString(3, likeQuery); 
            
            LOGGER.log(Level.INFO, "Esecuzione query di ricerca accessori per: " + likeQuery);
            
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    // Riutilizza il metodo esistente per mappare il risultato su un oggetto Accessori
                    risultatiAccessori.add(mapResultSetToAccessorio(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore SQL durante la ricerca degli accessori per query: " + searchQuery, e);
            // Rilancia l'eccezione per essere gestita dal livello superiore (la Servlet)
            throw e; 
        }
        
        return risultatiAccessori;
    }
}