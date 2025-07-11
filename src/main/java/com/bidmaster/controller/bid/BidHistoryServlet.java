package com.bidmaster.controller.bid;

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

import com.bidmaster.model.Bid;
import com.bidmaster.service.BidService;
import com.bidmaster.service.impl.BidServiceImpl;

/**
 * Servlet implementation class BidHistoryServlet
 * Handles displaying the bid history for a user or an item
 */
@WebServlet("/BidHistoryServlet")
public class BidHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BidHistoryServlet.class.getName());
    
    private BidService bidService;
    
    public void init() {
        bidService = new BidServiceImpl();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Check if request is for item bids or user bids
            String itemIdParam = request.getParameter("itemId");
            
            if (itemIdParam != null && !itemIdParam.isEmpty()) {
                // Get bids for a specific item
                int itemId = Integer.parseInt(itemIdParam);
                List<Bid> bids = bidService.getBidsByItem(itemId);
                request.setAttribute("bids", bids);
                request.setAttribute("itemId", itemId);
                request.getRequestDispatcher("bids/item-bids.jsp").forward(request, response);
            } else {
                // Get bids for the logged-in user
                int userId = (int) session.getAttribute("userId");
                List<Bid> bids = bidService.getBidsByBidder(userId);
                request.setAttribute("bids", bids);
                request.getRequestDispatcher("bids/user-bids.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid item ID format", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in BidHistoryServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward POST requests to doGet
        doGet(request, response);
    }
}
