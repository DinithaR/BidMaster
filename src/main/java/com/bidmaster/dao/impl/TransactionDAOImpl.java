package com.bidmaster.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.TransactionDAO;
import com.bidmaster.model.Transaction;
import com.bidmaster.util.DBConnectionUtil;

public class TransactionDAOImpl implements TransactionDAO {
    private static final Logger LOGGER = Logger.getLogger(TransactionDAOImpl.class.getName());
    
    private static final String INSERT_TRANSACTION = 
        "INSERT INTO Transactions (itemId, sellerId, buyerId, amount, status, paymentMethod) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String GET_TRANSACTION_BY_ID = 
        "SELECT t.*, i.title as itemTitle, s.username as sellerUsername, b.username as buyerUsername " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "WHERE t.transactionId = ?";
    
    private static final String GET_TRANSACTIONS_BY_SELLER = 
        "SELECT t.*, i.title as itemTitle, s.username as sellerUsername, b.username as buyerUsername " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "WHERE t.sellerId = ? " +
        "ORDER BY t.transactionDate DESC";
    
    private static final String GET_TRANSACTIONS_BY_BUYER = 
        "SELECT t.*, i.title as itemTitle, s.username as sellerUsername, b.username as buyerUsername " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "WHERE t.buyerId = ? " +
        "ORDER BY t.transactionDate DESC";
    
    private static final String GET_TRANSACTIONS_BY_ITEM = 
        "SELECT t.*, i.title as itemTitle, s.username as sellerUsername, b.username as buyerUsername " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "WHERE t.itemId = ? " +
        "ORDER BY t.transactionDate DESC";
    
    private static final String GET_ALL_TRANSACTIONS = 
        "SELECT t.*, i.title as itemTitle, s.username as sellerUsername, b.username as buyerUsername " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "ORDER BY t.transactionDate DESC";
    
    private static final String UPDATE_TRANSACTION_STATUS = 
        "UPDATE Transactions SET status = ? WHERE transactionId = ?";
    
    private static final String UPDATE_TRANSACTION_PAYMENT_METHOD = 
        "UPDATE Transactions SET paymentMethod = ? WHERE transactionId = ?";
    
    private static final String GET_TOTAL_REVENUE = 
        "SELECT SUM(amount) FROM Transactions WHERE status = 'completed'";
    
    private static final String GET_REVENUE_FOR_PERIOD = 
        "SELECT SUM(amount) FROM Transactions WHERE status = 'completed' AND transactionDate >= DATE_SUB(NOW(), INTERVAL ? DAY)";
    
    private static final String SEARCH_TRANSACTIONS = 
        "SELECT t.*, i.title as itemTitle, s.username as sellerUsername, b.username as buyerUsername " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "WHERE i.title LIKE ? OR s.username LIKE ? OR b.username LIKE ? " +
        "ORDER BY t.transactionDate DESC";
    
    private static final String GET_TRANSACTIONS_BY_STATUS = 
        "SELECT t.*, i.title as itemTitle, s.username as sellerUsername, b.username as buyerUsername " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "WHERE t.status = ? " +
        "ORDER BY t.transactionDate DESC";
    
    private static final String GET_TRANSACTIONS_IN_DATE_RANGE = 
        "SELECT t.*, i.title as itemTitle, s.username as sellerUsername, b.username as buyerUsername " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "WHERE t.transactionDate BETWEEN ? AND ? " +
        "ORDER BY t.transactionDate DESC";
    
    private static final String GET_MONTHLY_REVENUE = 
        "SELECT DATE_FORMAT(transactionDate, '%b %Y') as month, SUM(amount) as revenue " +
        "FROM Transactions " +
        "WHERE status = 'completed' AND transactionDate BETWEEN ? AND ? " +
        "GROUP BY DATE_FORMAT(transactionDate, '%b %Y') " +
        "ORDER BY MIN(transactionDate)";
    
