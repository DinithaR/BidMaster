package com.bidmaster.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.BidDAO;
import com.bidmaster.dao.ItemDAO;
import com.bidmaster.dao.impl.BidDAOImpl;
import com.bidmaster.dao.impl.ItemDAOImpl;
import com.bidmaster.model.Bid;
import com.bidmaster.model.Item;
import com.bidmaster.service.BidService;

/**
 * Implementation of BidService interface
 */
public class BidServiceImpl implements BidService {
    private static final Logger LOGGER = Logger.getLogger(BidServiceImpl.class.getName());
    
    private BidDAO bidDAO;
    private ItemDAO itemDAO;
    
    public BidServiceImpl() {
        this.bidDAO = new BidDAOImpl();
        this.itemDAO = new ItemDAOImpl();
    }

    @Override
    public int placeBid(int itemId, int bidderId, BigDecimal bidAmount) throws SQLException {
        try {
            // Get the item to check if bidding is allowed
            Item item = itemDAO.getItemById(itemId);
            
            if (item == null) {
                throw new SQLException("Item not found");
            }
            
            // Check if item is active
            if (!"active".equals(item.getStatus())) {
                throw new SQLException("Bidding is not allowed on this item");
            }
            
            // Check if bid amount is higher than current price
            if (bidAmount.compareTo(item.getCurrentPrice()) <= 0) {
                throw new SQLException("Bid amount must be higher than current price");
            }
            
            // Check if bidder is not the seller
            if (item.getSellerId() == bidderId) {
                throw new SQLException("You cannot bid on your own item");
            }
            
            // Create and insert the bid
            Bid bid = new Bid(itemId, bidderId, bidAmount);
            int bidId = bidDAO.insertBid(bid);
            
            // Update the current price of the item
            itemDAO.updateCurrentPrice(itemId, bidAmount.doubleValue());
            
            // Mark previous bids as outbid
            bidDAO.updateAllBidsStatus(itemId, "outbid");
            
            // Mark this bid as active
            bidDAO.updateBidStatus(bidId, "active");
            
            LOGGER.log(Level.INFO, "Bid placed successfully: ID {0}, Amount: {1}", new Object[]{bidId, bidAmount});
            
            return bidId;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error placing bid", e);
            throw e;
        }
    }
    
    @Override
    public int placeBid(Bid bid) throws SQLException {
        return placeBid(bid.getItemId(), bid.getBidderId(), bid.getBidAmount());
    }

    @Override
    public Bid getBidById(int bidId) throws SQLException {
        try {
            return bidDAO.getBidById(bidId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting bid by ID", e);
            throw e;
        }
    }

    @Override
    public List<Bid> getBidsByItem(int itemId) throws SQLException {
        try {
            return bidDAO.getBidsByItem(itemId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting bids by item", e);
            throw e;
        }
    }

    @Override
    public List<Bid> getBidsByBidder(int bidderId) throws SQLException {
        try {
            return bidDAO.getBidsByBidder(bidderId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting bids by bidder", e);
            throw e;
        }
    }

    @Override
    public Bid getHighestBidForItem(int itemId) throws SQLException {
        try {
            return bidDAO.getHighestBidForItem(itemId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting highest bid for item", e);
            throw e;
        }
    }

    @Override
    public boolean updateBidStatus(int bidId, String status) throws SQLException {
        try {
            return bidDAO.updateBidStatus(bidId, status);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating bid status", e);
            throw e;
        }
    }

    @Override
    public boolean markAsWinningBid(int bidId, int itemId) throws SQLException {
        try {
            // Mark all bids for this item as outbid
            bidDAO.updateAllBidsStatus(itemId, "outbid");
            
            // Mark this bid as winning
            boolean result = bidDAO.updateBidStatus(bidId, "winning");
            
            // Update item status to completed
            itemDAO.updateItemStatus(itemId, "completed");
            
            LOGGER.log(Level.INFO, "Bid marked as winning: ID {0}, Item: {1}", new Object[]{bidId, itemId});
            
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking bid as winning", e);
            throw e;
        }
    }

    @Override
    public boolean cancelBid(int bidId) throws SQLException {
        try {
            Bid bid = bidDAO.getBidById(bidId);
            
            if (bid == null) {
                throw new SQLException("Bid not found");
            }
            
            // Update bid status to cancelled
            boolean result = bidDAO.updateBidStatus(bidId, "cancelled");
            
            // Get highest remaining bid
            Bid highestBid = bidDAO.getHighestBidForItem(bid.getItemId());
            
            // Update item price to highest remaining bid or starting price
            if (highestBid != null) {
                itemDAO.updateCurrentPrice(bid.getItemId(), highestBid.getBidAmount().doubleValue());
            } else {
                Item item = itemDAO.getItemById(bid.getItemId());
                itemDAO.updateCurrentPrice(bid.getItemId(), item.getStartingPrice().doubleValue());
            }
            
            LOGGER.log(Level.INFO, "Bid cancelled: ID {0}", bidId);
            
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error cancelling bid", e);
            throw e;
        }
    }

    @Override
    public int countBidsForItem(int itemId) throws SQLException {
        try {
            return bidDAO.countBidsForItem(itemId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error counting bids for item", e);
            throw e;
        }
    }

    @Override
    public int getTotalBidCount() throws SQLException {
        try {
            return bidDAO.getTotalBidCount();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total bid count", e);
            throw e;
        }
    }

    @Override
    public int getNewBidCount(int days) throws SQLException {
        try {
            return bidDAO.getNewBidCount(days);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting new bid count for last " + days + " days", e);
            throw e;
        }
    }

    @Override
    public List<Bid> getRecentBidsForSeller(int sellerId, int limit) throws SQLException {
        try {
            return bidDAO.getRecentBidsForSeller(sellerId, limit);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent bids for seller: " + sellerId, e);
            throw e;
        }
    }

    @Override
    public int getTotalBidsCountForSeller(int sellerId) throws SQLException {
        try {
            return bidDAO.getTotalBidsCountForSeller(sellerId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total bids count for seller: " + sellerId, e);
            throw e;
        }
    }
    
    @Override
    public boolean updatePreviousHighestBid(int itemId, int newBidId) throws SQLException {
        try {
            return bidDAO.updatePreviousHighestBid(itemId, newBidId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating previous highest bid", e);
            throw e;
        }
    }
    @Override
    public boolean updateBid(Bid bid) throws SQLException {
        try {
            // Get the item to check if bidding is allowed
            Item item = itemDAO.getItemById(bid.getItemId());
            
            if (item == null) {
                throw new SQLException("Item not found");
            }
            
            // Check if item is active
            if (!"active".equals(item.getStatus())) {
                throw new SQLException("Bidding is not allowed on this item");
            }
            
            // Update the bid
            boolean result = bidDAO.updateBid(bid);
            
            if (result) {
                // Update the current price of the item
                itemDAO.updateCurrentPrice(bid.getItemId(), bid.getBidAmount().doubleValue());
                
                LOGGER.log(Level.INFO, "Bid updated successfully: ID {0}, Amount: {1}", 
                        new Object[]{bid.getBidId(), bid.getBidAmount()});
            }
            
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating bid", e);
            throw e;
        }
    }

}
