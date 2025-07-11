package com.bidmaster.model;

import java.sql.Timestamp;

public class Watchlist {
    private int watchlistId;
    private int userId;
    private int itemId;
    private Timestamp addedDate;
    
    // Additional fields for display purposes
    private String itemTitle;
    private String itemImageUrl;
    
    // Default constructor
    public Watchlist() {
    }
    
    // Parameterized constructor
    public Watchlist(int userId, int itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }
    
    // Constructor with watchlistId
    public Watchlist(int watchlistId, int userId, int itemId, Timestamp addedDate) {
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
    
    public Timestamp getAddedDate() {
        return addedDate;
    }
    
    public void setAddedDate(Timestamp addedDate) {
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
}
