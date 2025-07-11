package com.bidmaster.model;

import java.time.LocalDateTime;

/**
 * Model class representing a watchlist item
 */
public class WatchlistItem {
    private int watchlistId;
    private int userId;
    private int itemId;
    private LocalDateTime addedDate;
    
    // Additional fields for display purposes
    private String itemTitle;
    private String itemImageUrl;
    private double currentPrice;
    private LocalDateTime endTime;
    private String status;
    private String sellerUsername;
    
    /**
     * Default constructor
     */
    public WatchlistItem() {
    }
    
    /**
     * Constructor with userId and itemId
     * 
     * @param userId The user ID
     * @param itemId The item ID
     */
    public WatchlistItem(int userId, int itemId) {
        this.userId = userId;
        this.itemId = itemId;
        this.addedDate = LocalDateTime.now();
    }
    
    /**
     * Constructor with all fields
     * 
     * @param watchlistId The watchlist ID
     * @param userId The user ID
     * @param itemId The item ID
     * @param addedDate The date the item was added to the watchlist
     */
    public WatchlistItem(int watchlistId, int userId, int itemId, LocalDateTime addedDate) {
        this.watchlistId = watchlistId;
        this.userId = userId;
        this.itemId = itemId;
        this.addedDate = addedDate;
    }
    
    // Getters and Setters
    
    public int getWatchlistId() {
        return watchlistId;
    }
    
    public void setWatchlistId(int watchlistId) {
        this.watchlistId = watchlistId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public LocalDateTime getAddedDate() {
        return addedDate;
    }
    
    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }
    
    public String getItemTitle() {
        return itemTitle;
    }
    
    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    
    public String getItemImageUrl() {
        return itemImageUrl;
    }
    
    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSellerUsername() {
        return sellerUsername;
    }
    
    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
    
    @Override
    public String toString() {
        return "WatchlistItem{" +
                "watchlistId=" + watchlistId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", addedDate=" + addedDate +
                '}';
    }
}
