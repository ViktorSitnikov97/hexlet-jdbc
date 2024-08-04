package io.hexlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql1 = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";

            try (Statement statement1 = conn.createStatement()) {
                statement1.execute(sql1);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456789')";
            try (Statement statement2 = conn.createStatement()) {
                statement2.executeUpdate(sql2);
            }

//            var sql3 = "SELECT * FROM users";
//            try (Statement statement3 = conn.createStatement()) {
//                ResultSet resultSet = statement3.executeQuery(sql3);
//                while (resultSet.next()) {
//                    System.out.printf("%s %s\n",
//                            resultSet.getString("username"),
//                            resultSet.getString("phone")
//                    );
//                }
//            }
//
//            var sql4 = "INSERT INTO users (username, phone) VALUES (?, ?)";
//            try (var preparedStatement = conn.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS)) {
//                preparedStatement.setString(1, "Sonny");
//                preparedStatement.setString(2, "777777777");
//                preparedStatement.executeUpdate();
//
//                ResultSet generatedKeys4 = preparedStatement.getGeneratedKeys();
//                if (generatedKeys4.next()) {
//                    System.out.println("generatedKeys4 = " + generatedKeys4.getLong(1));
//                } else {
//                    throw new SQLException("DB have not returned an id after saving the entity");
//                }
//
//                preparedStatement.setString(1, "Lola");
//                preparedStatement.setString(2, "65656565655");
//                preparedStatement.executeUpdate();
//
//                ResultSet generatedKeys5 = preparedStatement.getGeneratedKeys();
//                if (generatedKeys5.next()) {
//                    System.out.println("generatedKeys5 = " + generatedKeys5.getLong(1));
//                } else {
//                    throw new SQLException("DB have not returned an id after saving the entity");
//                }
//
//            }
            var sql5 = "SELECT * FROM users";
            try (PreparedStatement preparedStatement2 = conn.prepareStatement(sql5)) {
                ResultSet result = preparedStatement2.executeQuery();

                while(result.next()){
                    String name = result.getString("username");
                    String phone = result.getString("phone");
                    System.out.printf("%s %s\n",name,phone);
                }
            }
            Map<String, String> users = Map.of(
                    "Sonny", "111111",
                    "Artem", "2222222",
                    "Misha", "33333333",
                    "Alex", "44444444");
            var sql6 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql6, Statement.RETURN_GENERATED_KEYS)) {
                for (var user : users.entrySet()) {
                    var key = user.getKey();
                    var value = user.getValue();
                    preparedStatement.setString(1, key);
                    preparedStatement.setString(2, value);
                    preparedStatement.executeUpdate();

                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        System.out.println(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("DB have not returned an id after saving the entity");
                    }
                }
            }
            List<String> deleteUser = List.of("Artem", "Misha");
            var sql7 = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement1 = conn.prepareStatement(sql7)) {
                for (var username : deleteUser) {
                    preparedStatement1.setString(1, username);
                    preparedStatement1.executeUpdate();
                }
//
            }
            var sql8 = "SELECT * FROM users WHERE id > ?";
            try (PreparedStatement preparedStatement2 = conn.prepareStatement(sql8)) {
                preparedStatement2.setInt(1, 0);
                ResultSet result = preparedStatement2.executeQuery();
                while(result.next()){
                    int id = result.getInt("id");
                    String name = result.getString("username");
                    String phone = result.getString("phone");
                    System.out.printf("%d %s %s\n", id, name, phone);
                }
            }
        }
    }
}