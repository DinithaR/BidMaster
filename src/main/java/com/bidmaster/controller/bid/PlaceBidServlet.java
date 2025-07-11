package com.bidmaster.controller.bid;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
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

@WebServlet("/PlaceBidServlet")
public class PlaceBidServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(PlaceBidServlet.class.getName());
    
    private BidService bidService;
    private ItemService itemService;
    
    public void init() {
        bidService = new BidServiceImpl();
        itemService = new ItemServiceImpl();
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
        
        int bidderId = (int) session.getAttribute("userId");
        
        try {
            // Get parameters
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            BigDecimal bidAmount = new BigDecimal(request.getParameter("bidAmount"));
            
            // Get item details
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                LOGGER.log(Level.WARNING, "Item not found: {0}", itemId);
                request.setAttribute("errorMessage", "Item not found");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Check if auction is still active
            if (!"active".equals(item.getStatus())) {
                LOGGER.log(Level.WARNING, "Attempt to bid on inactive auction: {0}", itemId);
                request.setAttribute("errorMessage", "This auction is no longer active");
                request.getRequestDispatcher("ItemDetailsServlet?id=" + itemId).forward(request, response);
                return;
            }
            
            // Check if user is not the seller
            if (item.getSellerId() == bidderId) {
                LOGGER.log(Level.WARNING, "Seller attempted to bid on own item: {0}", itemId);
                request.setAttribute("errorMessage", "You cannot bid on your own item");
                request.getRequestDispatcher("ItemDetailsServlet?id=" + itemId).forward(request, response);
                return;
            }
            
            // Check if bid amount is higher than current price
            if (bidAmount.compareTo(item.getCurrentPrice()) <= 0) {
                LOGGER.log(Level.WARNING, "Bid amount too low: {0} (current price: {1})", 
                        new Object[]{bidAmount, item.getCurrentPrice()});
                request.setAttribute("errorMessage", "Your bid must be higher than the current price");
                request.getRequestDispatcher("ItemDetailsServlet?id=" + itemId).forward(request, response);
                return;
            }
            
            // Create bid object
            Bid bid = new Bid();
            bid.setItemId(itemId);
            bid.setBidderId(bidderId);
            bid.setBidAmount(bidAmount);
            bid.setStatus("active");
            
            // Place bid
            int bidId = bidService.placeBid(bid);
            
            if (bidId > 0) {
                LOGGER.log(Level.INFO, "Bid placed successfully: ID {0}, Amount: {1}, Item: {2}", 
                        new Object[]{bidId, bidAmount, itemId});
                
                // Update item's current price
                item.setCurrentPrice(bidAmount);
                itemService.updateItem(item);
                
                // Update previous highest bid status to 'outbid'
                bidService.updatePreviousHighestBid(itemId, bidId);
                
                // Set success message in session to survive the redirect
                session.setAttribute("successMessage", "Your bid of $" + bidAmount + " has been placed successfully!");
                
                // Redirect back to item details
                response.sendRedirect("ItemDetailsServlet?id=" + itemId);
            } else {
                request.setAttribute("errorMessage", "Failed to place bid. Please try again.");
                request.getRequestDispatcher("ItemDetailsServlet?id=" + itemId).forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid input format", e);
            request.setAttribute("errorMessage", "Invalid input. Please enter a valid bid amount.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in PlaceBidServlet", e);
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in PlaceBidServlet", e);
            request.setAttribute("errorMessage", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
