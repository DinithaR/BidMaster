package com.bidmaster.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.bidmaster.model.Transaction;

/**
 * Data Access Object interface for Transaction
 */
public interface TransactionDAO {
    /**
     * Inserts a new transaction
     * 
     * @param transaction The transaction to insert
     * @return The ID of the inserted transaction
     * @throws SQLException if a database error occurs
     */
    int insertTransaction(Transaction transaction) throws SQLException;
    
    /**
     * Gets a transaction by its ID
     * 
     * @param transactionId The transaction ID
     * @return The transaction, or null if not found
     * @throws SQLException if a database error occurs
     */
    Transaction getTransactionById(int transactionId) throws SQLException;
    
    /**
     * Gets transactions by seller ID
     * 
     * @param sellerId The seller ID
     * @return List of transactions for the seller
     * @throws SQLException if a database error occurs
     */
    List<Transaction> getTransactionsBySeller(int sellerId) throws SQLException;
    
    /**
     * Gets transactions by buyer ID
     * 
     * @param buyerId The buyer ID
     * @return List of transactions for the buyer
     * @throws SQLException if a database error occurs
     */
    List<Transaction> getTransactionsByBuyer(int buyerId) throws SQLException;
    
    /**
     * Gets transactions by item ID
     * 
     * @param itemId The item ID
     * @return List of transactions for the item
     * @throws SQLException if a database error occurs
     */
    List<Transaction> getTransactionsByItem(int itemId) throws SQLException;
    
    /**
     * Gets all transactions
     * 
     * @return List of all transactions
     * @throws SQLException if a database error occurs
     */
    List<Transaction> getAllTransactions() throws SQLException;
    
    /**
     * Updates a transaction's status
     * 
     * @param transactionId The transaction ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateTransactionStatus(int transactionId, String status) throws SQLException;
    
    /**
     * Updates a transaction's payment method
     * 
     * @param transactionId The transaction ID
     * @param paymentMethod The new payment method
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateTransactionPaymentMethod(int transactionId, String paymentMethod) throws SQLException;
    
    /**
     * Gets the total revenue from completed transactions
     * 
     * @return The total revenue
     * @throws SQLException if a database error occurs
     */
    double getTotalRevenue() throws SQLException;
    
    /**
     * Gets the revenue for a specific period
     * 
     * @param days The number of days to look back
     * @return The revenue for the period
     * @throws SQLException if a database error occurs
     */
    double getRevenueForPeriod(int days) throws SQLException;
    
    /**
     * Searches for transactions by keyword
     * 
     * @param searchTerm The search term
     * @return List of matching transactions
     * @throws SQLException if a database error occurs
     */
    List<Transaction> searchTransactions(String searchTerm) throws SQLException;
    
    /**
     * Gets transactions by status
     * 
     * @param status The status to filter by
     * @return List of transactions with the specified status
     * @throws SQLException if a database error occurs
     */
    List<Transaction> getTransactionsByStatus(String status) throws SQLException;
    
    /**
     * Gets transactions within a date range
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of transactions within the date range
     * @throws SQLException if a database error occurs
     */
    List<Transaction> getTransactionsInDateRange(LocalDate startDate, LocalDate endDate) throws SQLException;
    
    /**
     * Gets monthly revenue data
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return Map with months as keys and revenue as values
     * @throws SQLException if a database error occurs
     */
    Map<String, Double> getMonthlyRevenue(LocalDate startDate, LocalDate endDate) throws SQLException;
    
    /**
     * Gets completed sales by seller
     *
     * @param sellerId The seller ID
     * @return List of completed sales for the seller
     * @throws SQLException if a database error occurs
     */
    List<Transaction> getCompletedSalesBySeller(int sellerId) throws SQLException;
    
    /**
     * Gets the count of completed sales by seller
     *
     * @param sellerId The seller ID
     * @return The count of completed sales
     * @throws SQLException if a database error occurs
     */
    int getCompletedSalesCountBySeller(int sellerId) throws SQLException;
    
    /**
     * Gets the total revenue by seller
     *
     * @param sellerId The seller ID
     * @return The total revenue for the seller
     * @throws SQLException if a database error occurs
     */
    double getTotalRevenueBySeller(int sellerId) throws SQLException;
}
