package com.bidmaster.service;

import java.sql.SQLException;
import java.util.List;

import com.bidmaster.model.Feedback;

/**
 * Service interface for Feedback operations
 */
public interface FeedbackService {
    /**
     * Creates a new feedback
     *
     * @param feedback The feedback to create
     * @return The ID of the created feedback
     * @throws SQLException if a database error occurs
     */
    int createFeedback(Feedback feedback) throws SQLException;

    /**
     * Gets a feedback by its ID
     *
     * @param feedbackId The feedback ID
     * @return The feedback, or null if not found
     * @throws SQLException if a database error occurs
     */
    Feedback getFeedbackById(int feedbackId) throws SQLException;

    /**
     * Gets feedback by transaction ID
     *
     * @param transactionId The transaction ID
     * @return List of feedback for the transaction
     * @throws SQLException if a database error occurs
     */
    List<Feedback> getFeedbackByTransaction(int transactionId) throws SQLException;

    /**
     * Gets feedback given by a user
     *
     * @param userId The user ID
     * @return List of feedback given by the user
     * @throws SQLException if a database error occurs
     */
    List<Feedback> getFeedbackGiven(int userId) throws SQLException;

    /**
     * Gets feedback received by a user
     *
     * @param userId The user ID
     * @return List of feedback received by the user
     * @throws SQLException if a database error occurs
     */
    List<Feedback> getFeedbackReceived(int userId) throws SQLException;

    /**
     * Updates a feedback
     *
     * @param feedback The feedback to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateFeedback(Feedback feedback) throws SQLException;

    /**
     * Deletes a feedback
     *
     * @param feedbackId The feedback ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean deleteFeedback(int feedbackId) throws SQLException;

    /**
     * Gets the average rating received by a user
     *
     * @param userId The user ID
     * @return The average rating, or 0 if no ratings
     * @throws SQLException if a database error occurs
     */
    double getAverageRatingForUser(int userId) throws SQLException;

    /**
     * Checks if a user has already given feedback for a transaction
     *
     * @param userId The user ID
     * @param transactionId The transaction ID
     * @return true if feedback exists, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean hasFeedbackForTransaction(int userId, int transactionId) throws SQLException;
}
