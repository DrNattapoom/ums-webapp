package io.muzoo.ssc.webapp.service;

import io.muzoo.ssc.webapp.model.User;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserService {

    /*
     * find user by username
     * create new user
     * delete user
     * list all users
     * update user by user id
     */

    private static final String INSERT_USER_SQL = "INSERT INTO user (username, password, display_name) VALUES (?, ?, ?);";
    private static final String SELECT_USER_SQL = "SELECT * FROM user WHERE username = ?;";

    @Setter
    private DatabaseConnectionService databaseConnectionService;

    // create new user
    public void createUser(String username, String password, String displayName) throws UserServiceException {
        // salt and hash password using bcrypt library
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try {
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
            // set username, password, and display name columns to insert the user
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, displayName);
            // execute the sql
            preparedStatement.executeUpdate();
            // commit the change
            connection.commit();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new UsernameNotUniqueException(String.format("username %s has already been taken", username));
        } catch (SQLException e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    // get user by username
    public User getUserByUsername(String username) {
        try {
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_SQL);
            // set username to select the user
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("display_name")
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) throws UserServiceException {

        UserService userService = new UserService();
        userService.setDatabaseConnectionService(new DatabaseConnectionService());
        User user = userService.getUserByUsername("DrPoom");
        System.out.println(user.getUsername());

    }

}