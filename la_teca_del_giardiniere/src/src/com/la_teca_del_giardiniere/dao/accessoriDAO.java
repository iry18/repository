package src.com.la_teca_del_giardiniere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
 
import com.mysql.cj.jdbc.MysqlDataSource;

import src.com.la_teca_del_giardiniere.classes.accessori;

public class accessoriDAO {

    private MysqlDataSource dataSource;

    public accessoriDAO() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDatabaseName("la_teca_del_giardiniere");
        dataSource.setUseSSL(false);
        dataSource.setAllowPublicKeyRetrieval(true);
    }

    public void aggiungiAccessori(accessori accessorio) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO accessori(id, Nome, prezzo, disponibilita, descrizione, dimensioni, data_inserimento) VALUES(?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setInt(1, accessorio.getId());
            preparedStatement.setString(2, accessorio.getNome());
            preparedStatement.setBigDecimal(3, accessorio.isPrezzo()); 
            preparedStatement.setInt(4, accessorio.getDisponibilita());
            preparedStatement.setString(5, accessorio.getDescrizione());
            preparedStatement.setString(6, accessorio.getDimensioni());
            preparedStatement.setTimestamp(7, accessorio.getData_inserimento());

            preparedStatement.executeUpdate();
        }
    }
}