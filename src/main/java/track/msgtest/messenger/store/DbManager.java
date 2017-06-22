package track.msgtest.messenger.store;

import track.msgtest.messenger.User;
import java.sql.*;

import static track.msgtest.messenger.net.MessengerServer.PATH_TO_DB;

/**
 * Created by ivan on 20.06.17.
 */
public class DbManager {

    private Connection connection;

    public DbManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
        } catch (SQLException ex) {
            System.out.println("Ошибка доступа к базе данных");
        }
    }

    public User getUser(String name, String pass) {
        String query = "SELECT ID FROM USERS WHERE NAME = ? AND PASS = ?";

        PreparedStatement stmt = null;
        User user = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setObject(1, name);
            stmt.setObject(2, pass);
            ResultSet rs = stmt.executeQuery();
            user = new User(rs.getLong( "ID"), name, pass);
            stmt.close();
        } catch (SQLException e) {

        } finally {
            return user;
        }
    }

    public User insertUser(String name, String pass) {
        String sql = "INSERT INTO USERS (ID, NAME, PASS) VALUES (?, ?, ?);";

        PreparedStatement stmt = null;
        long id = -1;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setObject(2, name);
            stmt.setObject(3, pass);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {

        } finally {
            return getUser(name, pass);
        }
    }
}
