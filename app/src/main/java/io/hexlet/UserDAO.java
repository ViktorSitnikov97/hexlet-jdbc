package io.hexlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {

        if (user.getId() == null) {
            String sql1 = "INSERT INTO users (name, phone) VALUES (? , ?);";
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement1.setString(1, user.getName());
                preparedStatement1.setString(2, user.getPhone());
                preparedStatement1.executeUpdate();

                ResultSet generatedKeys = preparedStatement1.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    user.setId(id);
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            String sql2 = "UPDATE users SET name = ?, phone = ? WHERE id = ?;";
            try (PreparedStatement preparedStatement2 = connection.prepareStatement(sql2)) {
                preparedStatement2.setString(1, user.getName());
                preparedStatement2.setString(2, user.getPhone());
                preparedStatement2.setLong(3, user.getId());
                preparedStatement2.executeUpdate();
            }
        }
    }

    public void delete(User user) throws SQLException {

        if (user.getId() != null) {
            String sql = "DELETE FROM users WHERE id = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, user.getId());
                preparedStatement.executeUpdate();
                System.out.println(user + "has been deleted");
                user.setId(null);
                }
            } else {
            System.out.println("There is no such user in the users table");
        }
    }

    public Optional<User> find(Long id) throws SQLException {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT * FROM users WHERE id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String userName = result.getString( "name");
                String phoneNumber = result.getString("phone");
                User user = new User(userName, phoneNumber);
                user.setId(id);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }
}
