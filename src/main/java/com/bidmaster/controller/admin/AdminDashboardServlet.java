package com.bidmaster.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
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

import com.bidmaster.model.Item;
import com.bidmaster.model.User;
import com.bidmaster.service.BidService;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.UserService;
import com.bidmaster.service.impl.BidServiceImpl;
import com.bidmaster.service.impl.ItemServiceImpl;
import com.bidmaster.service.impl.UserServiceImpl;

/**
 * Servlet implementation class AdminDashboardServlet
 * Handles the admin dashboard display with statistics and recent data
 */
@WebServlet("/admin/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdminDashboardServlet.class.getName());
    
    private UserService userService;
    private ItemService itemService;
    private BidService bidService;
    
    public void init() {
        userService = new UserServiceImpl();
        itemService = new ItemServiceImpl();
        bidService = new BidServiceImpl();
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
            // Get statistics for dashboard
            Map<String, Object> stats = getStatistics();
            request.setAttribute("stats", stats);
            
            // Get recent users
            List<User> recentUsers = userService.getRecentUsers(5);
            request.setAttribute("recentUsers", recentUsers);
            
            // Get recent items
            List<Item> recentItems = itemService.getRecentItems(5);
            request.setAttribute("recentItems", recentItems);
            
            // Forward to dashboard
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in AdminDashboardServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Collects statistics for the admin dashboard
     * 
     * @return Map containing various statistics
     * @throws SQLException if a database error occurs
     */
    private Map<String, Object> getStatistics() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        // User statistics
        int userCount = userService.getUserCount();
        int newUserCount = userService.getNewUserCount(7); // New users in last 7 days
        
        // Item statistics
        int activeItemCount = itemService.getActiveItemCount();
        int newItemCount = itemService.getNewItemCount(7); // New items in last 7 days
        
        // Bid statistics
        int bidCount = bidService.getTotalBidCount();
        int newBidCount = bidService.getNewBidCount(1); // New bids in last 24 hours
        
        // Revenue statistics (simplified)
        double revenue = 5680.00; // Placeholder - would come from a transaction service
        double revenueIncrease = 450.00; // Placeholder
        
        // Add all statistics to the map
        stats.put("userCount", userCount);
        stats.put("newUserCount", newUserCount);
        stats.put("activeItemCount", activeItemCount);
        stats.put("newItemCount", newItemCount);
        stats.put("bidCount", bidCount);
        stats.put("newBidCount", newBidCount);
        stats.put("revenue", revenue);
        stats.put("revenueIncrease", revenueIncrease);
        
        return stats;
    }
}
