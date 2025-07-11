package com.bidmaster.model;

import java.sql.Timestamp;

/**
 * Model class representing feedback from one user to another
 */
public class Feedback {
    private int feedbackId;
    private int transactionId;
    private int fromUserId;
    private int toUserId;
    private int rating;
    private String comment;
    private Timestamp feedbackDate;
    
    // For display purposes
    private String fromUsername;
    private String toUsername;
    private String itemTitle;
    
    /**
     * Default constructor
     */
    public Feedback() {
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param transactionId The transaction ID
     * @param fromUserId The ID of the user giving feedback
     * @param toUserId The ID of the user receiving feedback
     * @param rating The rating (1-5)
     * @param comment The feedback comment
     */
    public Feedback(int transactionId, int fromUserId, int toUserId, int rating, String comment) {
        this.transactionId = transactionId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.rating = rating;
        this.comment = comment;
    }
    
    /**
     * Constructor with all fields
     * 
     * @param feedbackId The feedback ID
     * @param transactionId The transaction ID
     * @param fromUserId The ID of the user giving feedback
     * @param toUserId The ID of the user receiving feedback
     * @param rating The rating (1-5)
     * @param comment The feedback comment
     * @param feedbackDate The date the feedback was given
     */
    public Feedback(int feedbackId, int transactionId, int fromUserId, int toUserId, 
                   int rating, String comment, Timestamp feedbackDate) {
        this.feedbackId = feedbackId;
        this.transactionId = transactionId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.rating = rating;
        this.comment = comment;
        this.feedbackDate = feedbackDate;
    }
    
    /**
     * Gets the feedback ID
     * 
     * @return The feedback ID
     */
    public int getFeedbackId() {
        return feedbackId;
    }
    
    /**
     * Sets the feedback ID
     * 
     * @param feedbackId The feedback ID
     */
    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    /**
     * Gets the transaction ID
     * 
     * @return The transaction ID
     */
    public int getTransactionId() {
        return transactionId;
    }
    
    /**
     * Sets the transaction ID
     * 
     * @param transactionId The transaction ID
     */
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    /**
     * Gets the ID of the user giving feedback
     * 
     * @return The ID of the user giving feedback
     */
    public int getFromUserId() {
        return fromUserId;
    }
    
    /**
     * Sets the ID of the user giving feedback
     * 
     * @param fromUserId The ID of the user giving feedback
     */
    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }
    
    /**
     * Gets the ID of the user receiving feedback
     * 
     * @return The ID of the user receiving feedback
     */
    public int getToUserId() {
        return toUserId;
    }
    
    /**
     * Sets the ID of the user receiving feedback
     * 
     * @param toUserId The ID of the user receiving feedback
     */
    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }
    
    /**
     * Gets the rating
     * 
     * @return The rating (1-5)
     */
    public int getRating() {
        return rating;
    }
    
    /**
     * Sets the rating
     * 
     * @param rating The rating (1-5)
     */
    public void setRating(int rating) {
        // Ensure rating is between 1 and 5
        if (rating < 1) {
            this.rating = 1;
        } else if (rating > 5) {
            this.rating = 5;
        } else {
            this.rating = rating;
        }
    }
    
    /**
     * Gets the feedback comment
     * 
     * @return The feedback comment
     */
    public String getComment() {
        return comment;
    }
    
    /**
     * Sets the feedback comment
     * 
     * @param comment The feedback comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /**
     * Gets the feedback date
     * 
     * @return The feedback date
     */
    public Timestamp getFeedbackDate() {
        return feedbackDate;
    }
    
    /**
     * Sets the feedback date
     * 
     * @param feedbackDate The feedback date
     */
    public void setFeedbackDate(Timestamp feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
    
    /**
     * Gets the username of the user giving feedback
     * 
     * @return The username of the user giving feedback
     */
    public String getFromUsername() {
        return fromUsername;
    }
    
    /**
     * Sets the username of the user giving feedback
     * 
     * @param fromUsername The username of the user giving feedback
     */
    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }
    
    /**
     * Gets the username of the user receiving feedback
     * 
     * @return The username of the user receiving feedback
     */
    public String getToUsername() {
        return toUsername;
    }
    
    /**
     * Sets the username of the user receiving feedback
     * 
     * @param toUsername The username of the user receiving feedback
     */
    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }
    
    /**
     * Gets the title of the item associated with this feedback
     * 
     * @return The item title
     */
    public String getItemTitle() {
        return itemTitle;
    }
    
    /**
     * Sets the title of the item associated with this feedback
     * 
     * @param itemTitle The item title
     */
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    
    /**
     * Gets a string representation of the rating as stars
     * 
     * @return A string of stars representing the rating
     */
    public String getRatingStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("★");
        }
        for (int i = rating; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", transactionId=" + transactionId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
