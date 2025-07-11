package com.bidmaster.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.bidmaster.model.Category;
import com.bidmaster.model.Item;

public interface ItemDAO {
    /**
     * Inserts a new item
     *
     * @param item The item to insert
     * @return The ID of the inserted item
     * @throws SQLException if a database error occurs
     */
    int insertItem(Item item) throws SQLException;

    /**
     * Gets an item by its ID
     *
     * @param itemId The item ID
     * @return The item, or null if not found
     * @throws SQLException if a database error occurs
     */
    Item getItemById(int itemId) throws SQLException;

    /**
     * Gets all items
     *
     * @return List of all items
     * @throws SQLException if a database error occurs
     */
    List<Item> getAllItems() throws SQLException;

    /**
     * Gets items by category
     *
     * @param categoryId The category ID
     * @return List of items in the specified category
     * @throws SQLException if a database error occurs
     */
    List<Item> getItemsByCategory(int categoryId) throws SQLException;

    /**
     * Gets items by seller
     *
     * @param sellerId The seller ID
     * @return List of items from the specified seller
     * @throws SQLException if a database error occurs
     */
    List<Item> getItemsBySeller(int sellerId) throws SQLException;

    /**
     * Searches for items by title or description
     *
     * @param searchTerm The search term
     * @return List of matching items
     * @throws SQLException if a database error occurs
     */
    List<Item> searchItems(String searchTerm) throws SQLException;

    /**
     * Gets active items (auctions in progress)
     *
     * @return List of active items
     * @throws SQLException if a database error occurs
     */
    List<Item> getActiveItems() throws SQLException;

    /**
     * Gets items ending soon
     *
     * @param hours The number of hours to look ahead
     * @return List of items ending within the specified hours
     * @throws SQLException if a database error occurs
     */
    List<Item> getEndingSoonItems(int hours) throws SQLException;

    /**
     * Updates an item
     *
     * @param item The item to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateItem(Item item) throws SQLException;

    /**
     * Updates an item's status
     *
     * @param itemId The item ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateItemStatus(int itemId, String status) throws SQLException;

    /**
     * Updates an item's current price
     *
     * @param itemId The item ID
     * @param newPrice The new price
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateCurrentPrice(int itemId, double newPrice) throws SQLException;

    /**
     * Deletes an item
     *
     * @param itemId The item ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean deleteItem(int itemId) throws SQLException;

    /**
     * Gets the count of active items in the system
     *
     * @return The count of active items
     * @throws SQLException if a database error occurs
     */
    int getActiveItemCount() throws SQLException;

    /**
     * Gets the count of new items created in the last specified number of days
     *
     * @param days The number of days to look back
     * @return The count of new items
     * @throws SQLException if a database error occurs
     */
    int getNewItemCount(int days) throws SQLException;

    /**
     * Gets a list of the most recently created items
     *
     * @param limit The maximum number of items to return
     * @return List of recent items
     * @throws SQLException if a database error occurs
     */
    List<Item> getRecentItems(int limit) throws SQLException;

    /**
     * Gets items created within a date range
     *
     * @param startDate The start date
     * @param endDate The end date
     * @return List of items created within the date range
     * @throws SQLException if a database error occurs
     */
    List<Item> getItemsCreatedInDateRange(LocalDate startDate, LocalDate endDate) throws SQLException;

    /**
     * Gets items with the most bids
     *
     * @param startDate The start date
     * @param endDate The end date
     * @param limit The maximum number of items to return
     * @return List of popular items
     * @throws SQLException if a database error occurs
     */
    List<Item> getItemsWithMostBids(LocalDate startDate, LocalDate endDate, int limit) throws SQLException;

    /**
     * Gets active items by seller
     *
     * @param sellerId The seller ID
     * @return List of active items from the specified seller
     * @throws SQLException if a database error occurs
     */
    List<Item> getActiveItemsBySeller(int sellerId) throws SQLException;

    /**
     * Gets the count of active items by seller
     *
     * @param sellerId The seller ID
     * @return The count of active items
     * @throws SQLException if a database error occurs
     */
    int getActiveItemCountBySeller(int sellerId) throws SQLException;
    
    /**
     * Gets featured items
     *
     * @param limit The maximum number of items to return
     * @return List of featured items
     * @throws SQLException if a database error occurs
     */
    List<Item> getFeaturedItems(int limit) throws SQLException;
    
    /**
     * Gets new items
     *
     * @param limit The maximum number of items to return
     * @return List of new items
     * @throws SQLException if a database error occurs
     */
    List<Item> getNewItems(int limit) throws SQLException;
    
    /**
     * Gets similar items
     *
     * @param itemId The item ID
     * @param limit The maximum number of items to return
     * @return List of similar items
     * @throws SQLException if a database error occurs
     */
    List<Item> getSimilarItems(int itemId, int limit) throws SQLException;
    
    /**
     * Gets all categories with item count
     *
     * @return List of categories with item count
     * @throws SQLException if a database error occurs
     */
    List<Category> getAllCategoriesWithItemCount() throws SQLException;
}
