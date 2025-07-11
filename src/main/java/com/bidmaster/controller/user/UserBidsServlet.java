package com.bidmaster.controller.user;

import java.io.IOException;
import java.math.BigDecimal;
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

import com.bidmaster.model.Bid;
import com.bidmaster.model.Item;
import com.bidmaster.service.BidService;
import com.bidmaster.service.ItemService;
import com.bidmaster.service.impl.BidServiceImpl;
import com.bidmaster.service.impl.ItemServiceImpl;

@WebServlet("/UserBidsServlet")
public class UserBidsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UserBidsServlet.class.getName());
    
    private BidService bidService;
    private ItemService itemService;
    
    public void init() {
        bidService = new BidServiceImpl();
        itemService = new ItemServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        try {
            // Get user's bids
            List<Bid> userBids = bidService.getBidsByBidder(userId);
            
            // Set attribute for JSP
            request.setAttribute("userBids", userBids);
            
            // Forward to profile page
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in UserBidsServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        String action = request.getParameter("action");
        
        try {
            if ("delete".equals(action)) {
                // Delete bid
                int bidId = Integer.parseInt(request.getParameter("bidId"));
                
                // Verify bid belongs to user
                Bid bid = bidService.getBidById(bidId);
                if (bid == null || bid.getBidderId() != userId) {
                    session.setAttribute("errorMessage", "You can only delete your own bids");
                    response.sendRedirect("ProfileServlet");
                    return;
                }
                
                // Delete the bid
                boolean deleted = bidService.cancelBid(bidId);
                
                if (deleted) {
                    session.setAttribute("successMessage", "Bid deleted successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to delete bid");
                }
                
            } else if ("update".equals(action)) {
                // Update bid
                int bidId = Integer.parseInt(request.getParameter("bidId"));
                BigDecimal newAmount = new BigDecimal(request.getParameter("bidAmount"));
                
                // Verify bid belongs to user
                Bid bid = bidService.getBidById(bidId);
                if (bid == null || bid.getBidderId() != userId) {
                    session.setAttribute("errorMessage", "You can only update your own bids");
                    response.sendRedirect("ProfileServlet");
                    return;
                }
                
                // Get item details
                Item item = itemService.getItemById(bid.getItemId());
                
                // Check if new amount is higher than current price
                if (newAmount.compareTo(item.getCurrentPrice()) <= 0) {
                    session.setAttribute("errorMessage", "New bid amount must be higher than current price");
                    response.sendRedirect("ProfileServlet");
                    return;
                }
                
                // Update the bid
                bid.setBidAmount(newAmount);
                boolean updated = bidService.updateBid(bid);
                
                if (updated) {
                    // Update item's current price
                    item.setCurrentPrice(newAmount);
                    itemService.updateItem(item);
                    
                    session.setAttribute("successMessage", "Bid updated successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to update bid");
                }
            }
            
            // Redirect back to profile
            response.sendRedirect("ProfileServlet");
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid input format", e);
            session.setAttribute("errorMessage", "Invalid input. Please enter a valid bid amount.");
            response.sendRedirect("ProfileServlet");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in UserBidsServlet", e);
            session.setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect("ProfileServlet");
        }
    }
}
