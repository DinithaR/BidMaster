package com.bidmaster.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bidmaster.dao.ReportDAO;
import com.bidmaster.dao.UserDAO;
import com.bidmaster.dao.ItemDAO;
import com.bidmaster.dao.TransactionDAO;
import com.bidmaster.dao.BidDAO;
import com.bidmaster.dao.impl.ReportDAOImpl;
import com.bidmaster.dao.impl.UserDAOImpl;
import com.bidmaster.dao.impl.ItemDAOImpl;
import com.bidmaster.dao.impl.TransactionDAOImpl;
import com.bidmaster.dao.impl.BidDAOImpl;
import com.bidmaster.model.Report;
import com.bidmaster.model.User;
import com.bidmaster.model.Item;
import com.bidmaster.model.Transaction;
import com.bidmaster.model.Bid;
import com.bidmaster.service.ReportService;

/**
 * Implementation of ReportService interface
 */
public class ReportServiceImpl implements ReportService {
    private static final Logger LOGGER = Logger.getLogger(ReportServiceImpl.class.getName());
    
    private ReportDAO reportDAO;
    private UserDAO userDAO;
    private ItemDAO itemDAO;
    private TransactionDAO transactionDAO;
    private BidDAO bidDAO;
    
    public ReportServiceImpl() {
        this.reportDAO = new ReportDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.itemDAO = new ItemDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
        this.bidDAO = new BidDAOImpl();
    }

    @Override
    public int createReport(Report report) throws SQLException {
        try {
            return reportDAO.insertReport(report);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating report", e);
            throw e;
        }
    }

    @Override
    public Report getReportById(int reportId) throws SQLException {
        try {
            Report report = reportDAO.getReportById(reportId);
            
            if (report != null) {
                // Get creator username
                User creator = userDAO.getUserById(report.getCreatedBy());
                if (creator != null) {
                    report.setCreatedByUsername(creator.getUsername());
                }
            }
            
            return report;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting report by ID", e);
            throw e;
        }
    }

    @Override
    public List<Report> getAllReports() throws SQLException {
        try {
            List<Report> reports = reportDAO.getAllReports();
            
            // Get creator usernames
            for (Report report : reports) {
                User creator = userDAO.getUserById(report.getCreatedBy());
                if (creator != null) {
                    report.setCreatedByUsername(creator.getUsername());
                }
            }
            
            return reports;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all reports", e);
            throw e;
        }
    }

    @Override
    public List<Report> getReportsByType(String reportType) throws SQLException {
        try {
            List<Report> reports = reportDAO.getReportsByType(reportType);
            
            // Get creator usernames
            for (Report report : reports) {
                User creator = userDAO.getUserById(report.getCreatedBy());
                if (creator != null) {
                    report.setCreatedByUsername(creator.getUsername());
                }
            }
            
            return reports;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting reports by type", e);
            throw e;
        }
    }

    @Override
    public boolean updateReport(Report report) throws SQLException {
        try {
            return reportDAO.updateReport(report);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating report", e);
            throw e;
        }
    }

