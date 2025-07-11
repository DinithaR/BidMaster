package com.bidmaster.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.bidmaster.model.Report;

/**
 * Service interface for Report operations
 */
public interface ReportService {
    /**
     * Creates a new report
     * 
     * @param report The report to create
     * @return The ID of the created report
     * @throws SQLException if a database error occurs
     */
    int createReport(Report report) throws SQLException;
    
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
    
    /**
     * Gets data for a report
     * 
     * @param report The report
     * @return Map containing report data
     * @throws SQLException if a database error occurs
     */
    Map<String, Object> getReportData(Report report) throws SQLException;
    
    /**
     * Generates a report file
     * 
     * @param report The report
     * @param format The file format (pdf, csv, xlsx)
     * @return The report file as a byte array
     * @throws SQLException if a database error occurs
     */
    byte[] generateReportFile(Report report, String format) throws SQLException;
    
    /**
     * Gets monthly revenue data
     * 
     * @return Map with months as keys and revenue as values
     * @throws SQLException if a database error occurs
     */
    Map<String, Double> getMonthlyRevenue() throws SQLException;
}
