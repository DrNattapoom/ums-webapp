package io.muzoo.ssc.webapp.service;

import io.muzoo.ssc.webapp.model.User;

import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * singleton design pattern
 */
public class UserService {

    /*
     * find user by username
     * create new user
     * delete user
     * list all users
     * update user by user id
     */

    private static UserService userService;

    private static final String INSERT_USER_SQL = "INSERT INTO user (username, password, display_name) VALUES (?, ?, ?);";
    private static final String DELETE_USER_SQL = "DELETE FROM user WHERE username = ?;";
    private static final String UPDATE_USER_SQL = "UPDATE user SET display_name = ? WHERE username = ?;";
    private static final String UPDATE_USER_PASSWORD_SQL = "UPDATE user SET password = ? WHERE username = ?;";
    private static final String SELECT_USER_SQL = "SELECT * FROM user WHERE username = ?;";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM user;";

    @Setter
    private DatabaseConnectionService databaseConnectionService;

    private UserService() {
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
            userService.setDatabaseConnectionService(DatabaseConnectionService.getInstance());
        }
        return userService;
    }

    // create new user
    public void createUser(String username, String password, String displayName) throws UserServiceException {
        // salt and hash password using bcrypt library
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        try (
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
        ) {
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
        try (
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_SQL);
        ) {
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
            return null;
        }
    }

    // list all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_SQL);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(
                    new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("display_name")
                    )
                );
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    // delete user
    public boolean deleteUserByUsername(String username) {
        try (
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL);
        ) {
            // set username to select the user
            preparedStatement.setString(1, username);
            int count = preparedStatement.executeUpdate();
            connection.commit();
            return count > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // update user display name by user username
    public void editUserByUsername(String username, String displayName) throws UserServiceException {
        try (
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL);
        ) {
            // set username and display name columns to insert the user
            preparedStatement.setString(1, displayName);
            preparedStatement.setString(2, username);
            // execute the sql
            preparedStatement.executeUpdate();
            // commit the change
            connection.commit();
        } catch (SQLException e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    // change user password
    public void changePassword(String username, String password) throws UserServiceException {
        try (
            Connection connection = databaseConnectionService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_PASSWORD_SQL);
        ) {
            // set username and display name columns to insert the user
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, username);
            // execute the sql
            preparedStatement.executeUpdate();
            // commit the change
            connection.commit();
        } catch (SQLException e) {
            throw new UserServiceException(e.getMessage());
        }
    }

    public static void main(String[] args) throws UserServiceException {

        UserService userService = UserService.getInstance();
        userService.createUser("test", "12345", "Test");
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user.getUsername());
        }

    }

}