    @Override
    public boolean deleteReport(int reportId) throws SQLException {
        try {
            return reportDAO.deleteReport(reportId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting report", e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> getReportData(Report report) throws SQLException {
        try {
            Map<String, Object> data = new HashMap<>();
            
            // Get date range
            LocalDate startDate = report.getStartDate();
            LocalDate endDate = report.getEndDate();
            
            // Add report metadata
            data.put("reportId", report.getReportId());
            data.put("title", report.getTitle());
            data.put("description", report.getDescription());
            data.put("reportType", report.getReportType());
            data.put("startDate", startDate);
            data.put("endDate", endDate);
            data.put("createdAt", report.getCreatedAt());
            
            // Get report data based on type
            switch (report.getReportType()) {
                case "sales":
                    data.putAll(getSalesReportData(startDate, endDate));
                    break;
                case "users":
                    data.putAll(getUsersReportData(startDate, endDate));
                    break;
                case "items":
                    data.putAll(getItemsReportData(startDate, endDate));
                    break;
                case "bids":
                    data.putAll(getBidsReportData(startDate, endDate));
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Unknown report type: {0}", report.getReportType());
                    break;
            }
            
            return data;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting report data", e);
            throw e;
        }
    }

    @Override
    public byte[] generateReportFile(Report report, String format) throws SQLException {
        try {
            // Get report data
            Map<String, Object> data = getReportData(report);
            
            // Generate report file based on format
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            switch (format.toLowerCase()) {
                case "pdf":
                    generatePdfReport(data, outputStream);
                    break;
                case "csv":
                    generateCsvReport(data, outputStream);
                    break;
                case "xlsx":
                    generateExcelReport(data, outputStream);
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Unsupported report format: {0}", format);
                    throw new IllegalArgumentException("Unsupported report format: " + format);
            }
            
            return outputStream.toByteArray();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error generating report file", e);
            throw e;
        }
    }

    @Override
    public Map<String, Double> getMonthlyRevenue() throws SQLException {
        try {
            Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
            
            // Get current date and 6 months back
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(5);
            
            // Format for month names
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
            
            // Initialize map with all months (including those with zero revenue)
            LocalDate currentMonth = startDate;
            while (!currentMonth.isAfter(endDate)) {
                monthlyRevenue.put(currentMonth.format(monthFormatter), 0.0);
                currentMonth = currentMonth.plusMonths(1);
            }
            
            // Get actual revenue data
            Map<String, Double> actualRevenue = transactionDAO.getMonthlyRevenue(startDate, endDate);
            
            // Merge actual data into the initialized map
            for (Map.Entry<String, Double> entry : actualRevenue.entrySet()) {
                monthlyRevenue.put(entry.getKey(), entry.getValue());
            }
            
            return monthlyRevenue;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting monthly revenue", e);
            throw e;
        }
    }
    
    /**
     * Gets data for a sales report
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return Map containing sales report data
     * @throws SQLException if a database error occurs
     */
    private Map<String, Object> getSalesReportData(LocalDate startDate, LocalDate endDate) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        
        // Get transactions in date range
        List<Transaction> transactions = transactionDAO.getTransactionsInDateRange(startDate, endDate);
        data.put("transactions", transactions);
        
        // Calculate total revenue
        double totalRevenue = 0.0;
        for (Transaction transaction : transactions) {
            if ("completed".equals(transaction.getStatus())) {
                totalRevenue += transaction.getAmount().doubleValue();
            }
        }
        data.put("totalRevenue", totalRevenue);
        
        // Get transaction counts by status
        Map<String, Integer> transactionsByStatus = new HashMap<>();
        for (Transaction transaction : transactions) {
            String status = transaction.getStatus();
            transactionsByStatus.put(status, transactionsByStatus.getOrDefault(status, 0) + 1);
        }
        data.put("transactionsByStatus", transactionsByStatus);
        
        // Get revenue by month
        Map<String, Double> revenueByMonth = transactionDAO.getMonthlyRevenue(startDate, endDate);
        data.put("revenueByMonth", revenueByMonth);
        
        return data;
    }
    
    /**
     * Gets data for a users report
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return Map containing users report data
     * @throws SQLException if a database error occurs
     */
    private Map<String, Object> getUsersReportData(LocalDate startDate, LocalDate endDate) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        
        // Get users registered in date range
        List<User> users = userDAO.getUsersRegisteredInDateRange(startDate, endDate);
        data.put("users", users);
        
        // Get user counts by role
        Map<String, Integer> usersByRole = new HashMap<>();
        for (User user : users) {
            String role = user.getRole();
            usersByRole.put(role, usersByRole.getOrDefault(role, 0) + 1);
        }
        data.put("usersByRole", usersByRole);
        
        // Get user registrations by month
        Map<String, Integer> registrationsByMonth = userDAO.getMonthlyRegistrations(startDate, endDate);
        data.put("registrationsByMonth", registrationsByMonth);
        
        // Get active users (users who placed bids or listed items)
        List<User> activeUsers = userDAO.getActiveUsers(startDate, endDate);
        data.put("activeUsers", activeUsers);
        data.put("activeUserCount", activeUsers.size());
        
        return data;
    }
    
