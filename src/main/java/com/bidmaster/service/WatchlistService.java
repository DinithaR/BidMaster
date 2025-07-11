package com.bidmaster.service;

import java.sql.SQLException;
import java.util.List;

import com.bidmaster.model.WatchlistItem;

/**
 * Service interface for Watchlist operations
 */
public interface WatchlistService {
    /**
     * Adds an item to a user's watchlist
     *
     * @param userId The user ID
     * @param itemId The item ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean addToWatchlist(int userId, int itemId) throws SQLException;

    /**
     * Removes an item from a user's watchlist
     *
     * @param userId The user ID
     * @param itemId The item ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean removeFromWatchlist(int userId, int itemId) throws SQLException;

    /**
     * Gets a user's watchlist
     *
     * @param userId The user ID
     * @return List of watchlist items
     * @throws SQLException if a database error occurs
     */
    List<WatchlistItem> getWatchlistByUser(int userId) throws SQLException;

    /**
     * Checks if an item is in a user's watchlist
     *
     * @param userId The user ID
     * @param itemId The item ID
     * @return true if the item is in the watchlist, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean isItemInWatchlist(int userId, int itemId) throws SQLException;
}
