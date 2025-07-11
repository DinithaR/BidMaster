package com.bidmaster.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.bidmaster.model.Bid;

/**
 * Service interface for Bid operations
 */
public interface BidService {
    /**
     * Places a bid on an item
     *
     * @param itemId The item ID
     * @param bidderId The bidder ID
     * @param bidAmount The bid amount
     * @return The ID of the created bid
     * @throws SQLException if a database error occurs
     */
    int placeBid(int itemId, int bidderId, BigDecimal bidAmount) throws SQLException;
    
    /**
     * Places a bid
     * 
     * @param bid The bid to place
     * @return The ID of the created bid
     * @throws SQLException if a database error occurs
     */
    int placeBid(Bid bid) throws SQLException;

    /**
     * Gets a bid by its ID
     *
     * @param bidId The bid ID
     * @return The bid, or null if not found
     * @throws SQLException if a database error occurs
     */
    Bid getBidById(int bidId) throws SQLException;

    /**
     * Gets all bids for an item
     *
     * @param itemId The item ID
     * @return List of bids for the item
     * @throws SQLException if a database error occurs
     */
    List<Bid> getBidsByItem(int itemId) throws SQLException;

    /**
     * Gets all bids placed by a bidder
     * @param bidderId The bidder ID
     * @return List of bids placed by the bidder
     * @throws SQLException if a database error occurs
     */
    List<Bid> getBidsByBidder(int bidderId) throws SQLException;

    /**
     * Gets the highest bid for an item
     *
     * @param itemId The item ID
     * @return The highest bid, or null if no bids
     * @throws SQLException if a database error occurs
     */
    Bid getHighestBidForItem(int itemId) throws SQLException;

    /**
     * Updates a bid's status
     *
     * @param bidId The bid ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateBidStatus(int bidId, String status) throws SQLException;

    /**
     * Marks a bid as the winning bid for an item
     *
     * @param bidId The bid ID
     * @param itemId The item ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean markAsWinningBid(int bidId, int itemId) throws SQLException;

    /**
     * Cancels a bid
     * @param bidId The bid ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean cancelBid(int bidId) throws SQLException;

    /**
     * Counts the number of bids for an item
     *
     * @param itemId The item ID
     * @return The number of bids
     * @throws SQLException if a database error occurs
     */
    int countBidsForItem(int itemId) throws SQLException;

    /**
     * Gets the total count of bids in the system
     *
     * @return The total number of bids
     * @throws SQLException if a database error occurs
     */
    int getTotalBidCount() throws SQLException;

    /**
     * Gets the count of new bids placed in the last specified number of days
     *
     * @param days The number of days to look back
     * @return The count of new bids
     * @throws SQLException if a database error occurs
     */
    int getNewBidCount(int days) throws SQLException;

    /**
     * Gets recent bids for items from a specific seller
     *
     * @param sellerId The seller ID
     * @param limit The maximum number of bids to return
     * @return List of recent bids on the seller's items
     * @throws SQLException if a database error occurs
     */
    List<Bid> getRecentBidsForSeller(int sellerId, int limit) throws SQLException;

    /**
     * Gets the total count of bids received on a seller's items
     *
     * @param sellerId The seller ID
     * @return The total number of bids
     * @throws SQLException if a database error occurs
     */
    int getTotalBidsCountForSeller(int sellerId) throws SQLException;
    
    /**
     * Updates previous highest bid status to 'outbid'
     *
     * @param itemId The item ID
     * @param newBidId The ID of the new highest bid
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updatePreviousHighestBid(int itemId, int newBidId) throws SQLException;
    /**
     * Updates a bid
     * 
     * @param bid The bid to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateBid(Bid bid) throws SQLException;

}
