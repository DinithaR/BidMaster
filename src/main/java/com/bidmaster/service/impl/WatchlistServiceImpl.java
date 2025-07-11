package com.bidmaster.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.WatchlistDAO;
import com.bidmaster.dao.impl.WatchlistDAOImpl;
import com.bidmaster.model.WatchlistItem;
import com.bidmaster.service.WatchlistService;

/**
 * Implementation of WatchlistService interface
 */
public class WatchlistServiceImpl implements WatchlistService {
    private static final Logger LOGGER = Logger.getLogger(WatchlistServiceImpl.class.getName());
    
    private WatchlistDAO watchlistDAO;
    
    public WatchlistServiceImpl() {
        this.watchlistDAO = new WatchlistDAOImpl();
    }

    @Override
    public boolean addToWatchlist(int userId, int itemId) throws SQLException {
        try {
            return watchlistDAO.addToWatchlist(userId, itemId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding item to watchlist", e);
            throw e;
        }
    }

    @Override
    public boolean removeFromWatchlist(int userId, int itemId) throws SQLException {
        try {
            return watchlistDAO.removeFromWatchlist(userId, itemId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing item from watchlist", e);
            throw e;
        }
    }

    @Override
    public List<WatchlistItem> getWatchlistByUser(int userId) throws SQLException {
        try {
            return watchlistDAO.getWatchlistByUser(userId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting watchlist for user", e);
            throw e;
        }
    }

    @Override
    public boolean isItemInWatchlist(int userId, int itemId) throws SQLException {
        try {
            return watchlistDAO.isItemInWatchlist(userId, itemId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if item is in watchlist", e);
            throw e;
        }
    }
}
