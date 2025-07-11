package com.bidmaster.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.bidmaster.model.Report;

/**
 * Data Access Object interface for Report
 */
public interface ReportDAO {
    /**
     * Inserts a new report
     * 
     * @param report The report to insert
     * @return The ID of the inserted report
     * @throws SQLException if a database error occurs
     */
    int insertReport(Report report) throws SQLException;
    
    /**
     * Gets a report by its ID
     * 
     * @param reportId The report ID
     * @return The report, or null if not found
     * @throws SQLException if a database error occurs
     */
    Report getReportById(int reportId) throws SQLException;
    
    /**
     * Gets all reports
     * 
     * @return List of all reports
     * @throws SQLException if a database error occurs
     */
    List<Report> getAllReports() throws SQLException;
    
    /**
     * Gets reports by type
     * 
     * @param reportType The report type
     * @return List of reports of the specified type
     * @throws SQLException if a database error occurs
     */
    List<Report> getReportsByType(String reportType) throws SQLException;
    
    /**
     * Gets reports created by a specific user
     * 
     * @param userId The user ID
     * @return List of reports created by the user
     * @throws SQLException if a database error occurs
     */
    List<Report> getReportsByCreator(int userId) throws SQLException;
    
    /**
     * Gets reports created within a date range
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of reports created within the date range
     * @throws SQLException if a database error occurs
     */
    List<Report> getReportsInDateRange(LocalDate startDate, LocalDate endDate) throws SQLException;
    
    /**
     * Updates a report
     * 
     * @param report The report to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateReport(Report report) throws SQLException;
    
    /**
     * Deletes a report
     * 
     * @param reportId The report ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean deleteReport(int reportId) throws SQLException;
}
