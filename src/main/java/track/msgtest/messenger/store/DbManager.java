package track.msgtest.messenger.store;

import org.sqlite.SQLiteException;
import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.ChatCreateMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.messages.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static track.msgtest.messenger.net.MessengerServer.PATH_TO_DB;

/**
 * Created by ivan on 20.06.17.
 */
public class DbManager implements UserStore, MessageStore, ChatStore {

    private Connection connection;

    public DbManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
        } catch (SQLException ex) {
            System.out.println("Ошибка доступа к базе данных");
        }
    }

    private static java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    @Override
    public User getUser(String name, String pass) {
        String query = "SELECT id FROM users WHERE name = ? AND pass = ?";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setObject(1, name);
            stmt.setObject(2, pass);
            ResultSet rs = stmt.executeQuery();
            User user = new User(rs.getLong( "ID"), name, pass);
            stmt.close();
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public User addUser(String name, String pass) {
        String sql = "INSERT INTO users (id, name, pass) VALUES (?, ?, ?);";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setObject(2, name);
            stmt.setObject(3, pass);
            stmt.executeUpdate();
            stmt.close();
            return getUser(name, pass);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public List<Long> getChatsByUserId(Long userId) {
        return null;
    }

    /**
     * получить информацию о чате
     */
    //Chat getChatById(Long chatId);

    /**
     * Список сообщений из чата
     */
    @Override
    public List<TextMessage> getMessagesFromChat(Long chatId) {
        List<TextMessage> hist = new ArrayList<>();
        String query = "SELECT * FROM messages WHERE chat_id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setObject(1, chatId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TextMessage msg = new TextMessage();
                msg.setType(Type.MSG_TEXT);
                msg.setChatId(chatId);
                msg.setSenderId(rs.getLong("owner_id"));
                msg.setId(rs.getLong("id"));
                msg.setText(rs.getString("msg_text"));
                hist.add(msg);
            }
            stmt.close();
            return hist;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Получить информацию о сообщении
     */
    @Override
    public Message getMessageById(Long messageId) {
        return null;
    }

    /**
     * Добавить сообщение в чат
     */

    @Override
    public void addMessage(TextMessage textMessage) {
        String sql = "INSERT INTO messages (timestamp, owner_id, chat_id, owner_name, msg_text) " +
                "VALUES (?, ?, ?, ?, ?);";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setObject(1, getCurrentTimeStamp().toString());
            stmt.setObject(2, textMessage.getSenderId());
            stmt.setObject(3, textMessage.getChatId());
            stmt.setObject(4, textMessage.getSenderName());
            stmt.setObject(5, textMessage.getText());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long createChat(ChatCreateMessage chatCreateMessage) {
        String sql = "INSERT INTO chats (name) VALUES (?);";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setObject(1, chatCreateMessage.getName());
            stmt.executeUpdate();
            stmt.close();
            return joinChat(chatCreateMessage.getSenderId(), chatCreateMessage.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public long getChatId(String name) {
        String query = "SELECT id FROM chats WHERE name = ?";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setObject(1, name);
            ResultSet rs = stmt.executeQuery();
            long id = rs.getLong( "id");
            stmt.close();
            return id;
        } catch (SQLException e) {
            return -1;
        }
    }



    @Override
    public long joinChat(long userId, String chatName) {
        String sql = "INSERT INTO chat_membership (user_id, chat_id) VALUES (?, ?);";
        PreparedStatement stmt = null;
        long chatId = getChatId(chatName);
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setLong(2, chatId);
            stmt.executeUpdate();
            stmt.close();
            return chatId;
        } catch (SQLiteException ex) {
            return chatId;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public String getChatNameById(long chatId) {
        String query = "SELECT name FROM chats WHERE id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, chatId);
            ResultSet rs = stmt.executeQuery();
            String chatName = rs.getString( "name");
            stmt.close();
            return chatName;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public String getChats(long userId) {
        String query = "SELECT chat_id FROM chat_membership WHERE user_id = ?";
        PreparedStatement stmt = null;
        StringBuilder chats = new StringBuilder("Чаты:");
        try {
            stmt = connection.prepareStatement(query);
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                chats.append(", " + getChatNameById(rs.getLong("chat_id")));
            }
            stmt.close();
            return chats.toString();
        } catch (SQLException ex) {
            return null;
        }
    }
}
