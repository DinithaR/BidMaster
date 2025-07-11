package com.bidmaster.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class representing a report
 */
public class Report {
    private int reportId;
    private String title;
    private String description;
    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    
    // For display purposes
    private String createdByUsername;
    
    /**
     * Default constructor
     */
    public Report() {
    }
    
    /**
     * Constructor with essential fields
     * 
     * @param title The report title
     * @param reportType The report type
     * @param createdBy The ID of the user who created the report
     */
    public Report(String title, String reportType, int createdBy) {
        this.title = title;
        this.reportType = reportType;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.status = "active";
    }
    
    /**
     * Constructor with all fields
     * 
     * @param reportId The report ID
     * @param title The report title
     * @param description The report description
     * @param reportType The report type
     * @param startDate The start date for report data
     * @param endDate The end date for report data
     * @param createdBy The ID of the user who created the report
     * @param createdAt The creation date and time
     * @param updatedAt The last update date and time
     * @param status The report status
     */
    public Report(int reportId, String title, String description, String reportType, LocalDate startDate, 
                 LocalDate endDate, int createdBy, LocalDateTime createdAt, LocalDateTime updatedAt, String status) {
        this.reportId = reportId;
        this.title = title;
        this.description = description;
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
    
    // Getters and Setters
    
    public int getReportId() {
        return reportId;
    }
    
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getReportType() {
        return reportType;
    }
    
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatedByUsername() {
        return createdByUsername;
    }
    
    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }
    
    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", title='" + title + '\'' +
                ", reportType='" + reportType + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}
