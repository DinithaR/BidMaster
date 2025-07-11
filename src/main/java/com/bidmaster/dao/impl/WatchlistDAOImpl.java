package com.bidmaster.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.WatchlistDAO;
import com.bidmaster.model.WatchlistItem;
import com.bidmaster.util.DBConnectionUtil;

/**
 * Implementation of WatchlistDAO interface
 */
public class WatchlistDAOImpl implements WatchlistDAO {
    private static final Logger LOGGER = Logger.getLogger(WatchlistDAOImpl.class.getName());
    
    private static final String ADD_TO_WATCHLIST = 
        "INSERT INTO Watchlist (userId, itemId) VALUES (?, ?)";
    
    private static final String REMOVE_FROM_WATCHLIST = 
        "DELETE FROM Watchlist WHERE userId = ? AND itemId = ?";
    
    private static final String GET_WATCHLIST_BY_USER = 
        "SELECT w.*, i.title AS itemTitle, i.imageUrl AS itemImageUrl, " +
        "i.currentPrice, i.endTime, i.status, u.username AS sellerUsername " +
        "FROM Watchlist w " +
        "JOIN Items i ON w.itemId = i.itemId " +
        "JOIN Users u ON i.sellerId = u.userId " +
        "WHERE w.userId = ? " +
        "ORDER BY w.addedDate DESC";
    
    private static final String CHECK_ITEM_IN_WATCHLIST = 
        "SELECT COUNT(*) FROM Watchlist WHERE userId = ? AND itemId = ?";

    @Override
    public boolean addToWatchlist(int userId, int itemId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_TO_WATCHLIST)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, itemId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Item added to watchlist: User {0}, Item {1}, Rows affected: {2}", 
                    new Object[]{userId, itemId, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding item to watchlist", e);
            throw e;
        }
    }

    @Override
    public boolean removeFromWatchlist(int userId, int itemId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_FROM_WATCHLIST)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, itemId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Item removed from watchlist: User {0}, Item {1}, Rows affected: {2}", 
                    new Object[]{userId, itemId, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing item from watchlist", e);
            throw e;
        }
    }

    @Override
    public List<WatchlistItem> getWatchlistByUser(int userId) throws SQLException {
        List<WatchlistItem> watchlist = new ArrayList<>();
        
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_WATCHLIST_BY_USER)) {
            
            preparedStatement.setInt(1, userId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    WatchlistItem item = extractWatchlistItemFromResultSet(resultSet);
                    watchlist.add(item);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting watchlist for user: " + userId, e);
            throw e;
        }
        
        return watchlist;
    }

    @Override
    public boolean isItemInWatchlist(int userId, int itemId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ITEM_IN_WATCHLIST)) {
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, itemId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
                
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if item is in watchlist", e);
            throw e;
        }
    }
    
    /**
     * Extracts a WatchlistItem object from a ResultSet
     *
     * @param resultSet The ResultSet containing watchlist item data
     * @return The extracted WatchlistItem object
     * @throws SQLException if a database error occurs
     */
    private WatchlistItem extractWatchlistItemFromResultSet(ResultSet resultSet) throws SQLException {
        WatchlistItem item = new WatchlistItem();
        item.setWatchlistId(resultSet.getInt("watchlistId"));
        item.setUserId(resultSet.getInt("userId"));
        item.setItemId(resultSet.getInt("itemId"));
        
        Timestamp addedDate = resultSet.getTimestamp("addedDate");
        if (addedDate != null) {
            item.setAddedDate(addedDate.toLocalDateTime());
        } else {
            item.setAddedDate(LocalDateTime.now());
        }
        
        // Set additional display fields
        item.setItemTitle(resultSet.getString("itemTitle"));
        item.setItemImageUrl(resultSet.getString("itemImageUrl"));
        item.setCurrentPrice(resultSet.getDouble("currentPrice"));
        
        Timestamp endTime = resultSet.getTimestamp("endTime");
        if (endTime != null) {
            item.setEndTime(endTime.toLocalDateTime());
        }
        
        item.setStatus(resultSet.getString("status"));
        item.setSellerUsername(resultSet.getString("sellerUsername"));
        
        return item;
    }
}
