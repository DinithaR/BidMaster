package com.bidmaster.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.bidmaster.model.Bid;

public interface BidDAO {
    /**
     * Inserts a new bid
     *
     * @param bid The bid to insert
     * @return The ID of the inserted bid
     * @throws SQLException if a database error occurs
     */
    int insertBid(Bid bid) throws SQLException;

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
     *
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
     * Updates all bids' status for an item
     *
     * @param itemId The item ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateAllBidsStatus(int itemId, String status) throws SQLException;

    /**
     * Deletes a bid
     *
     * @param bidId The bid ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean deleteBid(int bidId) throws SQLException;

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
     * Gets bids placed within a date range
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return List of bids placed within the date range
     * @throws SQLException if a database error occurs
     */
    List<Bid> getBidsInDateRange(LocalDate startDate, LocalDate endDate) throws SQLException;

    /**
     * Gets daily bid counts
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return Map with days as keys and bid counts as values
     * @throws SQLException if a database error occurs
     */
    Map<String, Integer> getDailyBidCounts(LocalDate startDate, LocalDate endDate) throws SQLException;

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
