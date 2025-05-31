package src.com.la_teca_del_giardiniere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.MysqlDataSource;
import src.com.la_teca_del_giardiniere.classes.Accessori; // Corretto nome classe

public class AccessoriDAO {

    private MysqlDataSource dataSource;

    public AccessoriDAO() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("root"); // Considera di usare variabili d'ambiente per le credenziali
        dataSource.setDatabaseName("la_teca_del_giardiniere");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
        // Aggiungi per gestione timezone corretta se necessario
        // dataSource.setServerTimezone("UTC");
    }

    // CREATE
    public void aggiungiAccessori(Accessori accessorio) throws SQLException {
        // L'ID è solitamente AUTO_INCREMENT, quindi non lo inseriamo.
        // La data_inserimento è spesso gestita dal DB (DEFAULT CURRENT_TIMESTAMP) o impostata qui.
        String sql = "INSERT INTO accessori(nome, prezzo, disponibilita, descrizione_breve, descrizione_dettagliata, dimensioni, immagine, categoria, data_inserimento) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
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
                    accessorio.setId(generatedKeys.getInt(1));
                }
            }

        }
    }

    // READ by ID
    public Accessori getAccessorioById(int id) throws SQLException {
        String sql = "SELECT * FROM accessori WHERE id = ?";
        Accessori accessorio = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    accessorio = new Accessori();
                    accessorio.setId(rs.getInt("id"));
                    accessorio.setNome(rs.getString("nome"));
                    accessorio.setPrezzo(rs.getBigDecimal("prezzo"));
                    accessorio.setDisponibilita(rs.getInt("disponibilita"));
                    accessorio.setDescrizioneBreve(rs.getString("descrizione_breve"));
                    accessorio.setDescrizioneDettagliata(rs.getString("descrizione_dettagliata"));
                    accessorio.setDimensioni(rs.getString("dimensioni"));
                    accessorio.setImmagine(rs.getString("immagine"));
                    accessorio.setCategoria(rs.getString("categoria"));
                    accessorio.setDataInserimento(rs.getTimestamp("data_inserimento"));
                }
            }
        }
        return accessorio;
    }

    // READ ALL
    public List<Accessori> getAllAccessori() throws SQLException {
        List<Accessori> listaAccessori = new ArrayList<>();
        String sql = "SELECT * FROM accessori ORDER BY nome ASC"; // O per ID, o data_inserimento
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Accessori accessorio = new Accessori();
                accessorio.setId(rs.getInt("id"));
                accessorio.setNome(rs.getString("nome"));
                accessorio.setPrezzo(rs.getBigDecimal("prezzo"));
                accessorio.setDisponibilita(rs.getInt("disponibilita"));
                accessorio.setDescrizioneBreve(rs.getString("descrizione_breve"));
                accessorio.setDescrizioneDettagliata(rs.getString("descrizione_dettagliata"));
                accessorio.setDimensioni(rs.getString("dimensioni"));
                accessorio.setImmagine(rs.getString("immagine"));
                accessorio.setCategoria(rs.getString("categoria"));
                accessorio.setDataInserimento(rs.getTimestamp("data_inserimento"));
                listaAccessori.add(accessorio);
            }
        }
        return listaAccessori;
    }

    // UPDATE
    public boolean updateAccessori(Accessori accessorio) throws SQLException {
        String sql = "UPDATE accessori SET nome = ?, prezzo = ?, disponibilita = ?, descrizione_breve = ?, descrizione_dettagliata = ?, dimensioni = ?, immagine = ?, categoria = ? WHERE id = ?";
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
            // data_inserimento solitamente non si aggiorna, ma se necessario:
            // preparedStatement.setTimestamp(9, accessorio.getDataInserimento());
            preparedStatement.setInt(9, accessorio.getId()); // La condizione WHERE

            return preparedStatement.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deleteAccessori(int id) throws SQLException {
        String sql = "DELETE FROM accessori WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        }
    }
}