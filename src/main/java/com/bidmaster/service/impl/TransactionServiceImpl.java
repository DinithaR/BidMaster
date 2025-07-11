package com.bidmaster.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.TransactionDAO;
import com.bidmaster.dao.impl.TransactionDAOImpl;
import com.bidmaster.model.Transaction;
import com.bidmaster.service.TransactionService;

/**
 * Implementation of TransactionService interface
 */
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOGGER = Logger.getLogger(TransactionServiceImpl.class.getName());
    
    private TransactionDAO transactionDAO;
    
    public TransactionServiceImpl() {
        this.transactionDAO = new TransactionDAOImpl();
    }

    @Override
    public int createTransaction(Transaction transaction) throws SQLException {
        try {
            return transactionDAO.insertTransaction(transaction);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating transaction", e);
            throw e;
        }
    }

    @Override
    public Transaction getTransactionById(int transactionId) throws SQLException {
        try {
            return transactionDAO.getTransactionById(transactionId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction by ID", e);
            throw e;
        }
    }

    @Override
    public List<Transaction> getTransactionsBySeller(int sellerId) throws SQLException {
        try {
            return transactionDAO.getTransactionsBySeller(sellerId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by seller", e);
            throw e;
        }
    }

    @Override
    public List<Transaction> getTransactionsByBuyer(int buyerId) throws SQLException {
        try {
            return transactionDAO.getTransactionsByBuyer(buyerId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by buyer", e);
            throw e;
        }
    }

    @Override
    public List<Transaction> getTransactionsByItem(int itemId) throws SQLException {
        try {
            return transactionDAO.getTransactionsByItem(itemId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by item", e);
            throw e;
        }
    }

    @Override
    public List<Transaction> getAllTransactions() throws SQLException {
        try {
            return transactionDAO.getAllTransactions();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all transactions", e);
            throw e;
        }
    }

    @Override
    public boolean updateTransactionStatus(int transactionId, String status) throws SQLException {
        try {
            return transactionDAO.updateTransactionStatus(transactionId, status);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction status", e);
            throw e;
        }
    }

    @Override
    public boolean updateTransactionPaymentMethod(int transactionId, String paymentMethod) throws SQLException {
        try {
            return transactionDAO.updateTransactionPaymentMethod(transactionId, paymentMethod);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction payment method", e);
            throw e;
        }
    }

    @Override
    public double getTotalRevenue() throws SQLException {
        try {
            return transactionDAO.getTotalRevenue();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total revenue", e);
            throw e;
        }
    }

    @Override
    public double getRevenueForPeriod(int days) throws SQLException {
        try {
            return transactionDAO.getRevenueForPeriod(days);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue for period", e);
            throw e;
        }
    }

    @Override
    public List<Transaction> searchTransactions(String searchTerm) throws SQLException {
        try {
            return transactionDAO.searchTransactions(searchTerm);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching transactions", e);
            throw e;
        }
    }

    @Override
    public List<Transaction> getTransactionsByStatus(String status) throws SQLException {
        try {
            return transactionDAO.getTransactionsByStatus(status);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by status", e);
            throw e;
        }
    }

    @Override
    public List<Transaction> getCompletedSalesBySeller(int sellerId) throws SQLException {
        try {
            return transactionDAO.getCompletedSalesBySeller(sellerId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting completed sales by seller", e);
            throw e;
        }
    }

    @Override
    public int getCompletedSalesCountBySeller(int sellerId) throws SQLException {
        try {
            return transactionDAO.getCompletedSalesCountBySeller(sellerId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting completed sales count by seller", e);
            throw e;
        }
    }

    @Override
    public double getTotalRevenueBySeller(int sellerId) throws SQLException {
        try {
            return transactionDAO.getTotalRevenueBySeller(sellerId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total revenue by seller", e);
            throw e;
        }
    }
    
    @Override
    public List<Transaction> getTransactionsByUser(int userId) throws SQLException {
        try {
            // Get transactions where user is either buyer or seller
            List<Transaction> buyerTransactions = transactionDAO.getTransactionsByBuyer(userId);
            List<Transaction> sellerTransactions = transactionDAO.getTransactionsBySeller(userId);
            
            // Combine the lists
            buyerTransactions.addAll(sellerTransactions);
            
            // Sort by transaction date (most recent first)
            buyerTransactions.sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));
            
            return buyerTransactions;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by user", e);
            throw e;
        }
    }
}
