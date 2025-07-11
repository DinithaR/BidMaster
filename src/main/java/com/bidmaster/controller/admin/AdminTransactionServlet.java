package com.bidmaster.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.bidmaster.model.Transaction;
import com.bidmaster.service.TransactionService;
import com.bidmaster.service.impl.TransactionServiceImpl;

/**
 * Servlet implementation class AdminTransactionServlet
 * Handles transaction management in the admin panel
 */
@WebServlet("/admin/AdminTransactionServlet")
public class AdminTransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminTransactionServlet.class.getName());
    
    private TransactionService transactionService;
    
    public void init() {
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
        
        try {
            // Get filter parameters
            String status = request.getParameter("status");
            String searchTerm = request.getParameter("searchTerm");
            
            List<Transaction> transactions;
            
            // Apply filters if provided
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Search functionality would need to be implemented in your DAO/Service
                transactions = transactionService.searchTransactions(searchTerm);
                request.setAttribute("searchTerm", searchTerm);
            } else if (status != null && !status.trim().isEmpty()) {
                // Filter by status
                transactions = transactionService.getTransactionsByStatus(status);
                request.setAttribute("selectedStatus", status);
            } else {
                // Get all transactions
                transactions = transactionService.getAllTransactions();
            }
            
            // Get revenue statistics
            double totalRevenue = transactionService.getTotalRevenue();
            double monthlyRevenue = transactionService.getRevenueForPeriod(30);
            double weeklyRevenue = transactionService.getRevenueForPeriod(7);
            
            // Set attributes for JSP
            request.setAttribute("transactions", transactions);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("monthlyRevenue", monthlyRevenue);
            request.setAttribute("weeklyRevenue", weeklyRevenue);
            
            // Forward to transactions JSP
            request.getRequestDispatcher("/admin/transactions.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminTransactionServlet", e);
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
        
        if (action == null) {
            response.sendRedirect("AdminTransactionServlet");
            return;
        }
        
        try {
            switch (action) {
                case "updateStatus":
                    updateTransactionStatus(request, response);
                    break;
                default:
                    response.sendRedirect("AdminTransactionServlet");
                    break;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminTransactionServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Handles updating a transaction's status
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @throws SQLException if a database error occurs
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    private void updateTransactionStatus(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int transactionId = Integer.parseInt(request.getParameter("transactionId"));
            String status = request.getParameter("status");
            
            if (status == null || status.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Status cannot be empty");
                doGet(request, response);
                return;
            }
            
            boolean updated = transactionService.updateTransactionStatus(transactionId, status);
            
            if (updated) {
                LOGGER.log(Level.INFO, "Transaction status updated: ID {0}, Status: {1}", 
                        new Object[]{transactionId, status});
                request.setAttribute("successMessage", "Transaction status updated successfully");
            } else {
                LOGGER.log(Level.WARNING, "Failed to update transaction status: ID {0}", transactionId);
                request.setAttribute("errorMessage", "Failed to update transaction status");
            }
            
            doGet(request, response);
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid transaction ID format", e);
            request.setAttribute("errorMessage", "Invalid transaction ID");
            doGet(request, response);
        }
    }
}
