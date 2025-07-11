package com.bidmaster.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Model class representing a transaction between a buyer and seller
 */
public class Transaction {
    private int transactionId;
    private int itemId;
    private int sellerId;
    private int buyerId;
    private BigDecimal amount;
    private Timestamp transactionDate;
    private String status;
    private String paymentMethod;
    
    // For display purposes
    private String itemTitle;
    private String sellerUsername;
    private String buyerUsername;
    private String imageUrl;
    private LocalDateTime endTime;

    /**
     * Default constructor
     */
    public Transaction() {
    }

    /**
     * Constructor with essential fields
     *
     * @param itemId The item ID
     * @param sellerId The seller ID
     * @param buyerId The buyer ID
     * @param amount The transaction amount
     */
    public Transaction(int itemId, int sellerId, int buyerId, BigDecimal amount) {
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.status = "pending";
    }

    /**
     * Constructor with all fields
     *
     * @param transactionId The transaction ID
     * @param itemId The item ID
     * @param sellerId The seller ID
     * @param buyerId The buyer ID
     * @param amount The transaction amount
     * @param transactionDate The transaction date
     * @param status The transaction status
     * @param paymentMethod The payment method
     */
    public Transaction(int transactionId, int itemId, int sellerId, int buyerId,
                      BigDecimal amount, Timestamp transactionDate, String status, String paymentMethod) {
        this.transactionId = transactionId;
        this.itemId = itemId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
        this.paymentMethod = paymentMethod;
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
     * Gets the item ID
     *
     * @return The item ID
     */
    public int getItemId() {
        return itemId;
    }

    /**
     * Sets the item ID
     *
     * @param itemId The item ID
     */
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the seller ID
     *
     * @return The seller ID
     */
    public int getSellerId() {
        return sellerId;
    }

    /**
     * Sets the seller ID
     *
     * @param sellerId The seller ID
     */
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * Gets the buyer ID
     *
     * @return The buyer ID
     */
    public int getBuyerId() {
        return buyerId;
    }

    /**
     * Sets the buyer ID
     *
     * @param buyerId The buyer ID
     */
    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * Gets the transaction amount
     *
     * @return The transaction amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the transaction amount
     *
     * @param amount The transaction amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the transaction date
     *
     * @return The transaction date
     */
    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the transaction date
     *
     * @param transactionDate The transaction date
     */
    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * Gets the transaction status
     *
     * @return The transaction status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the transaction status
     *
     * @param status The transaction status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the payment method
     *
     * @return The payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets the payment method
     *
     * @param paymentMethod The payment method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets the item title
     *
     * @return The item title
     */
    public String getItemTitle() {
        return itemTitle;
    }

    /**
     * Sets the item title
     *
     * @param itemTitle The item title
     */
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    /**
     * Gets the seller username
     *
     * @return The seller username
     */
    public String getSellerUsername() {
        return sellerUsername;
    }

    /**
     * Sets the seller username
     *
     * @param sellerUsername The seller username
     */
    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    /**
     * Gets the buyer username
     *
     * @return The buyer username
     */
    public String getBuyerUsername() {
        return buyerUsername;
    }

    /**
     * Sets the buyer username
     *
     * @param buyerUsername The buyer username
     */
    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }
    
    /**
     * Gets the image URL
     *
     * @return The image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL
     *
     * @param imageUrl The image URL
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the end time
     *
     * @return The end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time
     *
     * @param endTime The end time
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Checks if the transaction is completed
     *
     * @return true if the transaction is completed, false otherwise
     */
    public boolean isCompleted() {
        return "completed".equals(status);
    }

    /**
     * Checks if the transaction is pending
     *
     * @return true if the transaction is pending, false otherwise
     */
    public boolean isPending() {
        return "pending".equals(status);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", itemId=" + itemId +
                ", sellerId=" + sellerId +
                ", buyerId=" + buyerId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
