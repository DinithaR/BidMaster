package com.bidmaster.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.UserDAO;
import com.bidmaster.dao.impl.UserDAOImpl;
import com.bidmaster.model.User;
import com.bidmaster.service.UserService;

public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    
    private UserDAO userDAO;
    
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public void registerUser(User user) throws SQLException {
        try {
            // Check if username already exists
            if (userDAO.getUserByUsername(user.getUsername()) != null) {
                throw new SQLException("Username already exists");
            }
            
            // Check if email already exists
            if (userDAO.getUserByEmail(user.getEmail()) != null) {
                throw new SQLException("Email already exists");
            }
            
            // Set default role if not specified
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("user");
            }
            
            userDAO.insertUser(user);
            LOGGER.log(Level.INFO, "User registered successfully: {0}", user.getUsername());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registering user", e);
            throw e;
        }
    }

    @Override
    public User getUserById(int userId) throws SQLException {
        try {
            return userDAO.getUserById(userId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID", e);
            throw e;
        }
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        try {
            return userDAO.getUserByUsername(username);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by username", e);
            throw e;
        }
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        try {
            return userDAO.getUserByEmail(email);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email", e);
            throw e;
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        try {
            return userDAO.getAllUsers();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
            throw e;
        }
    }

    @Override
    public List<User> searchUsers(String searchTerm) throws SQLException {
        try {
            return userDAO.searchUsers(searchTerm);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching users", e);
            throw e;
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        try {
            return userDAO.updateUser(user);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
            throw e;
        }
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {
        try {
            return userDAO.deleteUser(userId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
            throw e;
        }
    }

    @Override
    public boolean authenticateUser(String username, String password) throws SQLException {
        try {
            return userDAO.validateUser(username, password);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error authenticating user", e);
            throw e;
        }
    }
    
    @Override
    public boolean validateUser(String username, String password) throws SQLException {
        try {
            return userDAO.validateUser(username, password);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error validating user", e);
            throw e;
        }
    }

    @Override
    public int getUserCount() throws SQLException {
        try {
            return userDAO.getUserCount();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user count", e);
            throw e;
        }
    }

    @Override
    public int getNewUserCount(int days) throws SQLException {
        try {
            return userDAO.getNewUserCount(days);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting new user count for last " + days + " days", e);
            throw e;
        }
    }

    @Override
    public List<User> getRecentUsers(int limit) throws SQLException {
        try {
            return userDAO.getRecentUsers(limit);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent users with limit " + limit, e);
            throw e;
        }
    }

    @Override
    public boolean isAdmin(User user) {
        if (user == null) {
            return false;
        }
        return "admin".equals(user.getRole());
    }
    
    @Override
    public boolean isSeller(User user) {
        if (user == null) {
            return false;
        }
        return "seller".equals(user.getRole());
    }
    
    @Override
    public List<User> getUsersByRole(String role) throws SQLException {
        try {
            return userDAO.getUsersByRole(role);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users by role: " + role, e);
            throw e;
        }
    }
}
