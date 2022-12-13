package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {
    public UserDaoJDBCImpl() {

    }
    Connection connection = getConnection();

    public void createUsersTable() {
        Statement statement = null;
        String createTable = "CREATE TABLE IF NOT EXISTS users(    ID INT AUTO_INCREMENT PRIMARY KEY,    NAME VARCHAR(30) NOT NULL,    LASTNAME VARCHAR(30) NOT NULL,    AGE TINYINT DEFAULT 0)";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }

    public void dropUsersTable() {
        Statement statement = null;
        String dropTable = "drop table if exists users;";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(dropTable);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        PreparedStatement preparedStatement = null;
        String saveUser = "INSERT INTO users (NAME, LASTNAME, AGE) VALUES (?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(saveUser);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeUserById(long id) {
        Statement statement = null;
        String removeByID = "DELETE FROM users WHERE ID = " + id;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(removeByID);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }

    public List<User> getAllUsers() {
        Statement statement = null;
        String getAll = "SELECT * FROM users;";
        List<User> userList = null;
        try {
            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery(getAll);
            userList = new ArrayList<User>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong(1));
                user.setName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setAge(rs.getByte(4));
                userList.add(user);
                System.out.println(user.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
        return userList;
    }

    public void cleanUsersTable() {
        Statement statement = null;
        String cleanTable = "TRUNCATE TABLE users;";

        try {
            statement = connection.createStatement();
            statement.executeUpdate(cleanTable);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(statement);
        }
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
