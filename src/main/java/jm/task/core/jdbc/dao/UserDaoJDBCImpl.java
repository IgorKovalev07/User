package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE users (Id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(20)," +
                "lastName VARCHAR(20), age TINYINT)";
        try (Connection connection = Util.getConnection()) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Ошибка: соединение с БД закрыто!");
            }
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                System.out.println("Таблица создана");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Connection connection = Util.getConnection()) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Ошибка: соединение с БД закрыто!");
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.execute(sql);
                System.out.println("Таблица удалена");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name,lastname,age) VALUES (?, ?, ?)";
        User user = new User(name, lastName, age);
        try (Connection connection = Util.getConnection()) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Ошибка: соединение с БД закрыто!");
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                if (name != null && !name.isEmpty() || lastName != null && !lastName.isEmpty() || age > 0) {
                    preparedStatement.setString(1, user.getName());
                    preparedStatement.setString(2, user.getLastName());
                    preparedStatement.setByte(3, user.getAge());
                    preparedStatement.executeUpdate();
                    System.out.println("User с именем - " + name + " добавлен в базу данных");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection()) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Ошибка: соединение с БД закрыто!");
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                System.out.println("User с id = " + id + " удален");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = Util.getConnection()) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Ошибка: соединение с БД закрыто!");
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery(sql)) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setAge(resultSet.getByte("age"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        try (Connection connection = Util.getConnection()) {
            if (connection == null || connection.isClosed()) {
                throw new SQLException("Ошибка: соединение с БД закрыто!");
            }
            try (Statement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate(sql);
                System.out.println("Таблица очищена");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