    private static final String GET_COMPLETED_SALES_BY_SELLER = 
        "SELECT t.*, i.title as itemTitle, i.imageUrl, s.username as sellerUsername, b.username as buyerUsername, i.endTime " +
        "FROM Transactions t " +
        "JOIN Items i ON t.itemId = i.itemId " +
        "JOIN Users s ON t.sellerId = s.userId " +
        "JOIN Users b ON t.buyerId = b.userId " +
        "WHERE t.sellerId = ? AND t.status = 'completed' " +
        "ORDER BY t.transactionDate DESC";
    
    private static final String GET_COMPLETED_SALES_COUNT_BY_SELLER = 
        "SELECT COUNT(*) FROM Transactions WHERE sellerId = ? AND status = 'completed'";
    
    private static final String GET_TOTAL_REVENUE_BY_SELLER = 
        "SELECT SUM(amount) FROM Transactions WHERE sellerId = ? AND status = 'completed'";

    @Override
    public int insertTransaction(Transaction transaction) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setInt(1, transaction.getItemId());
            preparedStatement.setInt(2, transaction.getSellerId());
            preparedStatement.setInt(3, transaction.getBuyerId());
            preparedStatement.setBigDecimal(4, transaction.getAmount());
            preparedStatement.setString(5, transaction.getStatus());
            preparedStatement.setString(6, transaction.getPaymentMethod());
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int transactionId = generatedKeys.getInt(1);
                    transaction.setTransactionId(transactionId);
                    LOGGER.log(Level.INFO, "Transaction created successfully: ID {0}, Amount: {1}", 
                            new Object[]{transactionId, transaction.getAmount()});
                    return transactionId;
                } else {
                    throw new SQLException("Creating transaction failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting transaction", e);
            throw e;
        }
    }

    @Override
    public Transaction getTransactionById(int transactionId) throws SQLException {
        Transaction transaction = null;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTION_BY_ID)) {
            
            preparedStatement.setInt(1, transactionId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    transaction = extractTransactionFromResultSet(resultSet);
                    transaction.setItemTitle(resultSet.getString("itemTitle"));
                    transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                    transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transaction by ID: " + transactionId, e);
            throw e;
        }
        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsBySeller(int sellerId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTIONS_BY_SELLER)) {
            
            preparedStatement.setInt(1, sellerId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = extractTransactionFromResultSet(resultSet);
                    transaction.setItemTitle(resultSet.getString("itemTitle"));
                    transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                    transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by seller: " + sellerId, e);
            throw e;
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByBuyer(int buyerId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTIONS_BY_BUYER)) {
            
            preparedStatement.setInt(1, buyerId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = extractTransactionFromResultSet(resultSet);
                    transaction.setItemTitle(resultSet.getString("itemTitle"));
                    transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                    transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by buyer: " + buyerId, e);
            throw e;
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByItem(int itemId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTIONS_BY_ITEM)) {
            
            preparedStatement.setInt(1, itemId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = extractTransactionFromResultSet(resultSet);
                    transaction.setItemTitle(resultSet.getString("itemTitle"));
                    transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                    transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by item: " + itemId, e);
            throw e;
        }
        return transactions;
    }

    @Override
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_TRANSACTIONS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Transaction transaction = extractTransactionFromResultSet(resultSet);
                transaction.setItemTitle(resultSet.getString("itemTitle"));
                transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all transactions", e);
            throw e;
        }
        return transactions;
    }

    @Override
    public boolean updateTransactionStatus(int transactionId, String status) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRANSACTION_STATUS)) {
            
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, transactionId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Transaction status updated: ID {0}, Status: {1}, Rows affected: {2}", 
                    new Object[]{transactionId, status, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction status: " + transactionId, e);
            throw e;
        }
    }

    @Override
    public boolean updateTransactionPaymentMethod(int transactionId, String paymentMethod) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRANSACTION_PAYMENT_METHOD)) {
            
            preparedStatement.setString(1, paymentMethod);
            preparedStatement.setInt(2, transactionId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Transaction payment method updated: ID {0}, Method: {1}, Rows affected: {2}", 
                    new Object[]{transactionId, paymentMethod, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction payment method: " + transactionId, e);
            throw e;
        }
    }

    @Override
    public double getTotalRevenue() throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_REVENUE);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
            return 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total revenue", e);
            throw e;
        }
    }

    @Override
    public double getRevenueForPeriod(int days) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_REVENUE_FOR_PERIOD)) {
            
            preparedStatement.setInt(1, days);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue for period: " + days + " days", e);
            throw e;
        }
    }

    @Override
    public List<Transaction> searchTransactions(String searchTerm) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_TRANSACTIONS)) {
            
            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = extractTransactionFromResultSet(resultSet);
                    transaction.setItemTitle(resultSet.getString("itemTitle"));
                    transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                    transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching transactions: " + searchTerm, e);
            throw e;
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByStatus(String status) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTIONS_BY_STATUS)) {
            
            preparedStatement.setString(1, status);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = extractTransactionFromResultSet(resultSet);
                    transaction.setItemTitle(resultSet.getString("itemTitle"));
                    transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                    transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by status: " + status, e);
            throw e;
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsInDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTIONS_IN_DATE_RANGE)) {
            
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = extractTransactionFromResultSet(resultSet);
                    transaction.setItemTitle(resultSet.getString("itemTitle"));
                    transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                    transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions in date range", e);
            throw e;
        }
        return transactions;
    }

    @Override
    public Map<String, Double> getMonthlyRevenue(LocalDate startDate, LocalDate endDate) throws SQLException {
        Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_MONTHLY_REVENUE)) {
            
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String month = resultSet.getString("month");
                    double revenue = resultSet.getDouble("revenue");
                    monthlyRevenue.put(month, revenue);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting monthly revenue", e);
            throw e;
        }
        return monthlyRevenue;
    }
    
    @Override
    public List<Transaction> getCompletedSalesBySeller(int sellerId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPLETED_SALES_BY_SELLER)) {
            
            preparedStatement.setInt(1, sellerId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = extractTransactionFromResultSet(resultSet);
                    transaction.setItemTitle(resultSet.getString("itemTitle"));
                    transaction.setSellerUsername(resultSet.getString("sellerUsername"));
                    transaction.setBuyerUsername(resultSet.getString("buyerUsername"));
                    transaction.setImageUrl(resultSet.getString("imageUrl"));
                    
                    // Get end time for display
                    Timestamp endTime = resultSet.getTimestamp("endTime");
                    if (endTime != null) {
                        transaction.setEndTime(endTime.toLocalDateTime());
                    }
                    
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting completed sales by seller: " + sellerId, e);
            throw e;
        }
        return transactions;
    }
    
    @Override
    public int getCompletedSalesCountBySeller(int sellerId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPLETED_SALES_COUNT_BY_SELLER)) {
            
            preparedStatement.setInt(1, sellerId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting completed sales count by seller: " + sellerId, e);
            throw e;
        }
    }
    
    @Override
    public double getTotalRevenueBySeller(int sellerId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_REVENUE_BY_SELLER)) {
            
            preparedStatement.setInt(1, sellerId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total revenue by seller: " + sellerId, e);
            throw e;
        }
    }
    
    /**
     * Extracts a Transaction object from a ResultSet
     * 
     * @param resultSet The ResultSet containing transaction data
     * @return The extracted Transaction object
     * @throws SQLException if a database error occurs
     */
    private Transaction extractTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt("transactionId"));
        transaction.setItemId(resultSet.getInt("itemId"));
        transaction.setSellerId(resultSet.getInt("sellerId"));
        transaction.setBuyerId(resultSet.getInt("buyerId"));
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setTransactionDate(resultSet.getTimestamp("transactionDate"));
        transaction.setStatus(resultSet.getString("status"));
        transaction.setPaymentMethod(resultSet.getString("paymentMethod"));
        return transaction;
    }
}
