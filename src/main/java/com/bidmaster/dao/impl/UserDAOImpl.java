package com.bidmaster.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.UserDAO;
import com.bidmaster.model.User;
import com.bidmaster.util.DBConnectionUtil;

public class UserDAOImpl implements UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());
    
    // SQL queries
    private static final String INSERT_USER = 
        "INSERT INTO Users (username, email, password, fullName, contactNo, role, profileImage) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String GET_USER_BY_ID = 
        "SELECT * FROM Users WHERE userId = ?";
    
    private static final String GET_USER_BY_USERNAME = 
        "SELECT * FROM Users WHERE username = ?";
    
    private static final String GET_USER_BY_EMAIL = 
        "SELECT * FROM Users WHERE email = ?";
    
    private static final String GET_ALL_USERS = 
        "SELECT * FROM Users ORDER BY registrationDate DESC";
    
    private static final String SEARCH_USERS = 
        "SELECT * FROM Users WHERE username LIKE ? OR email LIKE ? OR fullName LIKE ? ORDER BY registrationDate DESC";
    
    private static final String UPDATE_USER = 
        "UPDATE Users SET email = ?, password = ?, fullName = ?, contactNo = ?, role = ?, profileImage = ? " +
        "WHERE userId = ?";
    
    private static final String DELETE_USER = 
        "DELETE FROM Users WHERE userId = ?";
    
    private static final String VALIDATE_USER = 
        "SELECT * FROM Users WHERE username = ? AND password = ?";
    
    private static final String GET_USER_COUNT = 
        "SELECT COUNT(*) FROM Users";
    
    private static final String GET_NEW_USER_COUNT = 
        "SELECT COUNT(*) FROM Users WHERE registrationDate >= DATE_SUB(NOW(), INTERVAL ? DAY)";
    
    private static final String GET_RECENT_USERS = 
        "SELECT * FROM Users ORDER BY registrationDate DESC LIMIT ?";
    
    private static final String GET_USERS_IN_DATE_RANGE = 
        "SELECT * FROM Users WHERE registrationDate BETWEEN ? AND ? ORDER BY registrationDate DESC";
    
    private static final String GET_MONTHLY_REGISTRATIONS = 
        "SELECT DATE_FORMAT(registrationDate, '%b %Y') as month, COUNT(*) as count " +
        "FROM Users " +
        "WHERE registrationDate BETWEEN ? AND ? " +
        "GROUP BY DATE_FORMAT(registrationDate, '%b %Y') " +
        "ORDER BY MIN(registrationDate)";
    
    private static final String GET_ACTIVE_USERS = 
        "SELECT DISTINCT u.* FROM Users u " +
        "LEFT JOIN Bids b ON u.userId = b.bidderId " +
        "LEFT JOIN Items i ON u.userId = i.sellerId " +
        "WHERE (b.bidTime BETWEEN ? AND ?) OR (i.createdAt BETWEEN ? AND ?)";
    
    private static final String GET_TOP_BIDDERS = 
        "SELECT u.*, COUNT(b.bidId) as bidCount " +
        "FROM Users u " +
        "JOIN Bids b ON u.userId = b.bidderId " +
        "WHERE b.bidTime BETWEEN ? AND ? " +
        "GROUP BY u.userId " +
        "ORDER BY bidCount DESC " +
        "LIMIT ?";
    
    private static final String GET_USERS_BY_ROLE = 
        "SELECT * FROM Users WHERE role = ? ORDER BY username";

    @Override
    public void insertUser(User user) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getFullName());
            preparedStatement.setString(5, user.getContactNo());
            preparedStatement.setString(6, user.getRole());
            preparedStatement.setString(7, user.getProfileImage());
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    user.setUserId(userId);
                    LOGGER.log(Level.INFO, "User created successfully: {0}, ID: {1}", 
                            new Object[]{user.getUsername(), userId});
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting user", e);
            throw e;
        }
    }

    @Override
    public User getUserById(int userId) throws SQLException {
        User user = null;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID)) {
            
            preparedStatement.setInt(1, userId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID: " + userId, e);
            throw e;
        }
        
        return user;
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        User user = null;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_USERNAME)) {
            
            preparedStatement.setString(1, username);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by username: " + username, e);
            throw e;
        }
        
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        User user = null;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
            
            preparedStatement.setString(1, email);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email: " + email, e);
            throw e;
        }
        
        return user;
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
            throw e;
        }
        
        return users;
    }

    @Override
    public List<User> searchUsers(String searchTerm) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_USERS)) {
            
            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = extractUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching users: " + searchTerm, e);
            throw e;
        }
        
        return users;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER)) {
            
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullName());
            preparedStatement.setString(4, user.getContactNo());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.setString(6, user.getProfileImage());
            preparedStatement.setInt(7, user.getUserId());
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "User updated: {0}, Rows affected: {1}", 
                    new Object[]{user.getUsername(), rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getUserId(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            
            preparedStatement.setInt(1, userId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "User deleted: ID {0}, Rows affected: {1}", 
                    new Object[]{userId, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: " + userId, e);
            throw e;
        }
    }

    @Override
    public boolean validateUser(String username, String password) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(VALIDATE_USER)) {
            
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error validating user: " + username, e);
            throw e;
        }
    }

    @Override
    public int getUserCount() throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_COUNT);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user count", e);
            throw e;
        }
    }

    @Override
    public int getNewUserCount(int days) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_NEW_USER_COUNT)) {
            
            preparedStatement.setInt(1, days);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting new user count", e);
            throw e;
        }
    }

    @Override
    public List<User> getRecentUsers(int limit) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_RECENT_USERS)) {
            
            preparedStatement.setInt(1, limit);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = extractUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent users", e);
            throw e;
        }
        
        return users;
    }

    @Override
    public List<User> getUsersRegisteredInDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USERS_IN_DATE_RANGE)) {
            
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = extractUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users in date range", e);
            throw e;
        }
        
        return users;
    }

    @Override
    public Map<String, Integer> getMonthlyRegistrations(LocalDate startDate, LocalDate endDate) throws SQLException {
        Map<String, Integer> monthlyRegistrations = new LinkedHashMap<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_MONTHLY_REGISTRATIONS)) {
            
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String month = resultSet.getString("month");
                    int count = resultSet.getInt("count");
                    monthlyRegistrations.put(month, count);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting monthly registrations", e);
            throw e;
        }
        
        return monthlyRegistrations;
    }

    @Override
    public List<User> getActiveUsers(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACTIVE_USERS)) {
            
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            preparedStatement.setDate(3, Date.valueOf(startDate));
            preparedStatement.setDate(4, Date.valueOf(endDate));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = extractUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting active users", e);
            throw e;
        }
        
        return users;
    }

    @Override
    public List<User> getTopBidders(LocalDate startDate, LocalDate endDate, int limit) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOP_BIDDERS)) {
            
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            preparedStatement.setInt(3, limit);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = extractUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting top bidders", e);
            throw e;
        }
        
        return users;
    }
    
    @Override
    public List<User> getUsersByRole(String role) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USERS_BY_ROLE)) {
            
            preparedStatement.setString(1, role);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = extractUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users by role: " + role, e);
            throw e;
        }
        
        return users;
    }
    
    /**
     * Extracts a User object from a ResultSet
     * 
     * @param resultSet The ResultSet containing user data
     * @return The extracted User object
     * @throws SQLException if a database error occurs
     */
    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("userId"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setFullName(resultSet.getString("fullName"));
        user.setContactNo(resultSet.getString("contactNo"));
        user.setRole(resultSet.getString("role"));
        user.setRegistrationDate(resultSet.getTimestamp("registrationDate"));
        user.setProfileImage(resultSet.getString("profileImage"));
        return user;
    }
}
