package com.bidmaster.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.FeedbackDAO;
import com.bidmaster.model.Feedback;
import com.bidmaster.util.DBConnectionUtil;

/**
 * Implementation of FeedbackDAO interface
 */
public class FeedbackDAOImpl implements FeedbackDAO {
    private static final Logger LOGGER = Logger.getLogger(FeedbackDAOImpl.class.getName());
    
    private static final String INSERT_FEEDBACK = 
        "INSERT INTO Feedback (transactionId, fromUserId, toUserId, rating, comment) VALUES (?, ?, ?, ?, ?)";
    
    private static final String GET_FEEDBACK_BY_ID = 
        "SELECT f.*, uf.username as fromUsername, ut.username as toUsername, i.title as itemTitle " +
        "FROM Feedback f " +
        "JOIN Users uf ON f.fromUserId = uf.userId " +
        "JOIN Users ut ON f.toUserId = ut.userId " +
        "JOIN Transactions t ON f.transactionId = t.transactionId " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "WHERE f.feedbackId = ?";
    
    private static final String GET_FEEDBACK_BY_TRANSACTION = 
        "SELECT f.*, uf.username as fromUsername, ut.username as toUsername, i.title as itemTitle " +
        "FROM Feedback f " +
        "JOIN Users uf ON f.fromUserId = uf.userId " +
        "JOIN Users ut ON f.toUserId = ut.userId " +
        "JOIN Transactions t ON f.transactionId = t.transactionId " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "WHERE f.transactionId = ?";
    
    private static final String GET_FEEDBACK_GIVEN_BY_USER = 
        "SELECT f.*, uf.username as fromUsername, ut.username as toUsername, i.title as itemTitle " +
        "FROM Feedback f " +
        "JOIN Users uf ON f.fromUserId = uf.userId " +
        "JOIN Users ut ON f.toUserId = ut.userId " +
        "JOIN Transactions t ON f.transactionId = t.transactionId " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "WHERE f.fromUserId = ? ORDER BY f.feedbackDate DESC";
    
    private static final String GET_FEEDBACK_RECEIVED_BY_USER = 
        "SELECT f.*, uf.username as fromUsername, ut.username as toUsername, i.title as itemTitle " +
        "FROM Feedback f " +
        "JOIN Users uf ON f.fromUserId = uf.userId " +
        "JOIN Users ut ON f.toUserId = ut.userId " +
        "JOIN Transactions t ON f.transactionId = t.transactionId " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "WHERE f.toUserId = ? ORDER BY f.feedbackDate DESC";
    
    private static final String UPDATE_FEEDBACK = 
        "UPDATE Feedback SET rating = ?, comment = ? WHERE feedbackId = ?";
    
    private static final String DELETE_FEEDBACK = 
        "DELETE FROM Feedback WHERE feedbackId = ?";
    
    private static final String GET_AVERAGE_RATING = 
        "SELECT AVG(rating) FROM Feedback WHERE toUserId = ?";
    
    private static final String CHECK_FEEDBACK_EXISTS = 
        "SELECT COUNT(*) FROM Feedback WHERE fromUserId = ? AND transactionId = ?";

    @Override
    public int insertFeedback(Feedback feedback) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FEEDBACK, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, feedback.getTransactionId());
            preparedStatement.setInt(2, feedback.getFromUserId());
            preparedStatement.setInt(3, feedback.getToUserId());
            preparedStatement.setInt(4, feedback.getRating());
            preparedStatement.setString(5, feedback.getComment());
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating feedback failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int feedbackId = generatedKeys.getInt(1);
                    feedback.setFeedbackId(feedbackId);
                    LOGGER.log(Level.INFO, "Feedback created successfully: ID {0}, Rating: {1}",
                            new Object[]{feedbackId, feedback.getRating()});
                    return feedbackId;
                } else {
                    throw new SQLException("Creating feedback failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting feedback", e);
            throw e;
        }
    }

    @Override
    public Feedback getFeedbackById(int feedbackId) throws SQLException {
        Feedback feedback = null;
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_FEEDBACK_BY_ID)) {
            
            preparedStatement.setInt(1, feedbackId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    feedback = extractFeedbackFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting feedback by ID: " + feedbackId, e);
            throw e;
        }
        
        return feedback;
    }

    @Override
    public List<Feedback> getFeedbackByTransaction(int transactionId) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_FEEDBACK_BY_TRANSACTION)) {
            
            preparedStatement.setInt(1, transactionId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Feedback feedback = extractFeedbackFromResultSet(resultSet);
                    feedbackList.add(feedback);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting feedback by transaction: " + transactionId, e);
            throw e;
        }
        
        return feedbackList;
    }

    @Override
    public List<Feedback> getFeedbackGiven(int userId) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_FEEDBACK_GIVEN_BY_USER)) {
            
            preparedStatement.setInt(1, userId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Feedback feedback = extractFeedbackFromResultSet(resultSet);
                    feedbackList.add(feedback);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting feedback given by user: " + userId, e);
            throw e;
        }
        
        return feedbackList;
    }

    @Override
    public List<Feedback> getFeedbackReceived(int userId) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_FEEDBACK_RECEIVED_BY_USER)) {
            
            preparedStatement.setInt(1, userId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Feedback feedback = extractFeedbackFromResultSet(resultSet);
                    feedbackList.add(feedback);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting feedback received by user: " + userId, e);
            throw e;
        }
        
        return feedbackList;
    }

    @Override
    public boolean updateFeedback(Feedback feedback) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FEEDBACK)) {
            
            preparedStatement.setInt(1, feedback.getRating());
            preparedStatement.setString(2, feedback.getComment());
            preparedStatement.setInt(3, feedback.getFeedbackId());
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Feedback updated: ID {0}, Rows affected: {1}",
                    new Object[]{feedback.getFeedbackId(), rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating feedback: " + feedback.getFeedbackId(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteFeedback(int feedbackId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FEEDBACK)) {
            
            preparedStatement.setInt(1, feedbackId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Feedback deleted: ID {0}, Rows affected: {1}",
                    new Object[]{feedbackId, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting feedback: " + feedbackId, e);
            throw e;
        }
    }

    @Override
    public double getAverageRatingForUser(int userId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_AVERAGE_RATING)) {
            
            preparedStatement.setInt(1, userId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
                
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting average rating for user: " + userId, e);
            throw e;
        }
    }

    @Override
    public boolean hasFeedbackForTransaction(int userId, int transactionId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FEEDBACK_EXISTS)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, transactionId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
                
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking feedback existence", e);
            throw e;
        }
    }

    /**
     * Extracts a Feedback object from a ResultSet
     *
     * @param resultSet The ResultSet containing feedback data
     * @return The extracted Feedback object
     * @throws SQLException if a database error occurs
     */
    private Feedback extractFeedbackFromResultSet(ResultSet resultSet) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(resultSet.getInt("feedbackId"));
        feedback.setTransactionId(resultSet.getInt("transactionId"));
        feedback.setFromUserId(resultSet.getInt("fromUserId"));
        feedback.setToUserId(resultSet.getInt("toUserId"));
        feedback.setRating(resultSet.getInt("rating"));
        feedback.setComment(resultSet.getString("comment"));
        
        Timestamp feedbackDate = resultSet.getTimestamp("feedbackDate");
        if (feedbackDate != null) {
            feedback.setFeedbackDate(feedbackDate);
        }
        
        // Set additional display fields
        feedback.setFromUsername(resultSet.getString("fromUsername"));
        feedback.setToUsername(resultSet.getString("toUsername"));
        feedback.setItemTitle(resultSet.getString("itemTitle"));
        
        return feedback;
    }
}
