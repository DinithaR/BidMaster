package com.bidmaster.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid {
    private int bidId;
    private int itemId;
    private int bidderId;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
    private String status;
    
    // Additional fields for display purposes
    private String bidderUsername;
    private String itemTitle;
    private String itemImageUrl;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private String itemStatus;
    private String sellerUsername;
    
    // Default constructor
    public Bid() {
    }
    
    // Parameterized constructor
    public Bid(int itemId, int bidderId, BigDecimal bidAmount) {
        this.itemId = itemId;
        this.bidderId = bidderId;
        this.bidAmount = bidAmount;
        this.status = "active";
    }
    
    // Constructor with bidId
    public Bid(int bidId, int itemId, int bidderId, BigDecimal bidAmount, LocalDateTime bidTime, String status) {
        this.bidId = bidId;
        this.itemId = itemId;
        this.bidderId = bidderId;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime;
        this.status = status;
    }
    
    // Getters and Setters
    public int getBidId() {
        return bidId;
    }
    
    public void setBidId(int bidId) {
        this.bidId = bidId;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public int getBidderId() {
        return bidderId;
    }
    
    public void setBidderId(int bidderId) {
        this.bidderId = bidderId;
    }
    
    public BigDecimal getBidAmount() {
        return bidAmount;
    }
    
    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }
    
    public LocalDateTime getBidTime() {
        return bidTime;
    }
    
    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getBidderUsername() {
        return bidderUsername;
    }
    
    public void setBidderUsername(String bidderUsername) {
        this.bidderUsername = bidderUsername;
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
    
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getItemStatus() {
        return itemStatus;
    }
    
    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
    
    public String getSellerUsername() {
        return sellerUsername;
    }
    
    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
    
    @Override
    public String toString() {
        return "Bid{" +
                "bidId=" + bidId +
                ", itemId=" + itemId +
                ", bidderId=" + bidderId +
                ", bidAmount=" + bidAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
