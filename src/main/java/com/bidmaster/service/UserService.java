package com.bidmaster.service;

import java.sql.SQLException;
import java.util.List;

import com.bidmaster.model.User;

/**
 * Service interface for User operations
 */
public interface UserService {
    /**
     * Registers a new user
     *
     * @param user The user to register
     * @throws SQLException if a database error occurs
     */
    void registerUser(User user) throws SQLException;

    /**
     * Gets a user by ID
     *
     * @param userId The user ID
     * @return The user, or null if not found
     * @throws SQLException if a database error occurs
     */
    User getUserById(int userId) throws SQLException;

    /**
     * Gets a user by username
     *
     * @param username The username
     * @return The user, or null if not found
     * @throws SQLException if a database error occurs
     */
    User getUserByUsername(String username) throws SQLException;

    /**
     * Gets a user by email
     *
     * @param email The email
     * @return The user, or null if not found
     * @throws SQLException if a database error occurs
     */
    User getUserByEmail(String email) throws SQLException;

    /**
     * Gets all users
     *
     * @return List of all users
     * @throws SQLException if a database error occurs
     */
    List<User> getAllUsers() throws SQLException;

    /**
     * Searches for users by keyword
     *
     * @param searchTerm The search term
     * @return List of matching users
     * @throws SQLException if a database error occurs
     */
    List<User> searchUsers(String searchTerm) throws SQLException;

    /**
     * Updates a user
     *
     * @param user The user to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateUser(User user) throws SQLException;

    /**
     * Deletes a user
     *
     * @param userId The user ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean deleteUser(int userId) throws SQLException;

    /**
     * Authenticates a user
     *
     * @param username The username
     * @param password The password
     * @return true if authentication is successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean authenticateUser(String username, String password) throws SQLException;

    /**
     * Validates a user's credentials
     *
     * @param username The username
     * @param password The password
     * @return true if validation is successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean validateUser(String username, String password) throws SQLException;

    /**
     * Checks if a user is an admin
     *
     * @param user The user
     * @return true if the user is an admin, false otherwise
     */
    boolean isAdmin(User user);

    /**
     * Checks if a user is a seller
     *
     * @param user The user
     * @return true if the user is a seller, false otherwise
     */
    boolean isSeller(User user);

    /**
     * Gets the total count of users
     *
     * @return The total number of users
     * @throws SQLException if a database error occurs
     */
    int getUserCount() throws SQLException;

    /**
     * Gets the count of new users registered in the last specified number of days
     *
     * @param days The number of days to look back
     * @return The count of new users
     * @throws SQLException if a database error occurs
     */
    int getNewUserCount(int days) throws SQLException;

    /**
     * Gets a list of the most recently registered users
     *
     * @param limit The maximum number of users to return
     * @return List of recent users
     * @throws SQLException if a database error occurs
     */
    List<User> getRecentUsers(int limit) throws SQLException;
    
    /**
     * Gets users by role
     *
     * @param role The role to filter by
     * @return List of users with the specified role
     * @throws SQLException if a database error occurs
     */
    List<User> getUsersByRole(String role) throws SQLException;
}
