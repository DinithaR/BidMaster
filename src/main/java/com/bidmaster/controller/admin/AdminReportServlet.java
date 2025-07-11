package com.bidmaster.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.bidmaster.model.Report;
import com.bidmaster.model.User;
import com.bidmaster.model.Item;
import com.bidmaster.model.Transaction;
import com.bidmaster.service.ReportService;
import com.bidmaster.service.UserService;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.TransactionService;
import com.bidmaster.service.impl.ReportServiceImpl;
import com.bidmaster.service.impl.UserServiceImpl;
import com.bidmaster.service.impl.ItemServiceImpl;
import com.bidmaster.service.impl.TransactionServiceImpl;

/**
 * Servlet implementation class AdminReportServlet
 * Handles report management in the admin panel
 */
@WebServlet("/admin/AdminReportServlet")
public class AdminReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminReportServlet.class.getName());
    
    private ReportService reportService;
    private UserService userService;
    private ItemService itemService;
    private TransactionService transactionService;
    
    public void init() {
        reportService = new ReportServiceImpl();
        userService = new UserServiceImpl();
        itemService = new ItemServiceImpl();
        transactionService = new TransactionServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is an admin
        if (session == null || session.getAttribute("userId") == null || 
                !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Get action parameter
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                // Default action: show reports dashboard
                showReportsDashboard(request, response);
            } else {
                switch (action) {
                    case "view":
                        viewReport(request, response);
                        break;
                    case "create":
                        showCreateReportForm(request, response);
                        break;
                    case "edit":
                        showEditReportForm(request, response);
                        break;
                    case "download":
                        downloadReport(request, response);
                        break;
                    default:
                        showReportsDashboard(request, response);
                        break;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminReportServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in and is an admin
        if (session == null || session.getAttribute("userId") == null || 
                !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Get action parameter
        String action = request.getParameter("action");
        
        try {
            if (action == null) {
                response.sendRedirect("AdminReportServlet");
                return;
            }
            
            switch (action) {
                case "create":
                    createReport(request, response);
                    break;
                case "update":
                    updateReport(request, response);
                    break;
                case "delete":
                    deleteReport(request, response);
                    break;
                case "generate":
                    generateReport(request, response);
                    break;
                default:
                    response.sendRedirect("AdminReportServlet");
                    break;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminReportServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Shows the reports dashboard
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void showReportsDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        // Get all reports
        List<Report> reports = reportService.getAllReports();
        LOGGER.info("Retrieved " + reports.size() + " reports from database");
        
        // Get statistics for dashboard
        int userCount = userService.getUserCount();
        int activeItemCount = itemService.getActiveItemCount();
        double totalRevenue = transactionService.getTotalRevenue();
        
        // Get monthly revenue data for chart
        Map<String, Double> monthlyRevenue = reportService.getMonthlyRevenue();
        
        // Set attributes for JSP
        request.setAttribute("reports", reports);
        request.setAttribute("userCount", userCount);
        request.setAttribute("activeItemCount", activeItemCount);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("monthlyRevenue", monthlyRevenue);
        
        // Forward to reports JSP
        request.getRequestDispatcher("/admin/reports.jsp").forward(request, response);
    }
    
    /**
     * Shows a specific report
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void viewReport(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int reportId = Integer.parseInt(request.getParameter("id"));
            
            // Get report
            Report report = reportService.getReportById(reportId);
            
            if (report == null) {
                request.setAttribute("errorMessage", "Report not found");
                showReportsDashboard(request, response);
                return;
            }
            
            // Get report data
            Map<String, Object> reportData = reportService.getReportData(report);
            
            // Calculate average transaction value
            double avgTransactionValue = 0;
            if (reportData.get("transactions") != null) {
                List<Transaction> transactions = (List<Transaction>) reportData.get("transactions");
                if (!transactions.isEmpty()) {
                    avgTransactionValue = (double) reportData.get("totalRevenue") / transactions.size();
                } else {
                    avgTransactionValue = 0;
                }
            }
            reportData.put("avgTransactionValue", avgTransactionValue);
            
            // Set attributes for JSP
            request.setAttribute("report", report);
            request.setAttribute("reportData", reportData);
            
            // Forward to report view JSP
            request.getRequestDispatcher("/admin/report-view.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid report ID format", e);
            request.setAttribute("errorMessage", "Invalid report ID");
            showReportsDashboard(request, response);
        }
    }
    
    /**
     * Shows the create report form
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void showCreateReportForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        // Forward to create report JSP
        request.getRequestDispatcher("/admin/report-create.jsp").forward(request, response);
    }
    
    /**
     * Shows the edit report form
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void showEditReportForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int reportId = Integer.parseInt(request.getParameter("id"));
            
            // Get report
            Report report = reportService.getReportById(reportId);
            
            if (report == null) {
                request.setAttribute("errorMessage", "Report not found");
                showReportsDashboard(request, response);
                return;
            }
            
            // Set attributes for JSP
            request.setAttribute("report", report);
            
            // Forward to edit report JSP
            request.getRequestDispatcher("/admin/report-edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid report ID format", e);
            request.setAttribute("errorMessage", "Invalid report ID");
            showReportsDashboard(request, response);
        }
    }
    
    /**
     * Downloads a report
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void downloadReport(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int reportId = Integer.parseInt(request.getParameter("id"));
            String format = request.getParameter("format");
            
            if (format == null) {
                format = "pdf"; // Default format
            }
            
            // Get report
            Report report = reportService.getReportById(reportId);
            
            if (report == null) {
                request.setAttribute("errorMessage", "Report not found");
                showReportsDashboard(request, response);
                return;
            }
            
            // Generate report file
            byte[] reportFile = reportService.generateReportFile(report, format);
            
            // Set response headers
            response.setContentType(getContentType(format));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + report.getTitle().replaceAll("\\s+", "_") + "." + format + "\"");
            response.setContentLength(reportFile.length);
            
            // Write file to response
            response.getOutputStream().write(reportFile);
            response.getOutputStream().flush();
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid report ID format", e);
            request.setAttribute("errorMessage", "Invalid report ID");
            showReportsDashboard(request, response);
        }
    }
    
    /**
     * Creates a new report
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void createReport(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        // Get form parameters
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String reportType = request.getParameter("reportType");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        // Validate input
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Report title is required");
            showCreateReportForm(request, response);
            return;
        }
        
        if (reportType == null || reportType.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Report type is required");
            showCreateReportForm(request, response);
            return;
        }
        
        // Parse dates
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        if (startDateStr != null && !startDateStr.trim().isEmpty()) {
            startDate = LocalDate.parse(startDateStr);
        }
        
        if (endDateStr != null && !endDateStr.trim().isEmpty()) {
            endDate = LocalDate.parse(endDateStr);
        }
        
        // Create report object
        Report report = new Report();
        report.setTitle(title);
        report.setDescription(description);
        report.setReportType(reportType);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setCreatedBy((int) request.getSession().getAttribute("userId"));
        report.setCreatedAt(LocalDateTime.now());
        report.setStatus("active");
        
        // Save report
        int reportId = reportService.createReport(report);
        
        if (reportId > 0) {
            LOGGER.log(Level.INFO, "Report created: {0}, ID: {1}", new Object[]{title, reportId});
            request.setAttribute("successMessage", "Report created successfully");
            
            // Force a redirect to ensure fresh data load
            response.sendRedirect("AdminReportServlet?refresh=true");
        } else {
            request.setAttribute("errorMessage", "Failed to create report");
            showCreateReportForm(request, response);
        }
    }
    
    /**
     * Updates an existing report
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void updateReport(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            // Get form parameters
            int reportId = Integer.parseInt(request.getParameter("reportId"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String reportType = request.getParameter("reportType");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            
            // Validate input
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Report title is required");
                request.setAttribute("reportId", reportId);
                showEditReportForm(request, response);
                return;
            }
            
            if (reportType == null || reportType.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Report type is required");
                request.setAttribute("reportId", reportId);
                showEditReportForm(request, response);
                return;
            }
            
            // Get existing report
            Report report = reportService.getReportById(reportId);
            
            if (report == null) {
                request.setAttribute("errorMessage", "Report not found");
                showReportsDashboard(request, response);
                return;
            }
            
            // Parse dates
            LocalDate startDate = null;
            LocalDate endDate = null;
            
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }
            
            // Update report object
            report.setTitle(title);
            report.setDescription(description);
            report.setReportType(reportType);
            report.setStartDate(startDate);
            report.setEndDate(endDate);
            report.setUpdatedAt(LocalDateTime.now());
            
            // Save updated report
            boolean updated = reportService.updateReport(report);
            
            if (updated) {
                LOGGER.log(Level.INFO, "Report updated: {0}, ID: {1}", new Object[]{title, reportId});
                request.setAttribute("successMessage", "Report updated successfully");
                
                // Force a redirect to ensure fresh data load
                response.sendRedirect("AdminReportServlet?refresh=true");
            } else {
                request.setAttribute("errorMessage", "Failed to update report");
                request.setAttribute("reportId", reportId);
                showEditReportForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid report ID format", e);
            request.setAttribute("errorMessage", "Invalid report ID");
            showReportsDashboard(request, response);
        }
    }
    
    /**
     * Deletes a report
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void deleteReport(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int reportId = Integer.parseInt(request.getParameter("reportId"));
            
            // Delete report
            boolean deleted = reportService.deleteReport(reportId);
            
            if (deleted) {
                LOGGER.log(Level.INFO, "Report deleted: ID {0}", reportId);
                request.setAttribute("successMessage", "Report deleted successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to delete report");
            }
            
            // Force a redirect to ensure fresh data load
            response.sendRedirect("AdminReportServlet?refresh=true");
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid report ID format", e);
            request.setAttribute("errorMessage", "Invalid report ID");
            showReportsDashboard(request, response);
        }
    }
    
    /**
     * Generates a new report
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void generateReport(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        // Get form parameters
        String reportType = request.getParameter("reportType");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String title = request.getParameter("title");
        
        // Validate input
        if (reportType == null || reportType.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Report type is required");
            showReportsDashboard(request, response);
            return;
        }
        
        // Parse dates
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        if (startDateStr != null && !startDateStr.trim().isEmpty()) {
            startDate = LocalDate.parse(startDateStr);
        }
        
        if (endDateStr != null && !endDateStr.trim().isEmpty()) {
            endDate = LocalDate.parse(endDateStr);
        }
        
        // Generate default title if not provided
        if (title == null || title.trim().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateRange = "";
            
            if (startDate != null && endDate != null) {
                dateRange = " (" + startDate.format(formatter) + " to " + endDate.format(formatter) + ")";
            } else if (startDate != null) {
                dateRange = " (from " + startDate.format(formatter) + ")";
            } else if (endDate != null) {
                dateRange = " (until " + endDate.format(formatter) + ")";
            }
            
            title = reportType + " Report" + dateRange;
        }
        
        // Create report object
        Report report = new Report();
        report.setTitle(title);
        report.setDescription("Automatically generated " + reportType + " report");
        report.setReportType(reportType);
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setCreatedBy((int) request.getSession().getAttribute("userId"));
        report.setCreatedAt(LocalDateTime.now());
        report.setStatus("active");
        
        // Save report
        int reportId = reportService.createReport(report);
        
        if (reportId > 0) {
            LOGGER.log(Level.INFO, "Report generated: {0}, ID: {1}", new Object[]{title, reportId});
            
            // Redirect to view the generated report
            response.sendRedirect("AdminReportServlet?action=view&id=" + reportId);
        } else {
            request.setAttribute("errorMessage", "Failed to generate report");
            showReportsDashboard(request, response);
        }
    }
    
    /**
     * Gets the content type for a file format
     * 
     * @param format The file format
     * @return The content type
     */
    private String getContentType(String format) {
        switch (format.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "csv":
                return "text/csv";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return "application/octet-stream";
        }
    }
}