    /**
     * Gets data for an items report
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return Map containing items report data
     * @throws SQLException if a database error occurs
     */
    private Map<String, Object> getItemsReportData(LocalDate startDate, LocalDate endDate) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        
        // Get items created in date range
        List<Item> items = itemDAO.getItemsCreatedInDateRange(startDate, endDate);
        data.put("items", items);
        
        // Get item counts by status
        Map<String, Integer> itemsByStatus = new HashMap<>();
        for (Item item : items) {
            String status = item.getStatus();
            itemsByStatus.put(status, itemsByStatus.getOrDefault(status, 0) + 1);
        }
        data.put("itemsByStatus", itemsByStatus);
        
        // Get item counts by category
        Map<Integer, Integer> itemsByCategory = new HashMap<>();
        for (Item item : items) {
            int categoryId = item.getCategoryId();
            itemsByCategory.put(categoryId, itemsByCategory.getOrDefault(categoryId, 0) + 1);
        }
        data.put("itemsByCategory", itemsByCategory);
        
        // Get items with most bids
        List<Item> popularItems = itemDAO.getItemsWithMostBids(startDate, endDate, 10);
        data.put("popularItems", popularItems);
        
        return data;
    }
    
    /**
     * Gets data for a bids report
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return Map containing bids report data
     * @throws SQLException if a database error occurs
     */
    private Map<String, Object> getBidsReportData(LocalDate startDate, LocalDate endDate) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        
        // Get bids placed in date range
        List<Bid> bids = bidDAO.getBidsInDateRange(startDate, endDate);
        data.put("bids", bids);
        
        // Get bid counts by status
        Map<String, Integer> bidsByStatus = new HashMap<>();
        for (Bid bid : bids) {
            String status = bid.getStatus();
            bidsByStatus.put(status, bidsByStatus.getOrDefault(status, 0) + 1);
        }
        data.put("bidsByStatus", bidsByStatus);
        
        // Get bids by day
        Map<String, Integer> bidsByDay = bidDAO.getDailyBidCounts(startDate, endDate);
        data.put("bidsByDay", bidsByDay);
        
        // Get top bidders
        List<User> topBidders = userDAO.getTopBidders(startDate, endDate, 10);
        data.put("topBidders", topBidders);
        
        return data;
    }
    
    /**
     * Generates a PDF report
     * 
     * @param data The report data
     * @param outputStream The output stream to write to
     */
    private void generatePdfReport(Map<String, Object> data, ByteArrayOutputStream outputStream) {
        // This would use a PDF library like iText or Apache PDFBox
        // For simplicity, we'll just write a placeholder message
        try {
            String message = "PDF report for: " + data.get("title") + "\n";
            message += "Generated on: " + LocalDate.now() + "\n";
            message += "This is a placeholder for actual PDF generation.";
            
            outputStream.write(message.getBytes());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating PDF report", e);
        }
    }
    
    /**
     * Generates a CSV report
     * 
     * @param data The report data
     * @param outputStream The output stream to write to
     */
    private void generateCsvReport(Map<String, Object> data, ByteArrayOutputStream outputStream) {
        // This would use a CSV library like Apache Commons CSV
        // For simplicity, we'll just write a placeholder message
        try {
            String message = "CSV report for: " + data.get("title") + "\n";
            message += "Generated on: " + LocalDate.now() + "\n";
            message += "This is a placeholder for actual CSV generation.";
            
            outputStream.write(message.getBytes());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating CSV report", e);
        }
    }
    
    /**
     * Generates an Excel report
     * 
     * @param data The report data
     * @param outputStream The output stream to write to
     */
    private void generateExcelReport(Map<String, Object> data, ByteArrayOutputStream outputStream) {
        // This would use an Excel library like Apache POI
        // For simplicity, we'll just write a placeholder message
        try {
            String message = "Excel report for: " + data.get("title") + "\n";
            message += "Generated on: " + LocalDate.now() + "\n";
            message += "This is a placeholder for actual Excel generation.";
            
            outputStream.write(message.getBytes());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating Excel report", e);
        }
    }
}
