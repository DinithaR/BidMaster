package com.bidmaster.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.ReportDAO;
import com.bidmaster.model.Report;
import com.bidmaster.util.DBConnectionUtil;

/**
 * Implementation of ReportDAO interface
 */
public class ReportDAOImpl implements ReportDAO {
    private static final Logger LOGGER = Logger.getLogger(ReportDAOImpl.class.getName());
    
    private static final String INSERT_REPORT = 
        "INSERT INTO Reports (title, description, reportType, startDate, endDate, createdBy, createdAt, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String GET_REPORT_BY_ID = 
        "SELECT * FROM Reports WHERE reportId = ?";
    
    private static final String GET_ALL_REPORTS = 
        "SELECT * FROM Reports ORDER BY createdAt DESC";
    
    private static final String GET_REPORTS_BY_TYPE = 
        "SELECT * FROM Reports WHERE reportType = ? ORDER BY createdAt DESC";
    
    private static final String GET_REPORTS_BY_CREATOR = 
        "SELECT * FROM Reports WHERE createdBy = ? ORDER BY createdAt DESC";
    
    private static final String GET_REPORTS_IN_DATE_RANGE = 
        "SELECT * FROM Reports WHERE createdAt BETWEEN ? AND ? ORDER BY createdAt DESC";
    
    private static final String UPDATE_REPORT = 
        "UPDATE Reports SET title = ?, description = ?, reportType = ?, startDate = ?, endDate = ?, " +
        "updatedAt = ?, status = ? WHERE reportId = ?";
    
    private static final String DELETE_REPORT = 
        "DELETE FROM Reports WHERE reportId = ?";

    @Override
    public int insertReport(Report report) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REPORT, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, report.getTitle());
            preparedStatement.setString(2, report.getDescription());
            preparedStatement.setString(3, report.getReportType());
            
            if (report.getStartDate() != null) {
                preparedStatement.setDate(4, Date.valueOf(report.getStartDate()));
            } else {
                preparedStatement.setNull(4, java.sql.Types.DATE);
            }
            
            if (report.getEndDate() != null) {
                preparedStatement.setDate(5, Date.valueOf(report.getEndDate()));
            } else {
                preparedStatement.setNull(5, java.sql.Types.DATE);
            }
            
            preparedStatement.setInt(6, report.getCreatedBy());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(report.getCreatedAt()));
            preparedStatement.setString(8, report.getStatus() != null ? report.getStatus() : "active");
            
            int affectedRows = preparedStatement.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating report failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int reportId = generatedKeys.getInt(1);
                    report.setReportId(reportId);
                    LOGGER.log(Level.INFO, "Report created successfully: {0}, ID: {1}", 
                            new Object[]{report.getTitle(), reportId});
                    return reportId;
                } else {
                    throw new SQLException("Creating report failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting report", e);
            throw e;
        }
    }

    @Override
    public Report getReportById(int reportId) throws SQLException {
        Report report = null;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_REPORT_BY_ID)) {
            
            preparedStatement.setInt(1, reportId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    report = extractReportFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting report by ID: " + reportId, e);
            throw e;
        }
        return report;
    }

    @Override
    public List<Report> getAllReports() throws SQLException {
        List<Report> reports = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_REPORTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Report report = extractReportFromResultSet(resultSet);
                reports.add(report);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all reports", e);
            throw e;
        }
        return reports;
    }

    @Override
    public List<Report> getReportsByType(String reportType) throws SQLException {
        List<Report> reports = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_REPORTS_BY_TYPE)) {
            
            preparedStatement.setString(1, reportType);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Report report = extractReportFromResultSet(resultSet);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting reports by type: " + reportType, e);
            throw e;
        }
        return reports;
    }

    @Override
    public List<Report> getReportsByCreator(int userId) throws SQLException {
        List<Report> reports = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_REPORTS_BY_CREATOR)) {
            
            preparedStatement.setInt(1, userId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Report report = extractReportFromResultSet(resultSet);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting reports by creator: " + userId, e);
            throw e;
        }
        return reports;
    }

    @Override
    public List<Report> getReportsInDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Report> reports = new ArrayList<>();
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_REPORTS_IN_DATE_RANGE)) {
            
            preparedStatement.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(endDate.plusDays(1).atStartOfDay()));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Report report = extractReportFromResultSet(resultSet);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting reports in date range", e);
            throw e;
        }
        return reports;
    }

    @Override
    public boolean updateReport(Report report) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_REPORT)) {
            
            preparedStatement.setString(1, report.getTitle());
            preparedStatement.setString(2, report.getDescription());
            preparedStatement.setString(3, report.getReportType());
            
            if (report.getStartDate() != null) {
                preparedStatement.setDate(4, Date.valueOf(report.getStartDate()));
            } else {
                preparedStatement.setNull(4, java.sql.Types.DATE);
            }
            
            if (report.getEndDate() != null) {
                preparedStatement.setDate(5, Date.valueOf(report.getEndDate()));
            } else {
                preparedStatement.setNull(5, java.sql.Types.DATE);
            }
            
            LocalDateTime updatedAt = report.getUpdatedAt() != null ? report.getUpdatedAt() : LocalDateTime.now();
            preparedStatement.setTimestamp(6, Timestamp.valueOf(updatedAt));
            preparedStatement.setString(7, report.getStatus());
            preparedStatement.setInt(8, report.getReportId());
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Report updated: {0}, Rows affected: {1}", 
                    new Object[]{report.getTitle(), rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating report: " + report.getReportId(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteReport(int reportId) throws SQLException {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_REPORT)) {
            
            preparedStatement.setInt(1, reportId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            LOGGER.log(Level.INFO, "Report deleted: ID {0}, Rows affected: {1}", 
                    new Object[]{reportId, rowsAffected});
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting report: " + reportId, e);
            throw e;
        }
    }
    
    /**
     * Extracts a Report object from a ResultSet
     * 
     * @param resultSet The ResultSet containing report data
     * @return The extracted Report object
     * @throws SQLException if a database error occurs
     */
    private Report extractReportFromResultSet(ResultSet resultSet) throws SQLException {
        Report report = new Report();
        report.setReportId(resultSet.getInt("reportId"));
        report.setTitle(resultSet.getString("title"));
        report.setDescription(resultSet.getString("description"));
        report.setReportType(resultSet.getString("reportType"));
        
        Date startDate = resultSet.getDate("startDate");
        if (startDate != null) {
            report.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = resultSet.getDate("endDate");
        if (endDate != null) {
            report.setEndDate(endDate.toLocalDate());
        }
        
        report.setCreatedBy(resultSet.getInt("createdBy"));
        
        Timestamp createdAt = resultSet.getTimestamp("createdAt");
        if (createdAt != null) {
            report.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = resultSet.getTimestamp("updatedAt");
        if (updatedAt != null) {
            report.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        report.setStatus(resultSet.getString("status"));
        
        return report;
    }
}
